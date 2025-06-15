package com.sportsclubmanager.backend.user.dto;

import com.sportsclubmanager.backend.user.model.Role;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la información de un usuario que se devuelve en las respuestas de
 * la API. Puede utilizarse para distintos tipos de usuarios.
 */
@Data
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private Long nationalId;
    private String name;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private String username;
    private Set<Role> roles;

    public boolean isAdmin() {
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
}
