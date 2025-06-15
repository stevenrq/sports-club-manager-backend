package com.sportsclubmanager.backend.auth.security.filter;

import static com.sportsclubmanager.backend.auth.security.JwtConfig.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsclubmanager.backend.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Filtro de autenticación que maneja el proceso de login mediante JWT.
 * Extiende UsernamePasswordAuthenticationFilter para procesar las credenciales
 * del usuario y generar tokens JWT.
 */
public class JwtAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(
            JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    /**
     * Constructor que recibe el AuthenticationManager necesario para el proceso de
     * autenticación.
     *
     * @param authenticationManager El gestor de autenticación a utilizar
     */
    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Intenta autenticar al usuario con las credenciales proporcionadas en la
     * solicitud.
     * Lee el nombre de usuario y contraseña del cuerpo de la solicitud JSON.
     *
     * @param request  La solicitud HTTP que contiene las credenciales
     * @param response La respuesta HTTP
     * @return Un objeto Authentication que representa la autenticación exitosa
     * @throws AuthenticationException Si falla la autenticación
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        User user;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (IOException e) {
            log.error(
                    "No se pudieron analizar los datos del usuario de la solicitud. Asegúrese de que el formato JSON sea correcto. Error: {}",
                    e.getMessage());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * Maneja la autenticación exitosa generando un token JWT.
     * El token incluye el nombre de usuario y sus autoridades.
     *
     * @param request    La solicitud HTTP
     * @param response   La respuesta HTTP donde se enviará el token
     * @param chain      El filtro chain
     * @param authResult El resultado de la autenticación exitosa
     * @throws IOException      Si ocurre un error al escribir la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult
                .getPrincipal();

        String username = user.getUsername();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        Claims claims = Jwts.claims()
                .add(
                        "authorities",
                        new ObjectMapper().writeValueAsString(authorities))
                .add("username", username)
                .build();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

        Map<String, String> body = new HashMap<>();
        body.put("access_token", token);

        response.addHeader(AUTHORIZATION_HEADER, PREFIX_TOKEN + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Maneja la autenticación fallida enviando un mensaje de error al cliente.
     *
     * @param request  La solicitud HTTP
     * @param response La respuesta HTTP donde se enviará el mensaje de error
     * @param failed   La excepción que causó el fallo de autenticación
     * @throws IOException      Si ocurre un error al escribir la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Login fallido");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
