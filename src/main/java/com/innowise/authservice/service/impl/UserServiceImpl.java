package com.innowise.authservice.service.impl;

import com.innowise.authservice.client.KeycloakFeignClient;
import com.innowise.authservice.config.properties.KeycloakAuthClientProperties;
import com.innowise.authservice.dto.KeycloakExceptionDto;
import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.exception.KeycloakCreateUserException;
import com.innowise.authservice.exception.StateNotMatchException;
import com.innowise.authservice.mapper.KeycloakMapper;
import com.innowise.authservice.mapper.UserMapper;
import com.innowise.authservice.service.UserService;
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
public class UserServiceImpl implements UserService {
    private final KeycloakAuthClientProperties authClientProperties;
    private final UserMapper userMapper;
    private final RealmResource realmResource;
    private final UsersResource usersResource;
    private final KeycloakFeignClient keycloakFeignClient;
    private final KeycloakMapper keycloakMapper;

    @Override
    public void createUser(UserCreateDto userCreateDto) {
        CredentialRepresentation credentialRepresentation =
                userMapper.toCredentialRepresentation(userCreateDto.password());
        UserRepresentation userRepresentation =
                userMapper.toUserRepresentation(userCreateDto, List.of(credentialRepresentation));
        Response response = usersResource.create(userRepresentation);

        validateResponse(response);

        String createdId = CreatedResponseUtil.getCreatedId(response);
        addDefaultRole(createdId);
    }

    @Override
    public String createAuthPath() {
        return new StringBuilder()
                .append(authClientProperties.getServerUrl())
                .append("/realms/")
                .append(authClientProperties.getRealm())
                .append("/protocol/openid-connect/auth?response_type=code&client_id=")
                .append(authClientProperties.getClient())
                .append("&redirect_uri=")
                .append(authClientProperties.getRedirectUri())
                .append("&state=")
                .append(authClientProperties.getState())
                .toString();
    }

    @Override
    public TokenResponseDto getTokens(String state, String authCode) {
        if(!state.equals(authClientProperties.getState())) {
            throw new StateNotMatchException();
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("grant_type", "authorization_code");
        paramsMap.put("code", authCode);
        paramsMap.put("redirect_uri", authClientProperties.getRedirectUri());
        paramsMap.put("client_id", authClientProperties.getClient());
        paramsMap.put("client_secret", authClientProperties.getSecret());

        return keycloakFeignClient.getTokens(paramsMap);
    }

    @Override
    public TokenResponseDto refreshToken(TokenRefreshDto tokenRefreshDto) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("grant_type", "refresh_token");
        paramsMap.put("client_id", authClientProperties.getClient());
        paramsMap.put("client_secret", authClientProperties.getSecret());
        paramsMap.put("refresh_token", tokenRefreshDto.token());

        return keycloakFeignClient.getTokens(paramsMap);
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
