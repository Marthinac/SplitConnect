package com.marthina.splitconnect.exception;

public class OwnerAlreadyExistsException extends RuntimeException {
    public OwnerAlreadyExistsException(Long subscriptionId) {
        super("There's already an OWNER for this subscription");
    }
}
