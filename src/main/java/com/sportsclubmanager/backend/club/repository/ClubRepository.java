package com.sportsclubmanager.backend.club.repository;

import com.sportsclubmanager.backend.club.model.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);
}
