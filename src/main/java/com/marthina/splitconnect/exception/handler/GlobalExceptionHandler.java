package com.marthina.splitconnect.exception.handler;

import com.marthina.splitconnect.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Validation

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList()
                .toString();

        return buildError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleInvalidPassword(
            InvalidPasswordException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MaximumCapacityException.class)
    public ResponseEntity<ApiError> handleMaximumCapacity(
            MaximumCapacityException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    //Not found

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ApiError> handleSubscriptionNotFound(
            SubscriptionNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionUserNotFoundException.class)
    public ResponseEntity<ApiError> handleSubscriptionUserNotFound(
            SubscriptionUserNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ApiError> handleServiceNotFound(
            ServiceNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    //Conflicts

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyInUse(
            EmailAlreadyInUseException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyInSubscriptionException.class)
    public ResponseEntity<ApiError> handleUserAlreadyInSubscription(
            UserAlreadyInSubscriptionException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    //Helper
    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                status.value(),
                status.name(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}
