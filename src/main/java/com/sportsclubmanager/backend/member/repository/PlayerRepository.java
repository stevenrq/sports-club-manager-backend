package com.sportsclubmanager.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportsclubmanager.backend.member.model.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String name);
}
