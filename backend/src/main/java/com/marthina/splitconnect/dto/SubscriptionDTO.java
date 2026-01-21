package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;
import com.marthina.splitconnect.model.enums.ServicesType;
import com.marthina.splitconnect.model.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionDTO {

    private Long id;
    private Long serviceId;
    @NotBlank
    private String serviceName;
    private ServicesType serviceType;
    @NotNull
    private Country country;
    @NotNull
    private BigDecimal amount;
    private Currency currency;
    @NotNull
    private LocalDate dateStart;
    @NotNull
    private LocalDate dateEnd;
    @NotNull
    private Integer capacity;
    private SubscriptionStatus status;

    // campos úteis para "vaga disponível"
    private Integer usedSlots; // número atual de participantes
    private Boolean hasVacancy; // true se usedSlots < capacity && status == ACTIVE

}

