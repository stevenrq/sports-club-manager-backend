package com.sportsclubmanager.backend.user.controller;

import com.sportsclubmanager.backend.shared.validation.ValidationService;
import com.sportsclubmanager.backend.user.dto.ApiResponse;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.mapper.UserMapper;
import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import com.sportsclubmanager.backend.user.model.User;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService<User> userService;
    private final ValidationService validationService;

    private final UserMapper userMapper;

    public UserController(
            @Qualifier("userServiceImpl") UserService<User> userService,
            ValidationService validationService,
            UserMapper userMapper) {
        this.userService = userService;
        this.validationService = validationService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody User user,
            BindingResult bindingResult) {
        ResponseEntity<ApiResponse<UserResponse>> validationResult = validationService.handleValidation(user,
                bindingResult);
        if (validationResult != null)
            return validationResult;

        User savedUser = userService.save(user);
        UserResponse response = userMapper.toUserResponse(savedUser);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userService
                .findById(id)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getByUsername(
            @PathVariable String username) {
        return userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
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
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
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
        boolean updated = userService.updateAffiliationStatus(
                id,
                affiliationStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
