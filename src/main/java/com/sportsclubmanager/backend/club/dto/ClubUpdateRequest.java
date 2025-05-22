package com.sportsclubmanager.backend.club.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
public class ClubUpdateRequest {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(name = "phone_number", unique = true)
    private Long phoneNumber;
}
