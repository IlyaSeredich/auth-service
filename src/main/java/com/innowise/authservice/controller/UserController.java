package com.innowise.authservice.controller;

import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.dto.UserLoginDto;
import com.innowise.authservice.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(
            @RequestBody @Valid UserLoginDto userLoginDto
    ) {
        TokenResponseDto tokenResponseDto = userService.getTokens(userLoginDto);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@Valid @RequestBody TokenRefreshDto tokenRefreshDto) {
        TokenResponseDto tokenResponseDto = userService.refreshToken(tokenRefreshDto);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken() {
        return ResponseEntity.ok().build();
    }
}
