package com.innowise.authservice.exception;

public class UsernameNotFoundException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "User with username %s not found";

    public UsernameNotFoundException(String username) {
        super(createErrorMessage(username));
    }

    public static String createErrorMessage(String username) {
        return String.format(MESSAGE_TEMPLATE, username);
    }

}