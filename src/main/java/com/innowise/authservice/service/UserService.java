package com.innowise.authservice.service;

import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;

public interface UserService {
    void createUser(UserCreateDto userCreateDto);
    String createAuthPath();
    TokenResponseDto getTokens(String state, String authCode);
    TokenResponseDto refreshToken(TokenRefreshDto tokenRefreshDto);
}
