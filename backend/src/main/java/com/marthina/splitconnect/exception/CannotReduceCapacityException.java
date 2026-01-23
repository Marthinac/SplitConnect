package com.marthina.splitconnect.exception;

public class CannotReduceCapacityException extends RuntimeException {
    public CannotReduceCapacityException(Integer current, Integer requested) {
        super("Cannot reduce capacity from " + current + " to " + requested +
                ". There are already approved users.");
    }
}
