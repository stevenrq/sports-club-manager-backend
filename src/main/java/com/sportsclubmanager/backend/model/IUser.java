package com.sportsclubmanager.backend.model;

import java.util.Set;

/**
 * La interfaz {@code IUser} define el contrato para las entidades de usuario en
 * el sistema. Proporciona métodos para recuperar los roles de usuario, las
 * autoridades y el estado administrativo.
 * 
 * @deprecated
 */
public interface IUser {

    /**
     * Verifica si el usuario tiene privilegios administrativos.
     *
     * @return {@code true} si el usuario es administrador, {@code false} en caso
     *         contrario.
     */
    boolean isAdmin();

    /**
     * Recupera el conjunto de roles asignados al usuario.
     *
     * @return un {@code Set} de objetos {@link Role} que representan los roles del
     *         usuario.
     */
    Set<Role> getRoles();

    /**
     * Recupera el conjunto de todos los roles y autoridades asociados con el
     * usuario. Esto incluye tanto los roles asignados directamente al usuario como
     * las autoridades asociadas con esos roles.
     *
     * @return un {@code Set} de {@code String} que representa los nombres de roles
     *         y autoridades.
     */
    Set<String> getRolesAndAuthorities();
}