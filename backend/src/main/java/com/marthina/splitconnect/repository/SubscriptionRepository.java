package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // para operações que só o dono pode fazer
    Optional<Subscription> findByIdAndOwnerId(Long id, Long ownerId);

    List<Subscription> findByStatusAndCountry(SubscriptionStatus status, Country country);
}
