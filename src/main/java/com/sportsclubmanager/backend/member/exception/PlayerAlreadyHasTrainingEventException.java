package com.sportsclubmanager.backend.member.exception;

public class PlayerAlreadyHasTrainingEventException extends RuntimeException {

    public PlayerAlreadyHasTrainingEventException(String message) {
        super(message);
    }
}
