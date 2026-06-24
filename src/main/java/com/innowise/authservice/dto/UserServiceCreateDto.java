package com.innowise.authservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserServiceCreateDto(
        UUID id,
        String name,
        String surname,
        LocalDate birthDate,
        String email
) {
}
