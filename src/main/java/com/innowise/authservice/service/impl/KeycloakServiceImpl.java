package com.innowise.authservice.service.impl;

import com.innowise.authservice.client.KeycloakFeignClient;
import com.innowise.authservice.config.properties.KeycloakAuthClientProperties;
import com.innowise.authservice.dto.*;
import com.innowise.authservice.exception.KeycloakBadRequestException;
import com.innowise.authservice.exception.KeycloakCreateUserException;
import com.innowise.authservice.exception.KeycloakTokenException;
import com.innowise.authservice.exception.KeycloakUnavailableException;
import com.innowise.authservice.mapper.KeycloakMapper;
import com.innowise.authservice.service.KeycloakService;
import feign.FeignException;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final KeycloakAuthClientProperties authClientProperties;
    private final RealmResource realmResource;
    private final UsersResource usersResource;
    private final KeycloakFeignClient keycloakFeignClient;
    private final KeycloakMapper keycloakMapper;

    @Override
    public String createKeycloakUser(UserCreateDto userCreateDto) {
        CredentialRepresentation credentialRepresentation =
                keycloakMapper.toCredentialRepresentation(userCreateDto.password());
        UserRepresentation userRepresentation =
                keycloakMapper.toUserRepresentation(userCreateDto, List.of(credentialRepresentation));
        Response response = usersResource.create(userRepresentation);

        validateResponse(response);

        String createdId = CreatedResponseUtil.getCreatedId(response);
        addDefaultRole(createdId);

        return createdId;
    }

    @Override
    public TokenResponseDto getTokens(UserLoginDto userLoginDto) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("grant_type", "password");
        paramsMap.put("username", userLoginDto.username());
        paramsMap.put("password", userLoginDto.password());
        paramsMap.put("client_id", authClientProperties.getClient());
        paramsMap.put("client_secret", authClientProperties.getSecret());

        try {
            return keycloakFeignClient.getTokens(paramsMap);
        } catch (
                FeignException.BadRequest |
                FeignException.Unauthorized |
                FeignException.Forbidden ex) {
            throw new KeycloakBadRequestException();
        } catch (Exception ex) {
            throw new KeycloakUnavailableException();
        }
    }

    @Override
    public TokenResponseDto refreshToken(TokenRefreshDto tokenRefreshDto) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("grant_type", "refresh_token");
        paramsMap.put("client_id", authClientProperties.getClient());
        paramsMap.put("client_secret", authClientProperties.getSecret());
        paramsMap.put("refresh_token", tokenRefreshDto.token());

        try {
            return keycloakFeignClient.getTokens(paramsMap);
        } catch (FeignException.BadRequest |
                 FeignException.Unauthorized |
                 FeignException.Forbidden ex) {
            throw new KeycloakTokenException();
        } catch (Exception ex) {
            throw new KeycloakUnavailableException();
        }
    }

    @Override
    public void deleteKeycloakUser(String userId) {
        Response response = usersResource.delete(userId);
        validateResponse(response);
    }

    private void addDefaultRole(String userId) {
        RoleRepresentation roleRepresentation = realmResource.roles().get("user").toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private void validateResponse(Response response) {
        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            String errorMessage = response.readEntity(String.class);
            KeycloakExceptionDto keycloakExceptionDto = keycloakMapper.toDto(errorMessage);
            throw new KeycloakCreateUserException(keycloakExceptionDto.errorMessage());
        }
    }
}
