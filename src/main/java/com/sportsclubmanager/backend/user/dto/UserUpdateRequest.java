package com.sportsclubmanager.backend.user.dto;

import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.model.Role;
import com.sportsclubmanager.backend.user.validation.annotation.NoSpecialCharacters;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa la información necesaria para actualizar un usuario.
 * Puede utilizarse para distintos tipos de usuarios.
 */
@Getter
@Setter
@ToString
public class UserUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @NotNull
    @Column(name = "phone_number", unique = true, nullable = false, length = 10)
    private Long phoneNumber;

    @NotBlank
    @Email
    @Size(min = 16, max = 40)
    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @NotBlank
    @NoSpecialCharacters
    @Size(min = 6, max = 20)
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Transient
    private boolean admin;

    private Set<Role> roles;

    public Set<String> getRolesAndAuthorities() {
        return RoleAuthorityUtils.getRolesAndAuthorities(this.roles);
    }
}
