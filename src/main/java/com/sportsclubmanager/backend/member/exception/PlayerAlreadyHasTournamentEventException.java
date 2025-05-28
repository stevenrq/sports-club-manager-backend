package com.sportsclubmanager.backend.member.exception;

public class PlayerAlreadyHasTournamentEventException extends RuntimeException {

    public PlayerAlreadyHasTournamentEventException(String message) {
        super(message);
    }
}
