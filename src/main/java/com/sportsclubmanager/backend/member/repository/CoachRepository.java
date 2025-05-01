package com.sportsclubmanager.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportsclubmanager.backend.member.model.Coach;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByUsername(String name);
}
