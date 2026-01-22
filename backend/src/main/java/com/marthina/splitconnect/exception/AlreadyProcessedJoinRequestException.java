package com.marthina.splitconnect.exception;

public class AlreadyProcessedJoinRequestException extends RuntimeException {
    public AlreadyProcessedJoinRequestException(Long subscriptionUserId) {
        super("Join request " + subscriptionUserId + " not found or not authorized for owner.");
    }
}
