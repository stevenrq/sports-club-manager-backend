package com.sportsclubmanager.backend.user.repository;

import com.sportsclubmanager.backend.user.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
