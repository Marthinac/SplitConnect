package com.marthina.splitconnect.exception;

import java.time.LocalDate;

public class InvalidSubscriptionDateRangeException extends RuntimeException {
    public InvalidSubscriptionDateRangeException(LocalDate start, LocalDate end) {
        super("Subscription end date must be after start date. Start: " + start + ", End: " + end);
    }}
