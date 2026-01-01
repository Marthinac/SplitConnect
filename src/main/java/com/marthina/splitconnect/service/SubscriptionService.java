package com.marthina.splitconnect.service;

import com.marthina.splitconnect.exception.EmailAlreadyInUseException;
import com.marthina.splitconnect.models.Subscription;
import com.marthina.splitconnect.models.User;
import com.marthina.splitconnect.repository.SubscriptionRepository;

import java.util.List;

public class SubscriptionService {

    private final SubscriptionRepository subsRepository;

    public SubscriptionService(SubscriptionRepository subsRepository)  { this.subsRepository = subsRepository; }

    //public Subscription create(Subscription subscription) {
        // um usuário pode ter mais que uma subscrição? }

    public List<Subscription> findAll() {
        return subsRepository.findAll();
    }

   // public Subscription update(Long id, Subscription updated) { }

    public void delete(Long id) {
        subsRepository.deleteById(id);
    }

}
