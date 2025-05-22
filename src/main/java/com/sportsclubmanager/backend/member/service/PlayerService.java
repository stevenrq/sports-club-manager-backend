package com.sportsclubmanager.backend.member.service;

import java.util.List;
import java.util.Optional;

import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.member.repository.PlayerRepository;
import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import com.sportsclubmanager.backend.user.service.UserService;

@Service
public class PlayerService implements UserService<Player> {

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playerRepository, RoleRepository roleRepository,
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
            playerUpdated.setRoles(RoleAuthorityUtils.getRoles(playerUpdated, roleRepository));

            return Optional.of(playerRepository.save(playerUpdated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public boolean updateAffiliationStatus(Long id, AffiliationStatus affiliationStatus) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.orElseThrow();
            player.setAffiliationStatus(affiliationStatus);
            playerRepository.save(player);
            return true;
        }
        return false;
    }
}
