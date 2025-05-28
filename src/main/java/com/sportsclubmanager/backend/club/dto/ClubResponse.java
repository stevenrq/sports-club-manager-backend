package com.sportsclubmanager.backend.club.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubResponse {

    private Long id;

    private String name;

    private String address;

    private Long phoneNumber;

    private boolean enabled;

    private LocalDateTime creationDate;

    private String clubAdministratorName;

    private Set<String> coachNames;

    private Set<String> playerNames;
}
