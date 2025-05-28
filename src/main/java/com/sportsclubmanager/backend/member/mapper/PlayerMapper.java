package com.sportsclubmanager.backend.member.mapper;

import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.member.model.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nationalId", target = "nationalId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "events", target = "events")
    PlayerResponse toPlayerResponse(Player player);
}
