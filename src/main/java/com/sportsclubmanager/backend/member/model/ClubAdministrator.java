package com.sportsclubmanager.backend.member.model;

import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.user.model.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club_administrators")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class ClubAdministrator extends User {

    @OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    private Club club;
}
