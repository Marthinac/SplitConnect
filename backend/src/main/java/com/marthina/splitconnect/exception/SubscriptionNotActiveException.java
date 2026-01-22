package com.marthina.splitconnect.exception;

public class SubscriptionNotActiveException extends RuntimeException {
    public SubscriptionNotActiveException(Long subscriptionId) {
        super("The subscription " + subscriptionId + " is not active.");
    }
}
