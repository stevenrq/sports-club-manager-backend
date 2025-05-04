package com.sportsclubmanager.backend.shared.util;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.model.Coach;
import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.exception.RoleRetrievalException;
import com.sportsclubmanager.backend.user.model.Authority;
import com.sportsclubmanager.backend.user.model.Role;
import com.sportsclubmanager.backend.user.model.User;
import com.sportsclubmanager.backend.user.repository.RoleRepository;

public class RoleAuthorityUtils {

    private static final String ROLE_CLUB_ADMIN = "ROLE_CLUB_ADMIN";
    private static final String ROLE_COACH = "ROLE_COACH";
    private static final String ROLE_PLAYER = "ROLE_PLAYER";
    private static final String ROLE_USER = "ROLE_USER";

    private RoleAuthorityUtils() {
    }

    /**
     * Obtiene un conjunto de nombres de roles y autoridades a partir de un conjunto
     * de roles.
     * 
     * @param roles El conjunto de roles del cual se extraerán los nombres de los
     *              roles y autoridades.
     * @return Un conjunto de cadenas que representan los nombres de los roles y
     *         autoridades.
     */
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
     * Obtiene un conjunto de roles a partir de un usuario y un repositorio de
     * roles. Por defecto, se asigna el ROL_USER a todos los usuarios; si es un
     * tipo de usuario específico, se asigna el rol correspondiente.
     * 
     * @param user           El usuario del cual se extraerán los roles y
     *                       autoridades.
     * @param roleRepository El repositorio de roles utilizado para buscar los
     *                       roles por su nombre.
     * @return Un conjunto de roles asociados al usuario.
     * 
     * @throws RoleRetrievalException Si ocurre un error al recuperar los roles del
     *                                usuario.
     */
    public static Set<Role> getRoles(User user, RoleRepository roleRepository) {
        Set<String> rolesAndAuthoritiesOfUser = user.getRolesAndAuthorities();
        Set<Role> roles = new HashSet<>();

        try {
            if (rolesAndAuthoritiesOfUser.isEmpty()) {

                roles.add(roleRepository.findByName(ROLE_USER).orElseThrow());

                if (user instanceof ClubAdministrator) {
                    roles.add(roleRepository.findByName(ROLE_CLUB_ADMIN).orElseThrow());
                } else if (user instanceof Coach) {
                    roles.add(roleRepository.findByName(ROLE_COACH).orElseThrow());
                } else if (user instanceof Player) {
                    roles.add(roleRepository.findByName(ROLE_PLAYER).orElseThrow());
                }
            } else {
                for (String role : rolesAndAuthoritiesOfUser) {
                    Optional<Role> roleOptional = roleRepository.findByName(role);
                    roleOptional.ifPresent(roles::add);
                }
                return roles;
            }
        } catch (NoSuchElementException e) {
            throw new RoleRetrievalException("Error retrieving roles", e);
        } catch (IllegalArgumentException e) {
            throw new RoleRetrievalException("Invalid role name", e);
        } catch (Exception e) {
            throw new RoleRetrievalException("An unexpected error occurred", e);
        }
        return roles;
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