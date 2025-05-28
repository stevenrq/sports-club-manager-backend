package com.sportsclubmanager.backend.event.dto;

import com.sportsclubmanager.backend.event.model.EventVisibility;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
public class EventUpdateRequest {

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String location;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_visibility", nullable = false)
    private EventVisibility eventVisibility;

    @NotNull
    @Column(name = "maximum_participants", nullable = false)
    private Integer maximumParticipants;
}
