package com.innowise.authservice.controller;

import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
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
    public ResponseEntity<TokenResponseDto> getToken(
            @RequestParam String state,
            @RequestParam String code
    ) {
        TokenResponseDto tokenResponseDto = userService.getTokens(state, code);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody TokenRefreshDto tokenRefreshDto) {
        TokenResponseDto tokenResponseDto = userService.refreshToken(tokenRefreshDto);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
