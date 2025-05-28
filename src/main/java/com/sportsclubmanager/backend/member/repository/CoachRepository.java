package com.sportsclubmanager.backend.member.repository;

import com.sportsclubmanager.backend.member.model.Coach;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByUsername(String name);
}
