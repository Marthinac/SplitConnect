package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // para operações que só o dono pode fazer
    Subscription findByIdAndOwnerId(Long id, Long ownerId);
}
