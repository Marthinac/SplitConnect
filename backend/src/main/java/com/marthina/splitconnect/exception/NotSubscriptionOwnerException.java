package com.marthina.splitconnect.exception;

public class NotSubscriptionOwnerException extends RuntimeException {
    public NotSubscriptionOwnerException(Long id, Long ownerId) {
        super("You (id: " + id + ") are not the owner of this subscription. Owner id: " + ownerId);
    }
}
