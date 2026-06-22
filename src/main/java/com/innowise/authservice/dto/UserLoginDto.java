package com.innowise.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
        String username,
        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 50, message = "Password must be between 6 and 100 characters long")
        String password
) {
}
