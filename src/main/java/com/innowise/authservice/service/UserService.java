package com.innowise.authservice.service;

import com.innowise.authservice.dto.UserCreateDto;

public interface UserService {
    void createUser(UserCreateDto userCreateDto);
}
