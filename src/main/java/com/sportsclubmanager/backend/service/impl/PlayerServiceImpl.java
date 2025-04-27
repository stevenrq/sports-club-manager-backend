package com.sportsclubmanager.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportsclubmanager.backend.model.Player;
import com.sportsclubmanager.backend.model.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.repository.PlayerRepository;
import com.sportsclubmanager.backend.repository.RoleRepository;
import com.sportsclubmanager.backend.service.UserService;
import com.sportsclubmanager.backend.util.RoleAuthorityUtils;

@Service
public class PlayerServiceImpl implements UserService<Player> {

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public PlayerServiceImpl(PlayerRepository playerRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Player save(Player player) {
        player.setRoles(RoleAuthorityUtils.getRoles(player, roleRepository));
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Optional<Player> update(Long id, UserUpdateRequest userUpdateRequest) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            Player playerUpdated = playerOptional.orElseThrow();

            playerUpdated.setName(userUpdateRequest.getName());
            playerUpdated.setLastName(userUpdateRequest.getLastName());
            playerUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            playerUpdated.setEmail(userUpdateRequest.getEmail());
            playerUpdated.setUsername(userUpdateRequest.getUsername());
            playerUpdated.setRoles(RoleAuthorityUtils.getRolesFromUpdateRequest(userUpdateRequest, roleRepository));

            return Optional.of(playerRepository.save(playerUpdated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }
}
