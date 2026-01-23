package com.marthina.splitconnect.exception;

public class InvalidSubscriptionDatesException extends RuntimeException {
    public InvalidSubscriptionDatesException() {
        super("Subscription dates are required (dateStart and dateEnd cannot be null)");
    }}
