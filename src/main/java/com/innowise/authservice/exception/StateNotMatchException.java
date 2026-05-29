package com.innowise.authservice.exception;

public class StateNotMatchException extends RuntimeException {
    private static final String MESSAGE = "States not match";

    public StateNotMatchException() {
        super(createMessage());
    }

    public static String createMessage() {
        return MESSAGE;
    }
}
