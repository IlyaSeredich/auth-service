package com.innowise.authservice.exception;

public class KeycloakTokenException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "Invalid or expired refresh token";

    public KeycloakTokenException() {
        super(createErrorMessage());
    }

    public static String createErrorMessage() {
        return MESSAGE_TEMPLATE;
    }

}
