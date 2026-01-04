package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.repository.SubscriptionUserRepository;
import org.springframework.stereotype.Service;

//Gerenciar o vínculo entre usuários e assinaturas, garantindo regras como: não ultrapassar a capacidade
//não permitir usuários duplicados, definir papéis (OWNER, MEMBER), centralizar toda a lógica dessa relação
@Service
public class SubscriptionUserService {

    private final SubscriptionUserRepository subscriptionUserRepository;

    public SubscriptionUserService(SubscriptionUserRepository subscriptionUserRepository) {
        this.subscriptionUserRepository = subscriptionUserRepository;
    }

    //public SubscriptionUserDTO create(SubscriptionUserDTO dto) {}
    //Adicionar usuário a uma subscription
    //Remover usuário de uma subscription
    //Listar usuários de uma subscription

}
