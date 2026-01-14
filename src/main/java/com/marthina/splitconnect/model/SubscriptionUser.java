package com.marthina.splitconnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subscription_user")
public class SubscriptionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subs_id")
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private SubscriptionRole role;
    private LocalDate createdAt;

    protected SubscriptionUser() {
    }

    public SubscriptionUser(User user, Subscription subscription, SubscriptionRole role) {
        this.user = user;
        this.subscription = subscription;
        this.role = role;
        this.createdAt = LocalDate.now();
    }

}
