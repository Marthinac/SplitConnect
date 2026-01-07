package com.marthina.splitconnect.exception;

import com.marthina.splitconnect.model.Subscription;

public class MaximumCapacityException extends RuntimeException {
    public MaximumCapacityException(Subscription subscription) {
        super("Maximum capacity of " + subscription.getCapacity() + " reached.");
    }
}
