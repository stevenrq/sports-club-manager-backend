package com.sportsclubmanager.backend.user.exception;

public class RoleRetrievalException extends RuntimeException {

    /**
     * Construye una excepción de recuperación de roles con un mensaje específico.
     *
     * @param message el mensaje de error
     * @param cause   la causa de la excepción
     */
    public RoleRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
