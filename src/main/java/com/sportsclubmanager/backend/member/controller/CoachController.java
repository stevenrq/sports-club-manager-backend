package com.sportsclubmanager.backend.member.controller;

import com.sportsclubmanager.backend.member.model.Coach;
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
@RequestMapping("/api/coaches")
public class CoachController {

    private final UserService<Coach> coachService;
    private final ValidationService validationService;

    private final UserMapper userMapper;

    public CoachController(
            @Qualifier("coachService") UserService<Coach> coachService,
            ValidationService validationService,
            UserMapper userMapper) {
        this.coachService = coachService;
        this.validationService = validationService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody Coach coach,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<UserResponse>> validationResult = validationService.handleValidation(coach,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        Coach savedCoach = coachService.save(coach);
        UserResponse response = userMapper.toUserResponse(savedCoach);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return coachService
                .findById(id)
                .map(coach -> ResponseEntity.ok(userMapper.toUserResponse(coach)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(
            @PathVariable String username) {
        return coachService
                .findByUsername(username)
                .map(coach -> ResponseEntity.ok(userMapper.toUserResponse(coach)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
                coachService
                        .findAll()
                        .stream()
                        .map(userMapper::toUserResponse)
                        .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                coachService
                        .findAll(PageRequest.of(page, 5))
                        .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'COACH', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<UserResponse>> validationResult = validationService.handleValidation(
                userUpdateRequest,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        return coachService
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
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Coach> coachOptional = coachService.findById(id);

        if (coachOptional.isPresent()) {
            coachService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/affiliation-status")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> updateAffiliationStatus(
            @PathVariable Long id,
            @RequestBody AffiliationStatus affiliationStatus) {
        boolean updated = coachService.updateAffiliationStatus(
                id,
                affiliationStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
