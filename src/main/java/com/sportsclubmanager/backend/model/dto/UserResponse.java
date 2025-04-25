package com.sportsclubmanager.backend.model.dto;

import java.util.Set;

import com.sportsclubmanager.backend.model.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private Set<Role> roles;

    public boolean isAdmin() {
        return this.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
}
