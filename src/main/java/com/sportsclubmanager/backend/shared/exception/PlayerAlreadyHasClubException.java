package com.sportsclubmanager.backend.shared.exception;

public class PlayerAlreadyHasClubException extends RuntimeException {
    public PlayerAlreadyHasClubException(String message) {
        super(message);
    } 
}
