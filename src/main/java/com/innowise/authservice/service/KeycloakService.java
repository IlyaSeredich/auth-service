package com.innowise.authservice.service;

import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.dto.UserLoginDto;

public interface KeycloakService {
    String createKeycloakUser(UserCreateDto userCreateDto);
    void deleteKeycloakUser(String userId);
    TokenResponseDto getTokens(UserLoginDto userLoginDto);
    TokenResponseDto refreshToken(TokenRefreshDto tokenRefreshDto);
}
