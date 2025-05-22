package com.sportsclubmanager.backend.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nationalId", target = "nationalId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "roles", target = "roles")
    UserResponse toUserResponse(User user);
}
