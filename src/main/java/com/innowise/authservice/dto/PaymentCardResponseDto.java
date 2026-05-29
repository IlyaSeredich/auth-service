package com.innowise.authservice.dto;

import java.io.Serializable;
import java.util.UUID;

public record PaymentCardResponseDto(
        Long id,
        String number,
        String holder,
        String expirationDate,
        UUID userId,
        Boolean active
) implements Serializable {
}
