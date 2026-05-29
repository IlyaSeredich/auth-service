package com.innowise.authservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak.auth")
@Getter
@Setter
public class KeycloakAuthClientProperties {
    private String serverUrl;
    private String realm;
    private String client;
    private String secret;
}
