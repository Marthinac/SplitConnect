package com.marthina.splitconnect.model;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Country country;
    private BigDecimal amount;

    @Transient
    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Integer capacity;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    @ManyToOne
    @JoinColumn(name = "id_service")
    private Services service;
}
