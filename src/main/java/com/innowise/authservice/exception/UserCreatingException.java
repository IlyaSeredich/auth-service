package com.innowise.authservice.exception;

public class UserCreatingException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "An unexpected error occurred while creating user";

    public UserCreatingException() {
        super(createErrorMessage());
    }

    public static String createErrorMessage() {
        return MESSAGE_TEMPLATE;
    }

}
