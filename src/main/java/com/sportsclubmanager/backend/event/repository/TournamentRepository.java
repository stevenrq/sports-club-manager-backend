package com.sportsclubmanager.backend.event.repository;

import com.sportsclubmanager.backend.event.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
