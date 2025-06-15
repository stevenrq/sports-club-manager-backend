package com.sportsclubmanager.backend.event.controller;

import com.sportsclubmanager.backend.event.dto.EventUpdateRequest;
import com.sportsclubmanager.backend.event.model.Training;
import com.sportsclubmanager.backend.event.service.EventService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training-events")
public class TrainingEventController {

    private final EventService<Training> trainingEventService;

    public TrainingEventController(
            @Qualifier("trainingEventService") EventService<Training> trainingEventService) {
        this.trainingEventService = trainingEventService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Training> create(@RequestBody Training training) {
        return ResponseEntity.ok(trainingEventService.save(training));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Training> getById(@PathVariable Long id) {
        return trainingEventService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Training>> getAll() {
        return ResponseEntity.ok(trainingEventService.findAll());
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<Training>> getAllPaginated(
            @PathVariable Integer page) {
        return ResponseEntity.ok(
                trainingEventService.findAll(PageRequest.of(page, 5)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Training> update(
            @PathVariable Long id,
            @RequestBody EventUpdateRequest trainingEventUpdateRequest) {
        return trainingEventService
                .update(id, trainingEventUpdateRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Training> trainingOptional = trainingEventService.findById(id);

        if (trainingOptional.isPresent()) {
            trainingEventService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
