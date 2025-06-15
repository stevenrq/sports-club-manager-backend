package com.sportsclubmanager.backend.auth.security;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

/**
 * Clase de configuración para el manejo de JSON Web Tokens (JWT).
 * Contiene constantes utilizadas en la autenticación y autorización mediante
 * JWT.
 */
public class JwtConfig {

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Se genera usando el algoritmo HS256.
     */
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    /**
     * Prefijo estándar que se agrega al token JWT en los encabezados de
     * autorización.
     */
    public static final String PREFIX_TOKEN = "Bearer ";

    /**
     * Nombre del encabezado HTTP utilizado para la autorización.
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Tipo de contenido utilizado en las respuestas HTTP.
     */
    public static final String CONTENT_TYPE = "application/json";

    private JwtConfig() {
    }
}
