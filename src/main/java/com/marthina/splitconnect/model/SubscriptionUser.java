package com.marthina.splitconnect.model;

import com.marthina.splitconnect.model.enums.SubscriptionRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Table(
        name = "subscription_user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "subs_id"})},
        indexes = {
                @Index(name = "idx_subscription_id", columnList = "subs_id"),
                @Index(name = "idx_subscription_role", columnList = "subs_id, role")})
@Getter
@Setter
@Entity
public class SubscriptionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subs_id", nullable = false)
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionRole role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected SubscriptionUser() {
    }

    public SubscriptionUser(User user, Subscription subscription, SubscriptionRole role) {
        this.user = user;
        this.subscription = subscription;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
