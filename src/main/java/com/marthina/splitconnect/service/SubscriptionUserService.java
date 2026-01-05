package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.model.SubscriptionUser;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Gerenciar o vínculo entre usuários e assinaturas, garantindo regras como: não ultrapassar a capacidade
//não permitir usuários duplicados, definir papéis (OWNER, MEMBER), centralizar toda a lógica dessa relação
@Service
public class SubscriptionUserService {

    private final SubscriptionUserRepository subscriptionUserRepository;

    public SubscriptionUserService(SubscriptionUserRepository subscriptionUserRepository) {
        this.subscriptionUserRepository = subscriptionUserRepository;
    }

    //todo public SubscriptionUserDTO create(SubscriptionUserDTO dto) { }

    //todo Remover usuário de uma subscription
    public void removeUser (Long userid, Long subsid){
        subscriptionUserRepository.deleteById(userid);
    }

    //todo Adicionar usuário a uma subscription


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

    private SubscriptionUserDTO toResponseDTO(SubscriptionUser subsUser) {
        SubscriptionUserDTO dto = new SubscriptionUserDTO();
        dto.setId(subsUser.getId());
        dto.setRole(subsUser.getRole());
        dto.setDate(subsUser.getDate());

        return dto;
    }
}
