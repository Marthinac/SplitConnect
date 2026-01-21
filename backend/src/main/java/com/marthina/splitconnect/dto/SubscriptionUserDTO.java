package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.SubscriptionRole;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionUserDTO {
    private Long id;
    private Long userId;
    private Long subsId;

    private SubscriptionRole role;
    private LocalDateTime date;

    //true se o participante ainda está ativo na assinatura
    private Boolean active;

    // status da própria subscription
    private SubscriptionStatus subscriptionStatus;
}