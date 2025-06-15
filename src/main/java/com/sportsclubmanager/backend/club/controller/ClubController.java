package com.sportsclubmanager.backend.club.controller;

import com.sportsclubmanager.backend.club.dto.ClubResponse;
import com.sportsclubmanager.backend.club.dto.ClubUpdateRequest;
import com.sportsclubmanager.backend.club.mapper.ClubMapper;
import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.club.service.ClubService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    private final ClubMapper clubMapper;

    public ClubController(
            @Qualifier("clubServiceImpl") ClubService clubService,
            ClubMapper clubMapper) {
        this.clubService = clubService;
        this.clubMapper = clubMapper;
    }

    @PostMapping("/club-administrator/{clubAdminId}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Club> create(
            @RequestBody Club club,
            @PathVariable Long clubAdminId) {
        return ResponseEntity.ok(clubService.save(club, clubAdminId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<ClubResponse> getById(@PathVariable Long id) {
        return clubService
                .findById(id)
                .map(club -> ResponseEntity.ok(clubMapper.toClubResponse(club)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<ClubResponse> getByName(@PathVariable String name) {
        return clubService
                .findByName(name)
                .map(club -> ResponseEntity.ok(clubMapper.toClubResponse(club)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ClubResponse>> getAll() {
        return ResponseEntity.ok(
                clubService
                        .findAll()
                        .stream()
                        .map(clubMapper::toClubResponse)
                        .toList());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<ClubResponse>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                clubService
                        .findAll(PageRequest.of(page, 5))
                        .map(clubMapper::toClubResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<ClubResponse> update(
            @PathVariable Long id,
            @RequestBody ClubUpdateRequest clubUpdateRequest) {
        return clubService
                .update(id, clubUpdateRequest)
                .map(club -> ResponseEntity.ok(clubMapper.toClubResponse(club)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Club> clubOptional = clubService.findById(id);

        if (clubOptional.isPresent()) {
            clubService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
