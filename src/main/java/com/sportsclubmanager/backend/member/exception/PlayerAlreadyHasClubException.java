package com.sportsclubmanager.backend.member.exception;

public class PlayerAlreadyHasClubException extends RuntimeException {

    public PlayerAlreadyHasClubException(String message) {
        super(message);
    }
}
