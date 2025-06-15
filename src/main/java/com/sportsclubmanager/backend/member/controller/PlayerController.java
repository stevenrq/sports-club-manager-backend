package com.sportsclubmanager.backend.member.controller;

import com.sportsclubmanager.backend.member.mapper.PlayerMapper;
import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.member.model.PlayerResponse;
import com.sportsclubmanager.backend.member.service.PlayerService;
import com.sportsclubmanager.backend.shared.validation.ValidationService;
import com.sportsclubmanager.backend.user.dto.ApiResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import com.sportsclubmanager.backend.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final UserService<Player> userService;
    private final PlayerService playerService;
    private final ValidationService validationService;

    private final PlayerMapper playerMapper;

    public PlayerController(
            @Qualifier("playerService") UserService<Player> userService,
            @Qualifier("playerService") PlayerService playerService,
            ValidationService validationService,
            PlayerMapper playerMapper) {
        this.userService = userService;
        this.playerService = playerService;
        this.validationService = validationService;
        this.playerMapper = playerMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlayerResponse>> create(
            @Valid @RequestBody Player player,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<PlayerResponse>> validationResult = validationService.handlePlayerValidation(player,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        Player savedPlayer = userService.save(player);
        PlayerResponse response = playerMapper.toPlayerResponse(savedPlayer);
        ApiResponse<PlayerResponse> apiResponse = new ApiResponse<>(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return userService
                .findById(id)
                .map(player -> ResponseEntity.ok(playerMapper.toPlayerResponse(player)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<PlayerResponse> getByUsername(
            @PathVariable String username) {
        return userService
                .findByUsername(username)
                .map(player -> ResponseEntity.ok(playerMapper.toPlayerResponse(player)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<PlayerResponse>> getAll() {
        return ResponseEntity.ok(
                userService
                        .findAll()
                        .stream()
                        .map(playerMapper::toPlayerResponse)
                        .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<PlayerResponse>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                userService
                        .findAll(PageRequest.of(page, 5))
                        .map(playerMapper::toPlayerResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'COACH', 'ADMIN')")
    public ResponseEntity<ApiResponse<PlayerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<PlayerResponse>> validationResult = validationService.handlePlayerValidation(
                userUpdateRequest,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        return userService
                .update(id, userUpdateRequest)
                .map(user -> {
                    PlayerResponse response = playerMapper.toPlayerResponse(user);
                    ApiResponse<PlayerResponse> apiResponse = new ApiResponse<>(
                            response);
                    return ResponseEntity.ok(apiResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Player> playerOptional = userService.findById(id);

        if (playerOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/affiliation-status")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> updateAffiliationStatus(
            @PathVariable Long id,
            @RequestBody AffiliationStatus affiliationStatus) {
        boolean updated = userService.updateAffiliationStatus(
                id,
                affiliationStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{playerId}/register-in-tournament-event/{tournamentEventId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<String> registerInEvent(
            @PathVariable Long playerId,
            @PathVariable Long tournamentEventId) {
        boolean registered = playerService.registerInTournamentEvent(
                playerId,
                tournamentEventId);

        if (registered) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest()
                .body(
                        "An error occurred while registering the player in the tournament event");
    }

    @PatchMapping("/{playerId}/register-in-training-event/{trainingEventId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<String> registerInTrainingEvent(
            @PathVariable Long playerId,
            @PathVariable Long trainingEventId) {
        boolean registered = playerService.registerInTrainingEvent(
                playerId,
                trainingEventId);

        if (registered) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest()
                .body(
                        "An error occurred while registering the player in the training event");
    }
}
