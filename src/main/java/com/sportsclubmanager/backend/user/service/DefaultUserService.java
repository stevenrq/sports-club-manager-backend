package com.sportsclubmanager.backend.user.service;

import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.User;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import com.sportsclubmanager.backend.user.repository.UserRepository;
import com.sportsclubmanager.backend.util.RoleAuthorityUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultUserService implements BaseUserService<User> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setRoles(RoleAuthorityUtils.getRoles(user, roleRepository));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
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
            userUpdated.setRoles(RoleAuthorityUtils.getRolesFromUpdateRequest(userUpdateRequest, roleRepository));

            return Optional.of(userRepository.save(userUpdated));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
