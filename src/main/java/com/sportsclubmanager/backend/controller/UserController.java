package com.sportsclubmanager.backend.controller;

import com.sportsclubmanager.backend.mapper.UserMapper;
import com.sportsclubmanager.backend.model.User;
import com.sportsclubmanager.backend.model.dto.UserResponse;
import com.sportsclubmanager.backend.model.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService<User> userService;

    private final UserMapper userMapper;

    public UserController(@Qualifier("userService") UserService<User> userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList());
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<Page<UserResponse>> getAllPaginated(@PathVariable Integer page) {
        return ResponseEntity.ok(userService.findAll(PageRequest.of(page, 5))
                .map(userMapper::toUserResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        return userService.update(id, userUpdateRequest)
                .map(user -> ResponseEntity.ok(userMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
