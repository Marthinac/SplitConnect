package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.AvailableSubscriptionDTO;
import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.exception.*;
import com.marthina.splitconnect.model.*;
import com.marthina.splitconnect.model.enums.*;
import com.marthina.splitconnect.repository.ServicesRepository;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subsRepository;
    private final ServicesRepository servicesRepository;
    private final UserRepository userRepository;
    private final SubscriptionUserRepository subscriptionUserRepository;

    public SubscriptionService(SubscriptionRepository subsRepository,
                               ServicesRepository servicesRepository,
                               UserRepository userRepository,
                               SubscriptionUserRepository subscriptionUserRepository) {
        this.subsRepository = subsRepository;
        this.servicesRepository = servicesRepository;
        this.userRepository = userRepository;
        this.subscriptionUserRepository = subscriptionUserRepository;
    }

    @Transactional
    public SubscriptionDTO create(Long ownerUserId, SubscriptionDTO dto) {

        validateSubscriptionDTO(dto);

        // Criar ou buscar o serviço automaticamente
        Services service;
        if (dto.getServiceId() != null) {
            // Se o ID foi enviado, tenta buscar
            service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new ServiceNotFoundException(dto.getServiceId()));
        } else {
            // Se não enviou ID, cria um novo serviço com nome e tipo
            service = new Services();
            service.setName(dto.getServiceName());
            service.setType(dto.getServiceType());
            service = servicesRepository.save(service);
        }

        User owner = userRepository.findById(ownerUserId)
                .orElseThrow(() -> new UserNotFoundException(ownerUserId));

        Subscription subscription = new Subscription();
        subscription.setService(service);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setCountry(dto.getCountry());
        subscription.setAmount(dto.getAmount());
        subscription.setDateStart(dto.getDateStart());
        subscription.setDateEnd(dto.getDateEnd());
        subscription.setCapacity(dto.getCapacity());
        subscription.setOwner(owner);

        Subscription saved = subsRepository.save(subscription);

        SubscriptionUser ownerLink = new SubscriptionUser(
                owner,
                subscription,
                SubscriptionRole.OWNER
        );

        subscriptionUserRepository.save(ownerLink);

        return toDTO(saved);
    }

    public SubscriptionDTO findById(Long id) {
        Subscription subscription = subsRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
        checkAndExpire(subscription);
        return toDTO(subscription);
    }

    @Transactional
    public Page<SubscriptionDTO> findAll(Pageable pageable) {
        Page<Subscription> subscriptionsPage = subsRepository.findAll(pageable);
        List<SubscriptionDTO> response = new ArrayList<>();

        for (Subscription subscription : subscriptionsPage.getContent()) {
            checkAndExpire(subscription);
            subsRepository.save(subscription);
            if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
                response.add(toDTO(subscription));
            }
        }

        return new PageImpl<>(response, pageable, subscriptionsPage.getTotalElements());
    }


    @Transactional(readOnly = true)
    public Page<AvailableSubscriptionDTO> findAvailable(
            Pageable pageable,
            Country country,
            ServicesType serviceType) {

        return subsRepository.findAvailableSubscriptions(pageable, country, serviceType)
                .map(this::toAvailableSubscriptionDTO);
    }

    @Transactional(readOnly = true)
    public Page<AvailableSubscriptionDTO> findFiltered(Country country, ServicesType serviceType,
                                              BigDecimal maxPrice, Boolean hasVacancy, String serviceName, Pageable pageable) {

        return subsRepository.findFiltered(country, serviceType, maxPrice,
                hasVacancy, serviceName, pageable).map(this::toAvailableSubscriptionDTO);
    }


    private AvailableSubscriptionDTO toAvailableSubscriptionDTO(Subscription subscription) {
        int usedSlots = subscriptionUserRepository
                .countBySubscriptionAndStatus(subscription, SubscriptionUserStatus.APPROVED);

        boolean hasVacancy = usedSlots < subscription.getCapacity()
                && subscription.getStatus() == SubscriptionStatus.ACTIVE;

        AvailableSubscriptionDTO dto = new AvailableSubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setServiceName(subscription.getService().getName());
        dto.setOwnerName(subscription.getOwner().getName());
        dto.setCountry(subscription.getCountry());
        dto.setTotalSlots(subscription.getCapacity());
        dto.setCurrency(CurrencyUtils.getCurrencyByCountry(subscription.getCountry()));
        dto.setFormattedAmount(CurrencyUtils.formatAmount(subscription.getAmount(), subscription.getCountry()));
        dto.setFormattedPricePerUser(CurrencyUtils.formatAmountPerPerson(subscription.getAmount(), subscription.getCapacity(), subscription.getCountry()));
        dto.setUsedSlots(usedSlots);
        dto.setHasVacancy(hasVacancy);
        dto.setStatus(subscription.getStatus());

        return dto;
    }

    public void checkAndExpire(Subscription subscription) {
        if (subscription.getDateEnd() != null &&
                subscription.getDateEnd().isBefore(LocalDate.now()) &&
                subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
        }
    }

    @Transactional
    public SubscriptionDTO update(Long id, Long ownerId, SubscriptionDTO dto) {
        Subscription existing = subsRepository.findById(id).orElseThrow(() -> new SubscriptionNotFoundException(id));

        if (!isOwner(ownerId, id)) {
            throw new OnlyOwnerCanUpdateException();
        }

        // > 0 and bigger than approveds
        if (dto.getCapacity() != null) {
            if (dto.getCapacity() <= 0) {
                throw new InvalidSubscriptionCapacityException(dto.getCapacity());
            }
            int approvedCount = subscriptionUserRepository
                    .countBySubscriptionAndStatus(existing, SubscriptionUserStatus.APPROVED);
            if (dto.getCapacity() < approvedCount) {
                throw new CannotReduceCapacityException(existing.getCapacity(), dto.getCapacity());
            }
            existing.setCapacity(dto.getCapacity());
        }

        // dateEnd >= dateStart
        if (dto.getDateEnd() != null) {
            if (dto.getDateEnd().isBefore(existing.getDateStart())) {
                throw new InvalidSubscriptionDateRangeException(
                        existing.getDateStart(), dto.getDateEnd());
            }
            existing.setDateEnd(dto.getDateEnd());
        }

        // > 0
        if (dto.getAmount() != null) {
            if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSubscriptionAmountException(dto.getAmount());
            }
            existing.setAmount(dto.getAmount());
        }

        if (dto.getCountry() != null) {
            existing.setCountry(dto.getCountry());
        }

        return toDTO(subsRepository.save(existing));
    }

    private boolean isOwner(Long userId, Long subscriptionId) {
        return subscriptionUserRepository
                .countBySubscriptionIdAndUserIdAndRole(subscriptionId, userId, SubscriptionRole.OWNER) > 0;
    }

    @Transactional
    public void cancel(Long id, Long ownerId) {
        Subscription subscription = subsRepository
                .findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new NotSubscriptionOwnerException(id, ownerId));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subsRepository.save(subscription);
    }

    private SubscriptionDTO toDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setServiceId(subscription.getService().getId());
        dto.setServiceName(subscription.getService().getName());
        dto.setServiceType(subscription.getService().getType());
        dto.setStatus(subscription.getStatus());
        dto.setAmount(subscription.getAmount());
        dto.setCountry(subscription.getCountry());
        dto.setCapacity(subscription.getCapacity());
        dto.setCurrency(CurrencyUtils.getCurrencyByCountry(subscription.getCountry()));
        dto.setFormattedAmount(CurrencyUtils.formatAmount(subscription.getAmount(), subscription.getCountry()));
        dto.setFormattedPricePerUser(CurrencyUtils.formatAmountPerPerson(subscription.getAmount(), subscription.getCapacity(), subscription.getCountry()));
        dto.setDateStart(subscription.getDateStart());
        dto.setDateEnd(subscription.getDateEnd());
        dto.setUsedSlots(subscriptionUserRepository.
                countBySubscriptionAndStatus(subscription, SubscriptionUserStatus.APPROVED));
        dto.setHasVacancy(dto.getUsedSlots() < subscription.getCapacity() &&
                subscription.getStatus() == SubscriptionStatus.ACTIVE);
        return dto;
    }

    private void validateSubscriptionDTO(SubscriptionDTO dto) {
        //capacity
        if (dto.getCapacity() == null || dto.getCapacity() <= 0) {
            throw new InvalidSubscriptionCapacityException(dto.getCapacity());
        }

        //amount
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSubscriptionAmountException(dto.getAmount());
        }

        //dates
        if (dto.getDateStart() == null || dto.getDateEnd() == null) {
            throw new InvalidSubscriptionDatesException();
        }

        if (dto.getDateEnd().isBefore(dto.getDateStart())) {
            throw new InvalidSubscriptionDateRangeException(
                    dto.getDateStart(), dto.getDateEnd());
        }

        //service name
        if (dto.getServiceName() == null || dto.getServiceName().trim().isEmpty()) {
            throw new InvalidServiceNameException();
        }
    }
}