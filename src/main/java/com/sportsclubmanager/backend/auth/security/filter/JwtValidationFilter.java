package com.sportsclubmanager.backend.auth.security.filter;

import static com.sportsclubmanager.backend.auth.security.JwtConfig.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsclubmanager.backend.auth.security.SimpleGrantedAuthorityJsonCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Filtro para validar tokens JWT en las solicitudes entrantes.
 * Extiende BasicAuthenticationFilter para procesar la autenticación basada en
 * token.
 */
public class JwtValidationFilter extends BasicAuthenticationFilter {

    /**
     * Constructor que recibe el AuthenticationManager necesario para la validación.
     *
     * @param authenticationManager El gestor de autenticación a utilizar
     */
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Valida el token JWT presente en el encabezado de la solicitud.
     * Si el token es válido, establece la autenticación en el SecurityContext.
     *
     * @param request  La solicitud HTTP que contiene el token JWT
     * @param response La respuesta HTTP
     * @param chain    El filtro chain para continuar el procesamiento
     * @throws IOException      Si ocurre un error al escribir la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            Object authorityClaims = claims.get("authorities");

            // Deserializa la cadena JSON de reclamaciones de autoridad en una colección de
            // objetos GrantedAuthority utilizando un Jackson ObjectMapper con una unión
            // personalizada
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(
                                    SimpleGrantedAuthority.class,
                                    SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(
                                    authorityClaims.toString().getBytes(),
                                    SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities);

            SecurityContextHolder.getContext()
                    .setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (Exception e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());

            response
                    .getWriter()
                    .write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
