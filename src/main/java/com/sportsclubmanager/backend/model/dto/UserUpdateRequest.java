package com.sportsclubmanager.backend.model.dto;

import com.sportsclubmanager.backend.model.IUser;
import com.sportsclubmanager.backend.model.Role;
import com.sportsclubmanager.backend.util.RoleAuthorityUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserUpdateRequest implements IUser {

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

    @NotEmpty
    private Set<Role> roles;

    @Override
    public Set<String> getRolesAndAuthorities() {
        return RoleAuthorityUtils.getRolesAndAuthorities(this.roles);
    }
}
