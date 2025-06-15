package com.sportsclubmanager.backend.shared.util;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.model.Coach;
import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.exception.RoleRetrievalException;
import com.sportsclubmanager.backend.user.model.Authority;
import com.sportsclubmanager.backend.user.model.Role;
import com.sportsclubmanager.backend.user.model.User;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Clase de utilidad para la gestión de roles y autoridades en la aplicación.
 * Proporciona métodos para obtener conjuntos de roles y autoridades a partir
 * de diferentes representaciones de usuarios y para extraer roles específicos.
 */
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
     *              roles y autoridades. No debe ser nulo.
     * @return Un conjunto de cadenas que representan los nombres de los roles y
     *         autoridades.
     * @throws NullPointerException Si el conjunto de roles proporcionado es nulo.
     */
    public static Set<String> getRolesAndAuthorities(Set<Role> roles) {
        Objects.requireNonNull(roles, "La colección de roles no debe ser nula");

        return Stream.concat(
                roles.stream().map(Role::getName),
                roles
                        .stream()
                        .flatMap(role -> role.getAuthorities().stream())
                        .map(Authority::getName))
                .collect(Collectors.toSet());
    }

    /**
     * Obtiene un conjunto de roles a partir de un objeto de usuario o una solicitud
     * de actualización de usuario.
     *
     * @param user           El objeto de usuario (instancia de {@link User}) o la
     *                       solicitud de actualización de usuario (instancia de
     *                       {@link UserUpdateRequest}). No debe ser nulo.
     * @param roleRepository El repositorio para acceder a la información de los
     *                       roles. No debe ser nulo.
     * @return Un conjunto de objetos {@link Role} asociados al usuario o a la
     *         solicitud de actualización.
     * @throws RoleRetrievalException Si ocurre un error al recuperar los roles.
     */
    public static Set<Role> getRoles(
            Object user,
            RoleRepository roleRepository) {
        Set<Role> roles = new HashSet<>();

        if (user instanceof User u) {
            roles = getRolesFromUser(u, roleRepository);
        } else if (user instanceof UserUpdateRequest ur) {
            roles = getRolesFromUserUpdateRequest(ur, roleRepository);
        }
        return roles;
    }

    /**
     * Obtiene un conjunto de roles a partir de un objeto {@link User}.
     *
     * @param user           El objeto de usuario del cual se extraerán los roles.
     *                       No debe ser nulo.
     * @param roleRepository El repositorio para acceder a la información de los
     *                       roles. No debe ser nulo.
     * @return Un conjunto de objetos {@link Role} asociados al usuario.
     * @throws RoleRetrievalException Si ocurre un error al recuperar los roles
     *                                debido a la no existencia de un rol, un
     *                                nombre de rol inválido o un error inesperado.
     */
    private static Set<Role> getRolesFromUser(
            User user,
            RoleRepository roleRepository) {
        Set<Role> roles = new HashSet<>();

        try {
            Set<String> rolesAndAuthoritiesOfUser = user.getRolesAndAuthorities();

            if (rolesAndAuthoritiesOfUser.isEmpty()) {
                roles.add(roleRepository.findByName(ROLE_USER).orElseThrow());

                if (user instanceof ClubAdministrator) {
                    roles.add(
                            roleRepository.findByName(ROLE_CLUB_ADMIN).orElseThrow());
                } else if (user instanceof Coach) {
                    roles.add(
                            roleRepository.findByName(ROLE_COACH).orElseThrow());
                } else if (user instanceof Player) {
                    roles.add(
                            roleRepository.findByName(ROLE_PLAYER).orElseThrow());
                }
            } else {
                for (String role : rolesAndAuthoritiesOfUser) {
                    Optional<Role> roleOptional = roleRepository.findByName(
                            role);
                    roleOptional.ifPresent(roles::add);
                }
            }
        } catch (NoSuchElementException e) {
            throw new RoleRetrievalException("Error al recuperar roles", e);
        } catch (IllegalArgumentException e) {
            throw new RoleRetrievalException("Nombre de rol inválido", e);
        } catch (Exception e) {
            throw new RoleRetrievalException(
                    "Se produjo un error inesperado",
                    e);
        }
        return roles;
    }

    /**
     * Obtiene un conjunto de roles a partir de un objeto {@link UserUpdateRequest}.
     *
     * @param userUpdateRequest La solicitud de actualización de usuario de la cual
     *                          se extraerán los roles. No debe ser nula.
     * @param roleRepository    El repositorio para acceder a la información de los
     *                          roles. No debe ser nulo.
     * @return Un conjunto de objetos {@link Role} especificados en la solicitud de
     *         actualización.
     * @throws RoleRetrievalException Si ocurre un error al recuperar los roles
     *                                debido a la no existencia de un rol o un
     *                                error inesperado.
     * @throws NullPointerException   Si el conjunto de roles en la solicitud de
     *                                actualización está vacío.
     */
    private static Set<Role> getRolesFromUserUpdateRequest(
            UserUpdateRequest userUpdateRequest,
            RoleRepository roleRepository) {
        Set<Role> roles = new HashSet<>();

        try {
            Set<String> rolesAndAuthoritiesOfUserUpdateRequest = userUpdateRequest.getRolesAndAuthorities();

            if (!rolesAndAuthoritiesOfUserUpdateRequest.isEmpty()) {
                for (String role : rolesAndAuthoritiesOfUserUpdateRequest) {
                    Optional<Role> roleOptional = roleRepository.findByName(
                            role);
                    roleOptional.ifPresent(roles::add);
                }
            } else {
                throw new NullPointerException(
                        "Los roles de usuario no deben ser nulos");
            }
        } catch (NoSuchElementException e) {
            throw new RoleRetrievalException("Error al recuperar roles", e);
        } catch (Exception e) {
            throw new RoleRetrievalException(
                    "Se produjo un error inesperado",
                    e);
        }
        return roles;
    }
}
