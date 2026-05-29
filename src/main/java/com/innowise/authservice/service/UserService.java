package com.innowise.authservice.service;

import com.innowise.authservice.dto.LoginResponseDto;
import com.innowise.authservice.dto.UserCreateDto;

public interface UserService {
    void createUser(UserCreateDto userCreateDto);
    String createAuthPath();
    LoginResponseDto getTokens(String state, String authCode);
}
