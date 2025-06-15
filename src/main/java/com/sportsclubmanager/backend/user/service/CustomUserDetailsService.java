package com.sportsclubmanager.backend.user.service;

import com.sportsclubmanager.backend.user.model.User;
import com.sportsclubmanager.backend.user.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio personalizado que implementa UserDetailsService de Spring Security
 * para
 * cargar los detalles del usuario durante la autenticación.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga los detalles del usuario para la autenticación de Spring Security.
     *
     * @param username nombre de usuario a buscar
     * @return objeto UserDetails con la información del usuario para autenticación
     * @throws UsernameNotFoundException si el usuario no es encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(
                    "Usuario no encontrado con nombre de usuario: " + username);
        }

        User user = userOptional.orElseThrow();

        // La colección 'authorities' almacena tanto roles como autoridades
        Set<GrantedAuthority> authorities = user
                .getRolesAndAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorities);
    }
}
