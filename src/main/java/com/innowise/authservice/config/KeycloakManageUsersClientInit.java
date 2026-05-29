package com.innowise.authservice.config;

import com.innowise.authservice.config.properties.KeycloakManageUsersClientProperties;
import lombok.AllArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class KeycloakManageUsersClientInit {
    private final KeycloakManageUsersClientProperties manageUsersClientProperties;


    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(manageUsersClientProperties.getServerUrl())
                .realm(manageUsersClientProperties.getRealm())
                .clientId(manageUsersClientProperties.getClient())
                .clientSecret(manageUsersClientProperties.getSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak) {
        return keycloak.realm(manageUsersClientProperties.getRealm());
    }

    @Bean
    public UsersResource usersResource(RealmResource realmResource) {
        return realmResource.users();
    }

}
