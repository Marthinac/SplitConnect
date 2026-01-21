package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.SubscriptionRole;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionUserRepository extends JpaRepository<SubscriptionUser, Long> {
    // Verifica se o vínculo já existe
    boolean existsBySubscriptionAndUser(Subscription subscription, User user);

    // Verifica qual a role
    boolean existsBySubscriptionAndRole(Subscription subscription, SubscriptionRole role);

    // Conta quantos usuários estão na subscription
    long countBySubscription(Subscription subscription);

    // count para validar capacidade antes de permitir novos participantes.
    int countBySubscriptionAndActiveTrue(Subscription subscription);

    // Lista todos os usuários de uma subscription
    List<SubscriptionUser> findBySubscription(Subscription subscription);

    // Busca o vínculo específico (para remoção)
    Optional<SubscriptionUser> findBySubscriptionAndUser(Subscription subscription, User user);
    Optional<SubscriptionUser> findBySubscriptionIdAndUserId(Long subsId, Long userId);

    //todo when status active
    //List<SubscriptionUser> findBySubscriptionIdAndActiveTrue(Long subscriptionId);

}
