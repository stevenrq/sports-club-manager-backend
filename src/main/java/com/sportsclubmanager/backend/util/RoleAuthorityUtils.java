package com.sportsclubmanager.backend.util;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sportsclubmanager.backend.model.Authority;
import com.sportsclubmanager.backend.model.IUser;
import com.sportsclubmanager.backend.model.Role;
import com.sportsclubmanager.backend.repository.RoleRepository;

public class RoleAuthorityUtils {

    private static final String ROLE_USER = "ROLE_USER";

    private RoleAuthorityUtils() {
    }

    /**
     * Recupera un conjunto que contiene los nombres de todos los roles y
     * autoridades asociados con los roles dados. Esto incluye los nombres de los
     * roles asignados directamente, así como los nombres de las autoridades
     * asociadas con esos roles.
     *
     * @param roles un conjunto de objetos Role. No debe ser nulo.
     * @return un conjunto de cadenas que representan los nombres de roles y
     *         autoridades.
     * @throws NullPointerException si el parámetro roles es nulo.
     */
    public static Set<String> getRolesAndAuthorities(Set<Role> roles) {
        Objects.requireNonNull(roles, "The roles set must not be null");

        return Stream.concat(
                roles.stream()
                        .map(Role::getName),
                roles.stream()
                        .flatMap(role -> role.getAuthorities().stream())
                        .map(Authority::getName))
                .collect(Collectors.toSet());
    }

    /**
     * Recupera los roles asociados con un usuario dado. Si el usuario no tiene
     * roles, se le asigna el rol predeterminado 'ROLE_USER'.
     *
     * @param user           El usuario cuyos roles se van a recuperar.
     * @param roleRepository El repositorio utilizado para obtener roles de la base
     *                       de datos.
     * @return Un conjunto de roles asociados con el usuario.
     * @throws NoSuchElementException Si un rol especificado por el usuario no se
     *                                encuentra en el repositorio.
     * @throws RuntimeException       Si ocurre un error inesperado al recuperar los
     *                                roles.
     */
    public static Set<Role> getRoles(IUser user, RoleRepository roleRepository) {
        Set<String> rolesAndAuthoritiesOfUser = user.getRolesAndAuthorities();

        try {
            if (rolesAndAuthoritiesOfUser.isEmpty()) {
                return Set.of(roleRepository.findByName(ROLE_USER).orElseThrow());
            } else {
                Set<Role> roles = new HashSet<>();
                for (String role : rolesAndAuthoritiesOfUser) {
                    Optional<Role> optionalRole = roleRepository.findByName(role);
                    optionalRole.ifPresent(roles::add);
                }
                return roles;
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Role not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving roles: " + e.getMessage(), e);
        }
    }
}