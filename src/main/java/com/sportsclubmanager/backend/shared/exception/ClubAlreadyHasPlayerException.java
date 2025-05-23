package com.sportsclubmanager.backend.shared.exception;

public class ClubAlreadyHasPlayerException extends RuntimeException {
    public ClubAlreadyHasPlayerException(String message) {
        super(message);
    }
}
