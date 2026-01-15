package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;
import com.marthina.splitconnect.model.enums.ServicesType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionDTO {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private ServicesType serviceType;
    private Country country;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer capacity;

}

