package com.sportsclubmanager.backend.user.dto;

import java.util.Map;
import lombok.*;

/**
 * Clase genérica que representa la estructura de respuesta de la API.
 * Puede contener datos de cualquier tipo y/o errores de validación.
 *
 * @param <T> tipo de dato que contendrá la respuesta
 */
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {

    /**
     * Datos de la respuesta
     */
    private T data;

    /**
     * Mapa de errores de validación donde la clave es el campo y el valor es el
     * mensaje de error
     */
    private Map<String, String> errors;

    /**
     * Constructor para respuestas exitosas con datos
     *
     * @param data datos a incluir en la respuesta
     */
    public ApiResponse(T data) {
        this.data = data;
        this.errors = null;
    }

    /**
     * Constructor para respuestas con errores de validación
     *
     * @param errors mapa de errores donde la clave es el campo y el valor es el
     *               mensaje de error
     */
    public ApiResponse(Map<String, String> errors) {
        this.errors = errors;
        this.data = null;
    }
}
