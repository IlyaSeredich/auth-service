package com.innowise.authservice.controller;

import com.innowise.authservice.dto.LoginResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @GetMapping("/login")
    public ResponseEntity<Void> redirect() {
        String path = userService.createAuthPath();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(path))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<LoginResponseDto> getToken(
            @RequestParam String state,
            @RequestParam String code
    ) {
        LoginResponseDto loginResponseDto = userService.getTokens(state, code);
        return ResponseEntity.ok(loginResponseDto);
    }
}
