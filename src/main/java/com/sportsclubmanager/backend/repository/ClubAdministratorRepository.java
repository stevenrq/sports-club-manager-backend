package com.sportsclubmanager.backend.repository;

import com.sportsclubmanager.backend.model.ClubAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubAdministratorRepository extends JpaRepository<ClubAdministrator, Long> {
    Optional<ClubAdministrator> findByUsername(String name);
}
