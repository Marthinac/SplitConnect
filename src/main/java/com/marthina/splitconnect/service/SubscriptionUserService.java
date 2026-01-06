package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.exception.SubscriptionNotFoundException;
import com.marthina.splitconnect.exception.UserNotFoundException;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.SubscriptionRepository;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        @Transactional
    public SubscriptionUserDTO addUser(Long subscriptionId, SubscriptionUserDTO dto) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        if (subscriptionUserRepository.existsBySubscriptionAndUser(subscription, user)) {
            throw new RuntimeException("Usuário já está na subscription");
        }

        long count = subscriptionUserRepository.countBySubscription(subscription);
        if (count >= subscription.getCapacity()) {
            throw new RuntimeException("Capacidade máxima atingida");
        }

        SubscriptionUser subscriptionUser =
                new SubscriptionUser(user, subscription, dto.getRole());

        SubscriptionUser saved = subscriptionUserRepository.save(subscriptionUser);

        return toResponseDTO(saved);
    }

    //todo Listar usuários de uma subscription
    public List<SubscriptionUserDTO> findAll() {
        List<SubscriptionUser> users = subscriptionUserRepository.findAll();
        List<SubscriptionUserDTO> response = new ArrayList<>();

        for (SubscriptionUser user : users) {
            response.add(toResponseDTO(user));
        }

        return response;
        //com steam - return userRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<SubscriptionUserDTO> findUsersBySubscription(Long subscriptionId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription não encontrada"));

        return subscriptionUserRepository.findBySubscription(subscription)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //todo Remover usuário de uma subscription
    //public void removeUser (Long userid, Long subsid){subscriptionUserRepository.deleteById(userid);}

    @Transactional
    public void removeUser(Long subscriptionId, Long userId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription não encontrada"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User não encontrado"));

        SubscriptionUser subscriptionUser = subscriptionUserRepository
                .findBySubscriptionAndUser(subscription, user)
                .orElseThrow(() -> new RuntimeException("Vínculo não encontrado"));

        subscriptionUserRepository.delete(subscriptionUser);
    }


    private SubscriptionUserDTO toResponseDTO(SubscriptionUser subsUser) {
        SubscriptionUserDTO dto = new SubscriptionUserDTO();
        dto.setId(subsUser.getId());
        dto.setUserId(subsUser.getUser().getId());
        //dto.setSubscriptionId(subsUser.getSubscription().getId());
        dto.setRole(subsUser.getRole());
        dto.setDate(subsUser.getCreatedAt());
        return dto;
    }

}
