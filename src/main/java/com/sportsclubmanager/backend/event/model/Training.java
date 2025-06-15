package com.sportsclubmanager.backend.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "trainings")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Training extends Event {
}
