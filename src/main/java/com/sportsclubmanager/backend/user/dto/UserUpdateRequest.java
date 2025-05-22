package com.sportsclubmanager.backend.user.dto;

import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.model.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * Representa la información necesaria para actualizar un usuario.
 * Puede utilizarse para distintos tipos de usuarios.
 */
@Getter
@Setter
@ToString
public class UserUpdateRequest {

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Transient
    private boolean admin;

    private Set<Role> roles;

    public Set<String> getRolesAndAuthorities() {
        return RoleAuthorityUtils.getRolesAndAuthorities(this.roles);
    }
}
