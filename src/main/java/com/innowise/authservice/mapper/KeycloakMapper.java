package com.innowise.authservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.authservice.dto.KeycloakExceptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeycloakMapper {
    ObjectMapper objectMapper = new ObjectMapper();

    default KeycloakExceptionDto toDto(String errorMessage) {
        KeycloakExceptionDto keycloakExceptionDto;
        try {
            keycloakExceptionDto = objectMapper.readValue(errorMessage, KeycloakExceptionDto.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error creating keycloak message");
        }
        return keycloakExceptionDto;
    }

}
