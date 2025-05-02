package com.sportsclubmanager.backend.club.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportsclubmanager.backend.club.model.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByName(String name);
}
