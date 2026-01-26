package com.marthina.splitconnect.exception;

public class OnlyOwnerCanUpdateException extends RuntimeException {
    public OnlyOwnerCanUpdateException() {
    super("Only OWNER can update signature");
    }
}
