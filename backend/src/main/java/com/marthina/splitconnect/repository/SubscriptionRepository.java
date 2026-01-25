package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.ServicesType;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // para operações que só o dono pode fazer
    Optional<Subscription> findByIdAndOwnerId(Long id, Long ownerId);

    Page<Subscription> findByStatusAndDateEndAfter(SubscriptionStatus status, LocalDate dateEnd, Pageable pageable);

    List<Subscription> findByStatusAndCountry(SubscriptionStatus status, Country country);

    //country and serviceType optional
    @Query("""
        SELECT s FROM Subscription s\s
        JOIN FETCH s.owner\s
        JOIN FETCH s.service\s
        WHERE s.status = 'ACTIVE'\s
        AND s.dateEnd >= CURRENT_DATE
        AND EXISTS (
            SELECT 1 FROM SubscriptionUser su\s
            WHERE su.subscription = s\s
            AND su.status = 'APPROVED'
        )\s
        AND (:country IS NULL OR s.country = :country) 
        AND (:serviceType IS NULL OR s.service.type = :serviceType)
       \s""")
    Page<Subscription> findAvailableSubscriptions(
            Pageable pageable,
            @Param("country") Country country,
            @Param("serviceType") ServicesType serviceType);
}
