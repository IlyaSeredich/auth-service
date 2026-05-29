package com.innowise.authservice.client;

import com.innowise.authservice.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "keycloak",
        url = "http://localhost:8080/realms/app-realm/protocol/openid-connect/token"
)
public interface KeycloakFeignClient {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto getTokens(@RequestBody Map<String, String> params);
}


