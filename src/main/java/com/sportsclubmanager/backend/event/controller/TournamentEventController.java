package com.sportsclubmanager.backend.event.controller;

import com.sportsclubmanager.backend.event.dto.EventUpdateRequest;
import com.sportsclubmanager.backend.event.model.Tournament;
import com.sportsclubmanager.backend.event.service.EventService;
import com.sportsclubmanager.backend.shared.validation.ValidationService;
import com.sportsclubmanager.backend.user.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournament-events")
public class TournamentEventController {

    private final EventService<Tournament> tournamentEventService;
    private final ValidationService validationService;

    public TournamentEventController(
            @Qualifier("tournamentEventService") EventService<Tournament> tournamentEventService,
            ValidationService validationService) {
        this.tournamentEventService = tournamentEventService;
        this.validationService = validationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> create(
            @Valid @RequestBody Tournament tournament,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationService.validate(bindingResult);
        }

        Tournament savedTournament = tournamentEventService.save(tournament);
        ApiResponse<Tournament> apiResponse = new ApiResponse<>(
                savedTournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Tournament> getById(@PathVariable Long id) {
        return tournamentEventService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Tournament>> getAll() {
        return ResponseEntity.ok(tournamentEventService.findAll());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<Tournament>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                tournamentEventService.findAll(PageRequest.of(page, 5)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> update(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest tournamentEventUpdateRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationService.validate(bindingResult);
        }

        return tournamentEventService
                .update(id, tournamentEventUpdateRequest)
                .map(tournamentUpdated -> {
                    ApiResponse<Tournament> apiResponse = new ApiResponse<>(
                            tournamentUpdated);
                    return ResponseEntity.ok(apiResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Tournament> tournamentOptional = tournamentEventService.findById(id);

        if (tournamentOptional.isPresent()) {
            tournamentEventService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
