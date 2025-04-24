package com.sportsclubmanager.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "coaches")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Coach extends User {
}
