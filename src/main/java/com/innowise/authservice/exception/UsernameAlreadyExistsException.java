package com.innowise.authservice.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "User with username %s already exists";

    public UsernameAlreadyExistsException(String username) {
        super(createErrorMessage(username));
    }

    public static String createErrorMessage(String username) {
        return String.format(MESSAGE_TEMPLATE, username);
    }

}