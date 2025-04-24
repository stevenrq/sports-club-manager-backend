package com.sportsclubmanager.backend.repository;

import com.sportsclubmanager.backend.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
