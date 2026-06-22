package com.innowise.authservice.exception;

public class KeycloakCreateUserException extends RuntimeException {
    public KeycloakCreateUserException(String message) {
        super(message);
    }
}
