package com.marthina.splitconnect.model;

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

    private BigDecimal amount;
    private Integer capacity;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String country;

    @ManyToOne
    @JoinColumn(name = "id_service")
    private Services service;
}
