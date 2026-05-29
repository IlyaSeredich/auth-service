package com.innowise.authservice.client;

import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8081/api/users"
)
public interface UserServiceFeignClient {
    @PostMapping
    UserResponseDto createUser(
            @RequestBody UserCreateDto userCreateDto,
            @RequestHeader("Authorization") String authorization);
}


