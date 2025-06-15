package com.sportsclubmanager.backend.member.controller;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.service.ClubAdministratorService;
import com.sportsclubmanager.backend.shared.validation.ValidationService;
import com.sportsclubmanager.backend.user.dto.ApiResponse;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.mapper.UserMapper;
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
@RequestMapping("/api/club-administrators")
public class ClubAdministratorController {

    private final UserService<ClubAdministrator> userService;
    private final ClubAdministratorService clubAdministratorService;
    private final ValidationService validationService;

    private final UserMapper userMapper;

    public ClubAdministratorController(
            @Qualifier("clubAdministratorService") UserService<ClubAdministrator> userService,
            @Qualifier("clubAdministratorService") ClubAdministratorService clubAdministratorService,
            ValidationService validationService,
            UserMapper userMapper) {
        this.userService = userService;
        this.clubAdministratorService = clubAdministratorService;
        this.validationService = validationService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody ClubAdministrator clubAdministrator,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<UserResponse>> validationResult = validationService.handleValidation(
                clubAdministrator,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        ClubAdministrator savedClubAdmin = userService.save(clubAdministrator);
        UserResponse response = userMapper.toUserResponse(savedClubAdmin);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userService
                .findById(id)
                .map(clubAdministrator -> ResponseEntity.ok(userMapper.toUserResponse(clubAdministrator)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(
            @PathVariable String username) {
        return userService
                .findByUsername(username)
                .map(clubAdministrator -> ResponseEntity.ok(userMapper.toUserResponse(clubAdministrator)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
                userService
                        .findAll()
                        .stream()
                        .map(userMapper::toUserResponse)
                        .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                userService
                        .findAll(PageRequest.of(page, 5))
                        .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<UserResponse>> validationResult = validationService.handleValidation(
                userUpdateRequest,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        return userService
                .update(id, userUpdateRequest)
                .map(user -> {
                    UserResponse response = userMapper.toUserResponse(user);
                    ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
                            response);
                    return ResponseEntity.ok(apiResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ClubAdministrator> clubAdminOptional = userService.findById(
                id);

        if (clubAdminOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/affiliation-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateAffiliationStatus(
            @PathVariable Long id,
            @RequestBody AffiliationStatus affiliationStatus) {
        boolean affiliationStatusUpdated = userService.updateAffiliationStatus(
                id,
                affiliationStatus);
        if (affiliationStatusUpdated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/clubs/{clubId}/players/{playerId}")
    public ResponseEntity<Void> linkPlayerToClub(
            @PathVariable Long clubId,
            @PathVariable Long playerId) {
        clubAdministratorService.linkPlayerToClub(clubId, playerId);
        return ResponseEntity.ok().build();
    }
}
