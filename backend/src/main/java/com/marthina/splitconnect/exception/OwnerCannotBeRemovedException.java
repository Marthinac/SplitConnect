package com.marthina.splitconnect.exception;

import com.marthina.splitconnect.model.enums.SubscriptionRole;

public class OwnerCannotBeRemovedException extends RuntimeException {
    public OwnerCannotBeRemovedException(SubscriptionRole role) {
        super("OWNER can't be removed from the subscription.");
    }
}
