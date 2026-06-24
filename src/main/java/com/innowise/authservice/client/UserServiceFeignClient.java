package com.innowise.authservice.client;

import com.innowise.authservice.dto.UserResponseDto;
import com.innowise.authservice.dto.UserServiceCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        url = "${user-service.register.uri}"
)
public interface UserServiceFeignClient {
    @PostMapping
    UserResponseDto createUser(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserServiceCreateDto userServiceCreateDto);
}


