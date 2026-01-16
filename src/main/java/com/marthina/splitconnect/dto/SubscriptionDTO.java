package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;
import com.marthina.splitconnect.model.enums.ServicesType;
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

}

