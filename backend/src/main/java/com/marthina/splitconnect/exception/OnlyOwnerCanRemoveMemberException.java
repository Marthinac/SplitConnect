package com.marthina.splitconnect.exception;

import com.marthina.splitconnect.model.enums.SubscriptionRole;

public class OnlyOwnerCanRemoveMemberException extends RuntimeException {
    public OnlyOwnerCanRemoveMemberException(SubscriptionRole role) {
        super("Only OWNER can remove members, your role is: " + role);
    }
}
