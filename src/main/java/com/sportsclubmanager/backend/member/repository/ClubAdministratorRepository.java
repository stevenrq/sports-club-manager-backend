package com.sportsclubmanager.backend.member.repository;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubAdministratorRepository
    extends JpaRepository<ClubAdministrator, Long> {
    Optional<ClubAdministrator> findByUsername(String name);
}
