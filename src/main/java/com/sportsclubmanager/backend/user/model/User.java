package com.sportsclubmanager.backend.user.model;

import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.validation.annotation.NoSpecialCharacters;
import com.sportsclubmanager.backend.user.validation.annotation.PasswordStrength;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 7, max = 10)
    @Column(name = "national_id", nullable = false, unique = true, length = 10)
    private Long nationalId;

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

    /**
     * Se utiliza una longitud de 60 porque BCrypt generará una cadena de longitud
     * 60
     */
    @NotBlank
    @PasswordStrength
    @Size(min = 6, max = 60)
    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Transient
    private boolean admin;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "affiliation_status")
    private AffiliationStatus affiliationStatus;

    @PrePersist
    private void prePersist() {
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.affiliationStatus = AffiliationStatus.ACTIVE;
    }

    public Set<String> getRolesAndAuthorities() {
        return RoleAuthorityUtils.getRolesAndAuthorities(this.roles);
    }
}
