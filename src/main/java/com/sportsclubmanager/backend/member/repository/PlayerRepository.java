package com.sportsclubmanager.backend.member.repository;

import com.sportsclubmanager.backend.member.model.Player;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String name);
}
