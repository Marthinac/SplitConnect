package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.exception.*;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.enums.SubscriptionRole;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import com.marthina.splitconnect.model.enums.SubscriptionUserStatus;
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

    @Transactional
    public SubscriptionUserDTO requestJoin (Long subscriptionId, Long userId){

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        subscriptionService.checkAndExpire(subscription);
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionNotActiveException(subscriptionId);
        }

        if (subscriptionUserRepository.existsBySubscriptionAndUser(subscription, user)) {
            throw new UserAlreadyInSubscriptionException(subscription, user);
        }

        int approvedCount = subscriptionUserRepository.countBySubscriptionAndStatus(subscription, SubscriptionUserStatus.APPROVED);
        if (approvedCount >= subscription.getCapacity()) {
            throw new MaximumCapacityException(subscription);
        }

        SubscriptionUser subsUser = new SubscriptionUser(user, subscription, SubscriptionRole.MEMBER);
        subsUser.setStatus(SubscriptionUserStatus.PENDING);
        SubscriptionUser saved = subscriptionUserRepository.save(subsUser);

        return toResponseDTO(saved);
    }

    @Transactional
    public SubscriptionUserDTO approveJoin (Long subscriptionUserId, Long ownerId){

        SubscriptionUser subsUser = subscriptionUserRepository.findByIdAndSubscriptionOwnerId(subscriptionUserId, ownerId)
                .orElseThrow(() -> new SubscriptionUserNotFoundException(subscriptionUserId, ownerId));

        if (subsUser.getStatus() != SubscriptionUserStatus.PENDING) {
            throw new AlreadyProcessedJoinRequestException(subscriptionUserId);
        }

        int approvedCount = subscriptionUserRepository
                .countBySubscriptionAndStatus(subsUser.getSubscription(), SubscriptionUserStatus.APPROVED);
        if (approvedCount >= subsUser.getSubscription().getCapacity()) {
            throw new MaximumCapacityException(subsUser.getSubscription());
        }

        subsUser.setStatus(SubscriptionUserStatus.APPROVED);
        return toResponseDTO(subscriptionUserRepository.save(subsUser));
    }

    @Transactional
    public SubscriptionUserDTO rejectJoin (Long subscriptionUserId, Long ownerId){

        SubscriptionUser subsUser = subscriptionUserRepository.findByIdAndSubscriptionOwnerId(subscriptionUserId, ownerId)
                .orElseThrow(() -> new SubscriptionUserNotFoundException(subscriptionUserId, ownerId));

        if (subsUser.getStatus() != SubscriptionUserStatus.PENDING) {
            throw new AlreadyProcessedJoinRequestException(subscriptionUserId);
        }

        subsUser.setStatus(SubscriptionUserStatus.REJECTED);
        return toResponseDTO(subscriptionUserRepository.save(subsUser));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionUserDTO> listPendingRequests(Long subscriptionId, Long ownerId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        if (!subscription.getOwner().getId().equals(ownerId)) {
            throw new NotSubscriptionOwnerException(subscription.getOwner().getId(), ownerId);
        }

        return subscriptionUserRepository
                .findBySubscriptionAndStatus(subscription, SubscriptionUserStatus.PENDING)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //add user in a subscription (owner creating the subs., owner inviting frinds to subs., admin add member)
    @Transactional
    public SubscriptionUserDTO addDirectUser(Long subscriptionId, SubscriptionUserDTO dto, long userId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (dto.getRole() == SubscriptionRole.OWNER) {
            boolean ownerExists = subscriptionUserRepository.existsBySubscriptionAndRole(subscription, SubscriptionRole.OWNER);
            if (ownerExists) {
                throw new OwnerAlreadyExistsException(subscriptionId);
            }
        }

        subscriptionService.checkAndExpire(subscription);
        subscriptionRepository.save(subscription);

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionNotActiveException(subscriptionId);
        }

        int approvedCount = subscriptionUserRepository.countBySubscriptionAndStatus(subscription, SubscriptionUserStatus.APPROVED);
        if (approvedCount >= subscription.getCapacity()) {
            throw new MaximumCapacityException(subscription);
        }

        if (subscriptionUserRepository.existsBySubscriptionAndUser(subscription, user)) {
            throw new UserAlreadyInSubscriptionException(subscription, user);
        }

        SubscriptionUser subscriptionUser =
                new SubscriptionUser(user, subscription, dto.getRole());

        subscriptionUser.setStatus(SubscriptionUserStatus.APPROVED);
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

    @Transactional
    public void changeOwner(Long subscriptionId, Long newOwnerId, Long currentOwnerId) {
        SubscriptionUser currentOwner = subscriptionUserRepository
                .findBySubscriptionIdAndUserIdAndRole(subscriptionId, currentOwnerId, SubscriptionRole.OWNER)
                .orElseThrow(() -> new OnlyOwnerCanUpdateException());

        SubscriptionUser novoOwner = subscriptionUserRepository
                .findBySubscriptionIdAndUserId(subscriptionId, newOwnerId)
                .orElseThrow(() -> new UserNotFoundException(newOwnerId));

        //todo exception
        if (currentOwnerId.equals(newOwnerId)) {
            throw new RuntimeException("Cannot transfer ownership to yourself");
        }

        currentOwner.setRole(SubscriptionRole.MEMBER);
        novoOwner.setRole(SubscriptionRole.OWNER);

        subscriptionUserRepository.save(currentOwner);
        subscriptionUserRepository.save(novoOwner);
    }


    //removes a user from a subscription actionUserId - who wants to remove | targetUserId - who will be removed
    @Transactional
    public void removeParticipant(Long subscriptionId, Long actionUserId, Long targetUserId ) {

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

        //todo exception
        if (targetSubsUser.getRole() == SubscriptionRole.OWNER
                && subscriptionUserRepository.countBySubscriptionIdAndRole(subscriptionId, SubscriptionRole.OWNER) <= 1) {
            throw new RuntimeException("Cannot remove last OWNER");
        }

        if (actionSubsUser.getRole() != SubscriptionRole.OWNER) {
            throw new OnlyOwnerCanRemoveMemberException(actionSubsUser.getRole());}

        if (targetSubsUser.getRole() == SubscriptionRole.OWNER) {
            throw new OwnerCannotBeRemovedException(targetSubsUser.getRole());
        }

        subscriptionUserRepository.delete(targetSubsUser);
    }


    private SubscriptionUserDTO toResponseDTO(SubscriptionUser subsUser) {
        SubscriptionUserDTO dto = new SubscriptionUserDTO();
        dto.setId(subsUser.getId());
        dto.setUserId(subsUser.getUser().getId());
        dto.setSubsId(subsUser.getSubscription().getId());
        dto.setStatus(subsUser.getStatus());
        dto.setRole(subsUser.getRole());
        dto.setDate(subsUser.getCreatedAt());
        dto.setActive(subsUser.isActive());
        dto.setSubscriptionStatus(subsUser.getSubscription().getStatus());
        dto.setCreatedById(subsUser.getCreatedBy()); // User que criou/requestou
        dto.setLastModifiedById(subsUser.getLastModifiedBy()); // Owner que aprovou
        return dto;
    }


}
