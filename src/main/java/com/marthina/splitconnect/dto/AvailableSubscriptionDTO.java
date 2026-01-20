package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AvailableSubscriptionDTO {
    // centraliza tudo que o frontend precisa para mostrar “Netflix - R$ 10/pessoa - 3/5 vagas - ACTIVE”.
    private Long id;
    private String serviceName;
    private String ownerName;
    private String country;
    private BigDecimal pricePerUser; // amount / capacity
    private Integer totalSlots;
    private Integer usedSlots;
    private Boolean hasVacancy;
    private SubscriptionStatus status;
}
