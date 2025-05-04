package com.sportsclubmanager.backend.auth.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * La clase abstracta {@code SimpleGrantedAuthorityJsonCreator} se utiliza para
 * personalizar la deserialización de objetos {@code SimpleGrantedAuthority} en
 * formato JSON. Esto es útil en el contexto de Spring Security, donde las
 * autoridades (roles y permisos) se representan como instancias de
 * {@code SimpleGrantedAuthority}.
 *
 * Esta clase utiliza las anotaciones de Jackson para mapear el campo
 * {@code authority} del JSON a un objeto {@code SimpleGrantedAuthority}.
 *
 * <p>
 * Ejemplo de JSON esperado:
 * 
 * <pre>
 * {
 *   "authority": "ROLE_USER"
 * }
 * </pre>
 *
 * <p>
 * Se utiliza como un mixin en Jackson para configurar la
 * deserialización de objetos {@code SimpleGrantedAuthority}.
 * 
 * <p>
 * En pocas palabras, esta clase permite que Jackson sepa cómo deserializar un
 * objeto {@code SimpleGrantedAuthority} desde un JSON para que pueda ser
 * utilizado en el contexto de la seguridad de la aplicación.
 *
 * @see com.fasterxml.jackson.annotation.JsonCreator
 * @see com.fasterxml.jackson.annotation.JsonProperty
 * @see org.springframework.security.core.authority.SimpleGrantedAuthority
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    protected SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
    }
}