package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionUserRepository extends JpaRepository<SubscriptionUser, Long> {

    boolean existsBySubscriptionAndUser(Subscription subscription, User user);
    long countBySubscription(Subscription subscription);
}
