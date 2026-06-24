package com.innowise.authservice.config;

import com.innowise.authservice.config.properties.KeycloakAuthClientProperties;
import com.innowise.authservice.config.properties.KeycloakManageUsersClientProperties;
import com.innowise.authservice.config.properties.KeycloakRootProperties;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class KeycloakConfig {
    private final KeycloakRootProperties rootProperties;
    private final KeycloakManageUsersClientProperties manageUsersClientProperties;
    private final KeycloakAuthClientProperties authClientProperties;

    private Keycloak keycloak;
    private RealmResource realmResource;

    private final String appRealmName;

    public KeycloakConfig(
            KeycloakRootProperties rootProperties,
            KeycloakManageUsersClientProperties manageUsersClientProperties,
            KeycloakAuthClientProperties authClientProperties
    ) {
        this.rootProperties = rootProperties;
        this.manageUsersClientProperties = manageUsersClientProperties;
        this.authClientProperties = authClientProperties;
        appRealmName = manageUsersClientProperties.getRealm();
    }

    @PostConstruct
    private void init() {
        initKeycloak();

        if (!isAppRealmExists()) {
            createKeycloakEnv();
        }
    }

    private void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(rootProperties.getServerUrl())
                .realm(rootProperties.getRealm())
                .clientId(rootProperties.getClient())
                .username(rootProperties.getUsername())
                .password(rootProperties.getPassword())
                .build();
    }

    private boolean isAppRealmExists() {
        return keycloak.realms().findAll().stream()
                .anyMatch(realmRepresentation ->
                        realmRepresentation.getRealm().equals(appRealmName));
    }

    private void createKeycloakEnv() {
        configureRealm();
        configureClients();
    }

    private void configureRealm() {
        createRealm();
        createRealmRoles();
    }

    private void configureClients() {
        createAuthClient();
        createManageUsersClient();
    }

    private void createRealm() {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(appRealmName);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRequiredActions(Collections.emptyList());
        keycloak.realms().create(realmRepresentation);
        realmResource = keycloak.realm(appRealmName);
    }

    private void createRealmRoles() {
        createRealmRole("user");
        createRealmRole("admin");
    }

    private void createRealmRole(String name) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(name);

        realmResource
                .roles()
                .create(roleRepresentation);
    }

    private void createAuthClient() {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(authClientProperties.getClient());
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setSecret(authClientProperties.getSecret());
        clientRepresentation.setDirectAccessGrantsEnabled(true);

        try(Response response = realmResource.clients().create(clientRepresentation)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new RuntimeException("Failed to create client " + authClientProperties.getClient());
            }
        }
    }

    private void createManageUsersClient() {
        ClientRepresentation manageRepresentation = new ClientRepresentation();
        manageRepresentation.setClientId(manageUsersClientProperties.getClient());
        manageRepresentation.setStandardFlowEnabled(true);
        manageRepresentation.setPublicClient(false);
        manageRepresentation.setServiceAccountsEnabled(true);
        manageRepresentation.setSecret(manageUsersClientProperties.getSecret());

        try(Response response = realmResource.clients().create(manageRepresentation)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new RuntimeException("Failed to create client " + manageUsersClientProperties.getClient());
            }

            configureManageUsersClient(response);
        }

    }

    private void configureManageUsersClient(Response response) {
        String manageClientUUID = CreatedResponseUtil.getCreatedId(response);
        UserRepresentation serviceAccountUser = getServiceAccountUser(manageClientUUID);
        addRolesToManageServiceUser(serviceAccountUser);
    }

    private UserRepresentation getServiceAccountUser(String manageClientUUID) {
        return realmResource
                .clients()
                .get(manageClientUUID)
                .getServiceAccountUser();
    }

    private void addRolesToManageServiceUser(UserRepresentation serviceAccountUser) {
        String realmManagementId = getRealmManagementId();
        RoleRepresentation manageRealmRole = getManageRealmRole(realmManagementId);
        RoleRepresentation manageUsersRole = getManageUsersRole(realmManagementId);

        realmResource.users()
                .get(serviceAccountUser.getId())
                .roles()
                .clientLevel(realmManagementId)
                .add(List.of(manageRealmRole, manageUsersRole));
    }

    private String getRealmManagementId() {
        return realmResource.clients()
                .findByClientId("realm-management")
                .getFirst().getId();
    }

    private RoleRepresentation getManageRealmRole(String realManagementId) {
        return realmResource
                .clients()
                .get(realManagementId)
                .roles()
                .get("manage-realm")
                .toRepresentation();
    }

    private RoleRepresentation getManageUsersRole(String realmManagementId) {
        return realmResource
                .clients()
                .get(realmManagementId)
                .roles()
                .get("manage-users")
                .toRepresentation();
    }


}
