package com.sportsclubmanager.backend.member.exception;

public class ClubAlreadyHasPlayerException extends RuntimeException {

    public ClubAlreadyHasPlayerException(String message) {
        super(message);
    }
}
