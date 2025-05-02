package com.sportsclubmanager.backend.member.model;

import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "players")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Player extends User {

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    private Club club;
}
