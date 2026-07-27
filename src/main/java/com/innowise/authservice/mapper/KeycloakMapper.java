package com.innowise.authservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.authservice.dto.KeycloakExceptionDto;
import com.innowise.authservice.dto.UserCreateDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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

    @Mapping(target = "credentials", source = "credentials")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "email", source = "userCreateDto.email")
    @Mapping(target = "emailVerified", constant = "true")
    UserRepresentation toUserRepresentation(UserCreateDto userCreateDto, List<CredentialRepresentation> credentials);

    @Mapping(target = "temporary", constant = "false")
    @Mapping(target = "value", source = "password")
    @Mapping(target = "type", expression = "java(CredentialRepresentation.PASSWORD)")
    CredentialRepresentation toCredentialRepresentation(String password);

}
