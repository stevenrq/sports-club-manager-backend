package com.sportsclubmanager.backend.event.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sportsclubmanager.backend.member.model.Player;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = { "players" })
@ToString(exclude = { "players" })
@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @JsonIgnoreProperties(value = { "events", "club" })
    @ManyToMany(mappedBy = "events")
    private Set<Player> players = new HashSet<>();
}
