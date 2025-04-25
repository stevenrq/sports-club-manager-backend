package com.sportsclubmanager.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sportsclubmanager.backend.model.User;
import com.sportsclubmanager.backend.model.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "roles", target = "roles")
    UserResponse toUserResponse(User user);
}
