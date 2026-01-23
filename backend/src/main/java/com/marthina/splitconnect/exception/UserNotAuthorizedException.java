package com.marthina.splitconnect.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(Long requestedId, Long authenticatedId) {
        super(String.format("User %d is not authorized to modify user %d",
                authenticatedId, requestedId));
    }
}
