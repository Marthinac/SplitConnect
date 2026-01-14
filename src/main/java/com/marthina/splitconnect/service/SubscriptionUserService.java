package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.exception.*;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.SubscriptionRole;
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
                throw new RuntimeException("Já existe um OWNER nesta subscription");
            }
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

    //removes a user from a subscription
    @Transactional
    public void removeUser(Long subscriptionId, Long userId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        //precisa ser id? .findBySubscriptionidAndUserid
        SubscriptionUser subscriptionUser = subscriptionUserRepository
                .findBySubscriptionAndUser(subscription, user)
                .orElseThrow(() -> new SubscriptionUserNotFoundException(subscriptionId, userId));

        //todo exception
        if (subscriptionUser.getRole() == SubscriptionRole.OWNER) {
            throw new RuntimeException("OWNER can't be removed.");
        }

        subscriptionUserRepository.delete(subscriptionUser);
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
