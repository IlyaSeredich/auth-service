package com.innowise.authservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak.root")
@Getter
@Setter
public class KeycloakRootProperties {
    private String serverUrl;
    private String realm;
    private String client;
    private String username;
    private String password;
    private String secret;
}
