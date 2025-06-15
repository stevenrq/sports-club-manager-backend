package com.sportsclubmanager.backend.event.service;

import com.sportsclubmanager.backend.event.dto.EventUpdateRequest;
import com.sportsclubmanager.backend.event.model.Training;
import com.sportsclubmanager.backend.event.repository.TrainingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TrainingEventService implements EventService<Training> {

    private final TrainingRepository trainingRepository;

    public TrainingEventService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    @Override
    public Page<Training> findAll(Pageable pageable) {
        return trainingRepository.findAll(pageable);
    }

    @Override
    public Optional<Training> update(
            Long id,
            EventUpdateRequest eventUpdateRequest) {
        Optional<Training> trainingOptional = trainingRepository.findById(id);

        if (trainingOptional.isPresent()) {
            Training trainingUpdated = trainingOptional.orElseThrow();

            trainingUpdated.setName(eventUpdateRequest.getName());
            trainingUpdated.setDescription(eventUpdateRequest.getDescription());
            trainingUpdated.setLocation(eventUpdateRequest.getLocation());
            trainingUpdated.setStartDate(eventUpdateRequest.getStartDate());
            trainingUpdated.setEndDate(eventUpdateRequest.getEndDate());
            trainingUpdated.setEventVisibility(
                    eventUpdateRequest.getEventVisibility());
            trainingUpdated.setMaximumParticipants(
                    eventUpdateRequest.getMaximumParticipants());

            return Optional.of(trainingRepository.save(trainingUpdated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        trainingRepository.deleteById(id);
    }
}
