package com.marthina.splitconnect.service;

import com.marthina.splitconnect.exception.SubscriptionNotFoundException;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subsRepository;

    public SubscriptionService(SubscriptionRepository subsRepository) {
        this.subsRepository = subsRepository;
    }

    public Subscription create(Subscription subscription) {
        return subsRepository.save(subscription);
    }

    public Subscription findById(Long id) {
        return subsRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    public List<Subscription> findAll() {
        return subsRepository.findAll();
    }

    public Subscription update(Long id, Subscription updated) {
        Subscription existing = findById(id);
        // copiar campos necessários
        return subsRepository.save(existing);
    }

    public void delete(Long id) {
        subsRepository.deleteById(id);
    }
}
