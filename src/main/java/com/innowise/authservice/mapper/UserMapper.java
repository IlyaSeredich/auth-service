package com.innowise.authservice.mapper;

import com.innowise.authservice.dto.UserCreateDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "credentials", source = "credentials")
    @Mapping(target = "enabled", constant = "true")
    UserRepresentation toUserRepresentation(UserCreateDto userCreateDto, List<CredentialRepresentation> credentials);

    @Mapping(target = "temporary", constant = "false")
    @Mapping(target = "value", source = "password")
    @Mapping(target = "type", expression = "java(CredentialRepresentation.PASSWORD)")
    CredentialRepresentation toCredentialRepresentation(String password);
}
