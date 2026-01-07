package com.marthina.splitconnect.exception;

public class SubscriptionUserNotFoundException extends RuntimeException {
    public SubscriptionUserNotFoundException(Long subscriptionId, Long userId) {
        super("There is not a connection between user " + userId + " and subscription " + subscriptionId);
    }
}
