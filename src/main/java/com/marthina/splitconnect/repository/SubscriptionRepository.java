package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.models.Subscription;
import com.marthina.splitconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByEmail(String email);
}
