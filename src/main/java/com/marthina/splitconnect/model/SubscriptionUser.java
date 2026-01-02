package com.marthina.splitconnect.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscription_user")
public class SubscriptionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_subs")
    private Subscription subscription;

    private String role;
    private LocalDate date;

}
