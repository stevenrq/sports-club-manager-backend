package com.sportsclubmanager.backend.club.mapper;

import com.sportsclubmanager.backend.club.dto.ClubResponse;
import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.member.model.Coach;
import com.sportsclubmanager.backend.member.model.Player;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "clubAdministrator.name", target = "clubAdministratorName")
    @Mapping(source = "coaches", target = "coachNames", qualifiedByName = "mapCoachNames")
    @Mapping(source = "players", target = "playerNames", qualifiedByName = "mapPlayerNames")
    ClubResponse toClubResponse(Club club);

    /**
     * Mapea un conjunto de entrenadores a sus nombres.
     *
     * @param coaches el conjunto de entrenadores a mapear
     * @return conjunto de nombres de entrenadores
     */
    @Named("mapCoachNames")
    default Set<String> mapCoachNames(Set<Coach> coaches) {
        if (coaches == null || coaches.isEmpty()) {
            return Collections.emptySet();
        }
        return coaches
                .stream()
                .map(Coach::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Mapea un conjunto de jugadores a sus nombres.
     *
     * @param players el conjunto de jugadores a mapear
     * @return conjunto de nombres de jugadores
     */
    @Named("mapPlayerNames")
    default Set<String> mapPlayerNames(Set<Player> players) {
        if (players == null || players.isEmpty()) {
            return Collections.emptySet();
        }
        return players
                .stream()
                .map(Player::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
