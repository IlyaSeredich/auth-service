package com.innowise.authservice.exception;

public class KeycloakUnavailableException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "An unexpected error occurred while communicating with Keycloak";

    public KeycloakUnavailableException() {
        super(createErrorMessage());
    }

    public static String createErrorMessage() {
        return MESSAGE_TEMPLATE;
    }

}
