package com.marthina.splitconnect.exception;

import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.model.User;

public class UserAlreadyInSubscriptionException extends RuntimeException {
    public UserAlreadyInSubscriptionException(Subscription subscription, User user) {
        super("User" + user.getId() + " already registered in the subscription " + subscription.getId());
    }
}
