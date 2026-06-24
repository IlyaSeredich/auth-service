package com.innowise.authservice.mapper;

import com.innowise.authservice.dto.UserCreateDto;
import com.innowise.authservice.dto.UserServiceCreateDto;
import com.innowise.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "login", source = "username")
    User toUser(UUID id, String username, String password);

    UserServiceCreateDto toUserServiceCreateDto(UserCreateDto userCreateDto, UUID id);
}
