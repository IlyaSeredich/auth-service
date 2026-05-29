package com.innowise.authservice.dto;

public record UserLoginDto(
        String username,
        String password
) {
}
