package com.marthina.splitconnect.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionDTO {
    private Long id;
    private Long serviceId;
    private BigDecimal amount;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer capacity;
    private String country;
}
