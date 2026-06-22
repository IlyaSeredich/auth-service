package com.innowise.authservice.exception;

public class WrongPasswordException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "Wrong password";

    public WrongPasswordException() {
        super(createErrorMessage());
    }

    public static String createErrorMessage() {
        return MESSAGE_TEMPLATE;
    }

}
