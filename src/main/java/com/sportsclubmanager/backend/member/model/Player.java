package com.sportsclubmanager.backend.member.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.event.model.Event;
import com.sportsclubmanager.backend.user.model.User;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true, exclude = { "events" })
@ToString(exclude = { "events" })
@Entity
@Table(name = "players")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Player extends User {

    @JsonIgnoreProperties(value = { "players" })
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    private Club club;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JoinTable(name = "players_events", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events = new HashSet<>();
}
