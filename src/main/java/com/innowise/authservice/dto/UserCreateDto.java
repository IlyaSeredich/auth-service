package com.innowise.authservice.dto;

public record UserCreateDto(
        String username,
        String password
) {
}
