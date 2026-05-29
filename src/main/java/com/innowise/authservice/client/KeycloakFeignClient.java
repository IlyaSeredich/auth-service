package com.innowise.authservice.client;

import com.innowise.authservice.dto.LoginResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "keycloak",
        url = "${keycloak.auth.server-url}/realms/${keycloak.auth.realm}/protocol/openid-connect/token"
)
public interface KeycloakFeignClient {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponseDto getTokens(@RequestBody Map<String, String> params);
}
