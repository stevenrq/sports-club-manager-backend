package com.sportsclubmanager.backend.event.service;

import com.sportsclubmanager.backend.event.dto.EventUpdateRequest;
import com.sportsclubmanager.backend.event.model.Tournament;
import com.sportsclubmanager.backend.event.repository.TournamentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TournamentEventService implements EventService<Tournament> {

    private final TournamentRepository tournamentRepository;

    public TournamentEventService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Page<Tournament> findAll(Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }

    @Override
    public Optional<Tournament> update(
            Long id,
            EventUpdateRequest eventUpdateRequest) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(
                id);

        if (tournamentOptional.isPresent()) {
            Tournament tournamentUpdated = tournamentOptional.orElseThrow();

            tournamentUpdated.setName(eventUpdateRequest.getName());
            tournamentUpdated.setDescription(
                    eventUpdateRequest.getDescription());
            tournamentUpdated.setLocation(eventUpdateRequest.getLocation());
            tournamentUpdated.setStartDate(eventUpdateRequest.getStartDate());
            tournamentUpdated.setEndDate(eventUpdateRequest.getEndDate());
            tournamentUpdated.setEventVisibility(
                    eventUpdateRequest.getEventVisibility());
            tournamentUpdated.setMaximumParticipants(
                    eventUpdateRequest.getMaximumParticipants());

            return Optional.of(tournamentRepository.save(tournamentUpdated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        tournamentRepository.deleteById(id);
    }
}
