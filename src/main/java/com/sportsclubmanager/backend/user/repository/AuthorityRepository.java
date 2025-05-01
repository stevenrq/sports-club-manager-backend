package com.sportsclubmanager.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportsclubmanager.backend.user.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
