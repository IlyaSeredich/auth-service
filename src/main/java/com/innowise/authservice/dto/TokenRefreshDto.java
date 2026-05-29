package com.innowise.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshDto(
        @NotBlank(message = "Username must not be blank")
        String token
) {
}
