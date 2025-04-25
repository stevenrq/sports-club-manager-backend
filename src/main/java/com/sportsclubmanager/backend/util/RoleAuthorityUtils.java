package com.sportsclubmanager.backend.util;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sportsclubmanager.backend.model.Authority;
import com.sportsclubmanager.backend.model.ClubAdministrator;
import com.sportsclubmanager.backend.model.Coach;
import com.sportsclubmanager.backend.model.Player;
import com.sportsclubmanager.backend.model.Role;
import com.sportsclubmanager.backend.model.User;
import com.sportsclubmanager.backend.model.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.repository.RoleRepository;

public class RoleAuthorityUtils {

    private static final String ROLE_CLUB_ADMIN = "ROLE_CLUB_ADMIN";
    private static final String ROLE_COACH = "ROLE_COACH";
    private static final String ROLE_PLAYER = "ROLE_PLAYER";
    private static final String ROLE_USER = "ROLE_USER";

    private RoleAuthorityUtils() {
    }

    public static Set<String> getRolesAndAuthorities(Set<Role> roles) {
        Objects.requireNonNull(roles, "The roles set must not be null");

        return Stream.concat(
                roles.stream().map(Role::getName),
                roles.stream()
                        .flatMap(role -> role.getAuthorities().stream())
                        .map(Authority::getName))
                .collect(Collectors.toSet());
    }

    /**
     * Obtiene los roles asociados a un usuario específico. Si el usuario no tiene
     * roles y autoridades asignados, se determina el rol basado en el tipo de
     * usuario.
     * 
     * @param user           El usuario para el cual se obtendrán los roles.
     * @param roleRepository Repositorio utilizado para buscar los roles por nombre.
     * @return Un conjunto de roles asociados al usuario.
     * @throws IllegalArgumentException Si el tipo de usuario no es reconocido.
     * @throws NoSuchElementException   Si no se encuentra un rol específico en el
     *                                  repositorio.
     * @throws RuntimeException         Si ocurre un error inesperado al recuperar
     *                                  los roles.
     */
    public static Set<Role> getRoles(User user, RoleRepository roleRepository) {
        Set<String> rolesAndAuthoritiesOfUser = user.getRolesAndAuthorities();

        try {
            if (rolesAndAuthoritiesOfUser.isEmpty()) {
                if (user instanceof ClubAdministrator) {
                    return Set.of(roleRepository.findByName(ROLE_CLUB_ADMIN).orElseThrow());
                } else if (user instanceof Coach) {
                    return Set.of(roleRepository.findByName(ROLE_COACH).orElseThrow());
                } else if (user instanceof Player) {
                    return Set.of(roleRepository.findByName(ROLE_PLAYER).orElseThrow());
                } else if (user instanceof User) {
                    return Set.of(roleRepository.findByName(ROLE_USER).orElseThrow());
                } else {
                    throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
                }
            } else {
                Set<Role> roles = new HashSet<>();
                for (String role : rolesAndAuthoritiesOfUser) {
                    Optional<Role> roleOptional = roleRepository.findByName(role);
                    roleOptional.ifPresent(roles::add);
                }
                return roles;
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Role not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving roles: " + e.getMessage(), e);
        }
    }

    public static Set<Role> getRolesFromUpdateRequest(UserUpdateRequest userUpdateRequest,
            RoleRepository roleRepository) {
        Set<String> rolesAndAuthoritiesOfUserUpdateRequest = userUpdateRequest.getRolesAndAuthorities();

        Set<Role> roles = new HashSet<>();
        for (String role : rolesAndAuthoritiesOfUserUpdateRequest) {
            Optional<Role> roleOptional = roleRepository.findByName(role);
            roleOptional.ifPresent(roles::add);
        }
        return roles;
    }
}