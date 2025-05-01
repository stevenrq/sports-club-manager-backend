package com.sportsclubmanager.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;

import java.util.Optional;

public interface ClubAdministratorRepository extends JpaRepository<ClubAdministrator, Long> {
    Optional<ClubAdministrator> findByUsername(String name);
}
