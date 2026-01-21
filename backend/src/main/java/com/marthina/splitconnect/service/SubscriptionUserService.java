package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.exception.*;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.SubscriptionRole;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Gerenciar o vínculo entre usuários e assinaturas, garantindo regras como: não ultrapassar a capacidade
//não permitir usuários duplicados, definir papéis (OWNER, MEMBER), centralizar toda a lógica dessa relação
@Service
public class SubscriptionUserService {

    private final SubscriptionUserRepository subscriptionUserRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;


    public SubscriptionUserService(SubscriptionUserRepository subscriptionUserRepository,
                                   UserRepository userRepository,
                                   SubscriptionRepository subscriptionRepository,
                                   SubscriptionService subscriptionService) {
        this.subscriptionUserRepository = subscriptionUserRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionService = subscriptionService;
    }

    //TODO STATUS
    /*
    {
        public SubscriptionUserDTO requestJoin (Long subscriptionId, Long userId){
        // mesmas validações (ativa, vaga, não duplicado)

        SubscriptionUser subsUser = new SubscriptionUser(user, subscription, SubscriptionRole.MEMBER);
        subsUser.setStatus(SubscriptionStatus.PENDING); // pedido pendente
        SubscriptionUser saved = subscriptionUserRepository.save(subsUser);
        return toResponseDTO(saved);
    }

        public SubscriptionUserDTO approveJoin (Long subscriptionUserId, Long ownerId){
        SubscriptionUser subsUser = subscriptionUserRepository.findById(subscriptionUserId).orElseThrow();
        Subscription subscription = subsUser.getSubscription();

        if (!subscription.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Only owner can approve");
        }

        subsUser.setStatus(SubscriptionUserStatus.APPROVED);
        return toResponseDTO(subsUserRepository.save(subsUser));
    }
    */

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

        subscriptionService.checkAndExpire(subscription);
        subscriptionRepository.save(subscription);

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new RuntimeException("Subscription is not active");
        }

        long usedSlots = subscriptionUserRepository.countBySubscription(subscription);
        if (usedSlots >= subscription.getCapacity()) {
            throw new RuntimeException("No vacancies available. The limit capacity for this subscription is already reached.");
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
        dto.setActive(subsUser.getActive() != null ? subsUser.getActive() : true);
        dto.setSubscriptionStatus(subsUser.getSubscription().getStatus());
        return dto;
    }

}
