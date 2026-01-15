package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.exception.ServiceNotFoundException;
import com.marthina.splitconnect.exception.SubscriptionNotFoundException;
import com.marthina.splitconnect.model.Services;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.repository.ServicesRepository;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.marthina.splitconnect.model.CurrencyUtils.getCurrencyByCountry;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subsRepository;
    private final ServicesRepository servicesRepository;

    public SubscriptionService(SubscriptionRepository subsRepository,
                               ServicesRepository servicesRepository) {
        this.subsRepository = subsRepository;
        this.servicesRepository = servicesRepository;
    }

    public SubscriptionDTO create(SubscriptionDTO dto) {

        // Criar ou buscar o serviço automaticamente
        Services service;
        if (dto.getServiceId() != null) {
            // Se o ID foi enviado, tenta buscar
            service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new ServiceNotFoundException(dto.getServiceId()));
        } else {
            // Se não enviou ID, cria um novo serviço com nome e tipo
            service = new Services();
            service.setName(dto.getServiceName());       // ex: "Amazon Prime"
            service.setType(dto.getServiceType());       // enum: MOVIES, STREAMING...
            service = servicesRepository.save(service);
        }

        Subscription subscription = new Subscription();
        subscription.setService(service);
        subscription.setCountry(dto.getCountry());
        subscription.setAmount(dto.getAmount());
        subscription.setDateStart(dto.getDateStart());
        subscription.setDateEnd(dto.getDateEnd());
        subscription.setCapacity(dto.getCapacity());


        Subscription saved = subsRepository.save(subscription);

        return toDTO(saved);
    }

    public SubscriptionDTO findById(Long id) {
        return toDTO(subsRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id)));
    }

    public List<SubscriptionDTO> findAll() {
        List<Subscription> subscriptions = subsRepository.findAll();
        List<SubscriptionDTO> response = new ArrayList<>();

        for (Subscription subscription : subscriptions) {
            response.add(toDTO(subscription));
        }

        return response;
        //com steam - return subsRepository.findAll().stream().map(this::toDTO).toList();
    }

    public SubscriptionDTO update(Long id, SubscriptionDTO dto) {
        Subscription existing = subsRepository.findById(id).orElseThrow(() -> new SubscriptionNotFoundException(id));

        existing.setCountry(dto.getCountry());
        existing.setDateEnd(dto.getDateEnd());
        existing.setAmount(dto.getAmount());

        return toDTO(subsRepository.save(existing));
    }

    public void delete(Long id) {
        subsRepository.deleteById(id);
    }

    private SubscriptionDTO toDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setServiceId(subscription.getService().getId());
        dto.setServiceName(subscription.getService().getName());
        dto.setServiceType(subscription.getService().getType());
        dto.setAmount(subscription.getAmount());
        dto.setCountry(subscription.getCountry());
        dto.setCurrency(getCurrencyByCountry(subscription.getCountry()));
        dto.setDateStart(subscription.getDateStart());
        dto.setDateEnd(subscription.getDateEnd());
        dto.setCapacity(subscription.getCapacity());
        return dto;
    }
}
