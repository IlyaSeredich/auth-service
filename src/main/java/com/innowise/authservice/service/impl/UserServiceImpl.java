package com.innowise.authservice.service.impl;

import com.innowise.authservice.client.UserServiceFeignClient;
import com.innowise.authservice.dto.*;
import com.innowise.authservice.entity.User;
import com.innowise.authservice.exception.UserCreatingException;
import com.innowise.authservice.exception.UsernameAlreadyExistsException;
import com.innowise.authservice.exception.UsernameNotFoundException;
import com.innowise.authservice.exception.WrongPasswordException;
import com.innowise.authservice.mapper.UserMapper;
import com.innowise.authservice.repository.UserRepository;
import com.innowise.authservice.service.KeycloakService;
import com.innowise.authservice.service.UserService;
import jakarta.transaction.Transactional;
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
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    @Transactional
    public void createUser(UserCreateDto userCreateDto) {
        validateUsernameForCreating(userCreateDto.username());

        String hashedPassword = passwordEncoder.encode(userCreateDto.password());
        String id = keycloakService.createKeycloakUser(userCreateDto);

        User user = userMapper.toUser(UUID.fromString(id), userCreateDto.username(), hashedPassword);

        User savedUser;

        try {
            savedUser = userRepository.save(user);
        } catch (Exception ex) {
            keycloakService.deleteKeycloakUser(id);
            throw new UserCreatingException();
        }

        UserServiceCreateDto userServiceCreateDto =
                userMapper.toUserServiceCreateDto(userCreateDto, UUID.fromString(id));

        try {
            TokenResponseDto tokenResponseDto =
                    keycloakService.getTokens(
                            new UserLoginDto(userCreateDto.username(),
                                    userCreateDto.password())
                    );
            userServiceFeignClient.createUser("Bearer " + tokenResponseDto.accessToken(),
                    userServiceCreateDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            keycloakService.deleteKeycloakUser(id);
            userRepository.delete(savedUser);
            throw new UserCreatingException();
        }
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
