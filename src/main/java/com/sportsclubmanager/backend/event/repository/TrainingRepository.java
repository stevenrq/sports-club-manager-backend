package com.sportsclubmanager.backend.event.repository;

import com.sportsclubmanager.backend.event.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
