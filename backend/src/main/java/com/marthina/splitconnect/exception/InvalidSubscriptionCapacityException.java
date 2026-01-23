package com.marthina.splitconnect.exception;

public class InvalidSubscriptionCapacityException extends RuntimeException {
    public InvalidSubscriptionCapacityException(Integer capacity) {
        super("Subscription capacity must be greater than 0, got: " + capacity);
    }
}
