package com.sportsclubmanager.backend.service.impl;

import com.sportsclubmanager.backend.model.IUser;
import com.sportsclubmanager.backend.model.Role;
import com.sportsclubmanager.backend.model.User;
import com.sportsclubmanager.backend.model.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.repository.RoleRepository;
import com.sportsclubmanager.backend.repository.UserRepository;
import com.sportsclubmanager.backend.service.UserService;
import com.sportsclubmanager.backend.util.RoleAuthorityUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service(value = "userService")
public class UserServiceImpl implements UserService<User> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    isAdmin(user);
                    return user;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    isAdmin(user);
                    return user;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll().stream()
                .map(user -> {
                    isAdmin(user);
                    return user;
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable)
                .map(user -> {
                    isAdmin(user);
                    return user;
                });
    }

    @Override
    @Transactional
    public Optional<User> update(Long id, UserUpdateRequest userUpdateRequest) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User userUpdated = userOptional.orElseThrow();

            userUpdated.setName(userUpdateRequest.getName());
            userUpdated.setLastName(userUpdateRequest.getLastName());
            userUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            userUpdated.setEmail(userUpdateRequest.getEmail());
            userUpdated.setUsername(userUpdateRequest.getUsername());
            userUpdated.setRoles(getRoles(userUpdateRequest));

            return Optional.of(userRepository.save(userUpdated));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Recupera el conjunto de roles asociados con el usuario dado.
     *
     * @param user el usuario para el cual se recuperarán los roles
     * @return un conjunto de roles asociados con el usuario
     */
    private Set<Role> getRoles(IUser user) {
        return RoleAuthorityUtils.getRoles(user, roleRepository);
    }

    /**
     * Verifica si el usuario es administrador y establece la propiedad admin como
     * true o false.
     * 
     * @param user el usuario a verificar si es administrador
     */
    private void isAdmin(User user) {
        boolean admin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ROLE_ADMIN));

        user.setAdmin(admin);
    }
}
