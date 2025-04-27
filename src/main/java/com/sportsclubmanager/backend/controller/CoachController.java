package com.sportsclubmanager.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportsclubmanager.backend.mapper.UserMapper;
import com.sportsclubmanager.backend.model.Coach;
import com.sportsclubmanager.backend.model.dto.UserResponse;
import com.sportsclubmanager.backend.model.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.service.UserService;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final UserService<Coach> coachService;

    private final UserMapper userMapper;

    public CoachController(@Qualifier("coachServiceImpl") UserService<Coach> coachService, UserMapper userMapper) {
        this.coachService = coachService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<Coach> create(@RequestBody Coach coach) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.save(coach));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return coachService.findById(id)
                .map(coach -> ResponseEntity.ok(userMapper.toUserResponse(coach)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        return coachService.findByUsername(username)
                .map(coach -> ResponseEntity.ok(userMapper.toUserResponse(coach)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(coachService.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(@PathVariable Integer page) {
        return ResponseEntity.ok(coachService.findAll(PageRequest.of(page, 5))
                .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'COACH', 'ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        return coachService.update(id, userUpdateRequest)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
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
}
