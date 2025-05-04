package com.sportsclubmanager.backend.member.controller;

import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.mapper.UserMapper;
import com.sportsclubmanager.backend.user.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/club-administrators")
public class ClubAdministratorController {

    private final UserService<ClubAdministrator> clubAdministratorService;

    private final UserMapper userMapper;

    public ClubAdministratorController(
            @Qualifier("clubAdministratorService") UserService<ClubAdministrator> clubAdministratorService,
            UserMapper userMapper) {
        this.clubAdministratorService = clubAdministratorService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<ClubAdministrator> create(@RequestBody ClubAdministrator clubAdministrator) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clubAdministratorService.save(clubAdministrator));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return clubAdministratorService.findById(id)
                .map(clubAdministrator -> ResponseEntity.ok(userMapper.toUserResponse(clubAdministrator)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        return clubAdministratorService.findByUsername(username)
                .map(clubAdministrator -> ResponseEntity.ok(userMapper.toUserResponse(clubAdministrator)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(clubAdministratorService.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(@PathVariable Integer page) {
        return ResponseEntity.ok(clubAdministratorService.findAll(PageRequest.of(page, 5))
                .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        return clubAdministratorService.update(id, userUpdateRequest)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorService.findById(id);

        if (clubAdminOptional.isPresent()) {
            clubAdministratorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/change-affiliation-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateAffiliationStatus(@PathVariable Long id, @RequestBody AffiliationStatus affiliationStatus) {
        boolean updated = clubAdministratorService.updateAffiliationStatus(id, affiliationStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
