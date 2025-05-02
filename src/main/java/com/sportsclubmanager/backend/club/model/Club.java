package com.sportsclubmanager.backend.club.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.model.Coach;
import com.sportsclubmanager.backend.member.model.Player;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "clubs")
@EqualsAndHashCode(exclude = {"clubAdministrator", "coaches", "players"})
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "club")
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer", "club"})
    private ClubAdministrator clubAdministrator;

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer", "club"})
    @OneToMany(mappedBy = "club")
    private Set<Coach> coaches = new HashSet<>();

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer", "club"})
    @OneToMany(mappedBy = "club")
    private Set<Player> players = new HashSet<>();
}
