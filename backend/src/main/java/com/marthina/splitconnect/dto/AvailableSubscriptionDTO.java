package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
public class AvailableSubscriptionDTO {
    // centraliza tudo que o frontend precisa para mostrar “Netflix - R$ 10/pessoa - 3/5 vagas - ACTIVE”.
    private Long id;
    private String serviceName;
    private String ownerName;
    private Country country;
    private Currency currency;
    private Integer totalSlots;
    private Integer usedSlots;
    private Boolean hasVacancy;
    private SubscriptionStatus status;
    private String formattedPricePerUser;
    private String formattedAmount;

}
