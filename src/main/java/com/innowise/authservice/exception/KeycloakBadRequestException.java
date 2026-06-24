package com.innowise.authservice.exception;

public class KeycloakBadRequestException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "Invalid token request sent to Keycloak";

    public KeycloakBadRequestException() {
        super(createErrorMessage());
    }

    public static String createErrorMessage() {
        return MESSAGE_TEMPLATE;
    }

}
