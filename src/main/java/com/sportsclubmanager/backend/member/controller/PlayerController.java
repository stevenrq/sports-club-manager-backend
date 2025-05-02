package com.sportsclubmanager.backend.member.controller;

import java.util.List;
import java.util.Optional;

import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.mapper.UserMapper;
import com.sportsclubmanager.backend.user.service.BaseUserService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final BaseUserService<Player> playerService;

    private final UserMapper userMapper;

    public PlayerController(@Qualifier("playerService") BaseUserService<Player> playerService, UserMapper userMapper) {
        this.playerService = playerService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<Player> create(@RequestBody Player player) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.save(player));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return playerService.findById(id)
                .map(player -> ResponseEntity.ok(userMapper.toUserResponse(player)))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        return playerService.findByUsername(username)
                .map(player -> ResponseEntity.ok(userMapper.toUserResponse(player)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(playerService.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(@PathVariable Integer page) {
        return ResponseEntity.ok(playerService.findAll(PageRequest.of(page, 5))
                .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'COACH', 'ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        return playerService.update(id, userUpdateRequest)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Player> playerOptional = playerService.findById(id);

        if (playerOptional.isPresent()) {
            playerService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/change-affiliation-status/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> updateAffiliationStatus(@PathVariable Long id, @RequestBody AffiliationStatus affiliationStatus) {
        boolean updated = playerService.updateAffiliationStatus(id, affiliationStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
