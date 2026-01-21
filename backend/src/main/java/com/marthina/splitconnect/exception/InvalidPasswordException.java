package com.marthina.splitconnect.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("invalid password");
    }
}
