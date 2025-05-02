package com.sportsclubmanager.backend.club.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportsclubmanager.backend.club.dto.ClubUpdateRequest;
import com.sportsclubmanager.backend.club.model.Club;

public interface BaseClubService {

    Club save(Club club, Long clubAdminId);

    Optional<Club> findById(Long id);

    Optional<Club> findByName(String name);

    List<Club> findAll();

    Page<Club> findAll(Pageable pageable);

    Optional<Club> update(Long id, ClubUpdateRequest clubUpdateRequest);

    void deleteById(Long id);
}
