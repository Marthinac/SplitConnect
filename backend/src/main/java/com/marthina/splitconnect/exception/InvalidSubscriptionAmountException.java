package com.marthina.splitconnect.exception;

import java.math.BigDecimal;

public class InvalidSubscriptionAmountException extends RuntimeException {
    public InvalidSubscriptionAmountException(BigDecimal amount) {
        super("Subscription amount must be greater than 0, got: " + amount);
    }}
