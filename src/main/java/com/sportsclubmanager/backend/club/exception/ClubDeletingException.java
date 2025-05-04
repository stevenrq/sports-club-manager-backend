package com.sportsclubmanager.backend.club.exception;

public class ClubDeletingException extends RuntimeException {

    /**
     * Excepción personalizada que se lanza al intentar eliminar un club.
     *
     * @param message Mensaje descriptivo del error.
     * @param cause   La causa original de la excepción.
     */
    public ClubDeletingException(String message, Throwable cause) {
        super(message, cause);
    }
}
