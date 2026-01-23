package com.marthina.splitconnect.exception;

public class InvalidServiceNameException extends RuntimeException {
    public InvalidServiceNameException() {
        super("Service name is required and cannot be empty");
    }}
