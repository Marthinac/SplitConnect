package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.exception.SubscriptionNotFoundException;
import com.marthina.splitconnect.exception.UserNotFoundException;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subsRepository;

    public SubscriptionService(SubscriptionRepository subsRepository) {
        this.subsRepository = subsRepository;
    }

    public SubscriptionDTO create(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setServiceId(dto.getServiceId());
        subscription.setAmount(dto.getAmount());
        subscription.setDateStart(dto.getDateStart());
        subscription.setDateEnd(dto.getDateEnd());
        subscription.setCapacity(dto.getCapacity());
        subscription.setCountry(dto.getCountry());

        Subscription saved = subsRepository.save(subscription);

        return toDTO(saved);
    }

    public Subscription findById(Long id) {
        return subsRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
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

    private SubscriptionDTO toDTO(Subscription s) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(s.getId());
        //todo dto.setServiceId(s.getService());
        dto.setAmount(s.getAmount());
        dto.setDateStart(s.getDateStart());
        dto.setDateEnd(s.getDateEnd());
        dto.setCapacity(s.getCapacity());
        dto.setCountry(s.getCountry());
        return dto;
    }
}
