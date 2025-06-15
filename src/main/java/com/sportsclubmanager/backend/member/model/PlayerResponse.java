package com.sportsclubmanager.backend.member.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sportsclubmanager.backend.event.model.Event;
import com.sportsclubmanager.backend.user.model.Role;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerResponse {

    private Long id;
    private Long nationalId;
    private String name;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private String username;
    private Set<Role> roles;

    @JsonIgnoreProperties({ "players" })
    private Set<Event> events;

    public boolean isAdmin() {
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
}
