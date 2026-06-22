package com.innowise.authservice.service.impl;

import com.innowise.authservice.dto.TokenRefreshDto;
import com.innowise.authservice.dto.TokenResponseDto;
import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.dto.UserLoginDto;
import com.innowise.authservice.entity.User;
import com.innowise.authservice.exception.UsernameAlreadyExistsException;
import com.innowise.authservice.exception.UsernameNotFoundException;
import com.innowise.authservice.exception.WrongPasswordException;
import com.innowise.authservice.mapper.UserMapper;
import com.innowise.authservice.repository.UserRepository;
import com.innowise.authservice.service.KeycloakService;
import com.innowise.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserCreateDto userCreateDto) {
        validateUsernameForCreating(userCreateDto.username());

        String hashedPassword = passwordEncoder.encode(userCreateDto.password());
        String id = keycloakService.createKeycloakUser(userCreateDto);

        User user = userMapper.toUser(UUID.fromString(id), userCreateDto.username(), hashedPassword);

        userRepository.save(user);
    }

    @Override
    public TokenResponseDto getTokens(UserLoginDto userLoginDto) {
        validateLoginConditions(userLoginDto.username(), userLoginDto.password());

        return keycloakService.getTokens(userLoginDto);
    }

    @Override
    public TokenResponseDto refreshToken(TokenRefreshDto tokenRefreshDto) {
        return keycloakService.refreshToken(tokenRefreshDto);
    }

    private void validateUsernameForCreating(String username) {
        if(userRepository.existsByLogin(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
    }

    private void validateLoginConditions(String username, String password) {
        User user = userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException(username));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException();
        }
    }
}
