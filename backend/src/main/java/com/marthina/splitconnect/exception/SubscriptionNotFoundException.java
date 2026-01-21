package com.marthina.splitconnect.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long id) {
        super("Could not find subscription " + id);
    }
}
