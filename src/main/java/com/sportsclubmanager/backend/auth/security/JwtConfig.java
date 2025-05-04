package com.sportsclubmanager.backend.auth.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtConfig {

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CONTENT_TYPE = "application/json";

    private JwtConfig() {
    }
}
