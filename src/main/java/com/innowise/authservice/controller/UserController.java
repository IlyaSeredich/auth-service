package com.innowise.authservice.controller;

import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
