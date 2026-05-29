package com.innowise.authservice.service.impl;

import com.innowise.authservice.dto.UserCreateDto;
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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RealmResource realmResource;
    private final UsersResource usersResource;

    @Override
    public void createUser(UserCreateDto userCreateDto) {
        CredentialRepresentation credentialRepresentation = userMapper.toCredentialRepresentation(userCreateDto.password());
        UserRepresentation userRepresentation = userMapper.toUserRepresentation(userCreateDto, List.of(credentialRepresentation));
        Response response = usersResource.create(userRepresentation);

        String createdId = CreatedResponseUtil.getCreatedId(response);
        addDefaultRole(createdId);
    }

    private void addDefaultRole(String userId) {
        RoleRepresentation roleRepresentation = realmResource.roles().get("user").toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }
}
