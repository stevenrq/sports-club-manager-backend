package com.sportsclubmanager.backend.auth.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase abstracta que proporciona un creador personalizado para deserializar
 * objetos SimpleGrantedAuthority desde JSON.
 * Esta clase es necesaria para manejar la serialización/deserialización
 * correcta de los roles de autorización.
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    /**
     * Constructor protegido que se utiliza para crear instancias durante la
     * deserialización JSON.
     *
     * @param role El rol o autoridad que se asignará, especificado como una
     *             propiedad JSON "authority"
     */
    @JsonCreator
    protected SimpleGrantedAuthorityJsonCreator(
            @JsonProperty("authority") String role) {
    }
}
