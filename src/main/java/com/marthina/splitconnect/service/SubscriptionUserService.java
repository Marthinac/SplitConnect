package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.exception.*;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.SubscriptionRole;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Gerenciar o vínculo entre usuários e assinaturas, garantindo regras como: não ultrapassar a capacidade
//não permitir usuários duplicados, definir papéis (OWNER, MEMBER), centralizar toda a lógica dessa relação
@Service
public class SubscriptionUserService {

    private final SubscriptionUserRepository subscriptionUserRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionUserService(SubscriptionUserRepository subscriptionUserRepository,
                                   UserRepository userRepository,
                                   SubscriptionRepository subscriptionRepository) {
        this.subscriptionUserRepository = subscriptionUserRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    //add user in a subscription
    @Transactional
    public SubscriptionUserDTO addUser(Long subscriptionId, SubscriptionUserDTO dto) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        //todo exception
        if (dto.getRole() == SubscriptionRole.OWNER) {
            boolean ownerExists = subscriptionUserRepository.existsBySubscriptionAndRole(subscription, SubscriptionRole.OWNER);
            if (ownerExists) {
                throw new RuntimeException("There's already an OWNER for this subscription");
            }
        }

        if (subscription.getCapacity() == subscriptionUserRepository.countBySubscription(subscription)) {
            throw new RuntimeException("The limit capacity for this subscription is already reached.");
        }

        if (subscriptionUserRepository.existsBySubscriptionAndUser(subscription, user)) {
            throw new UserAlreadyInSubscriptionException(subscription, user);
        }

        long count = subscriptionUserRepository.countBySubscription(subscription);
        if (count >= subscription.getCapacity()) {
            throw new MaximumCapacityException(subscription);
        }

        SubscriptionUser subscriptionUser =
                new SubscriptionUser(user, subscription, dto.getRole());

        SubscriptionUser saved = subscriptionUserRepository.save(subscriptionUser);

        return toResponseDTO(saved);
    }

    //list users from a subscription
    @Transactional(readOnly = true)
    public List<SubscriptionUserDTO> findUsersBySubscription(Long subscriptionId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        return subscriptionUserRepository.findBySubscription(subscription)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //removes a user from a subscription actionUserId - who wants to remove | targetUserId - who will be removed
    @Transactional
    public void removeUser(Long subscriptionId, Long actionUserId, Long targetUserId ) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User actionUser = userRepository.findById(actionUserId)
                .orElseThrow(() -> new UserNotFoundException(actionUserId));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(targetUserId));

        SubscriptionUser actionSubsUser = subscriptionUserRepository
                .findBySubscriptionAndUser(subscription, actionUser)
                .orElseThrow(() -> new SubscriptionUserNotFoundException(subscriptionId, actionUserId));

        SubscriptionUser targetSubsUser = subscriptionUserRepository
                .findBySubscriptionAndUser(subscription, targetUser)
                .orElseThrow(() -> new SubscriptionUserNotFoundException(subscriptionId, targetUserId));

        if (actionSubsUser.getRole() != SubscriptionRole.OWNER) {
            throw new RuntimeException("Only OWNER can remove members");
        }

        //todo exception
        if (targetSubsUser.getRole() == SubscriptionRole.OWNER) {
            throw new RuntimeException("OWNER can't be removed.");
        }

        subscriptionUserRepository.delete(targetSubsUser);
    }


    private SubscriptionUserDTO toResponseDTO(SubscriptionUser subsUser) {
        SubscriptionUserDTO dto = new SubscriptionUserDTO();
        dto.setId(subsUser.getId());
        dto.setUserId(subsUser.getUser().getId());
        dto.setSubsId(subsUser.getSubscription().getId());
        dto.setRole(subsUser.getRole());
        dto.setDate(subsUser.getCreatedAt());
        return dto;
    }

}
