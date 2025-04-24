package com.sportsclubmanager.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club_administrators")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class ClubAdministrator extends User {
}
