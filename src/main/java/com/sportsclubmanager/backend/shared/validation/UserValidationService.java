package com.sportsclubmanager.backend.shared.validation;

import com.sportsclubmanager.backend.user.dto.ApiResponse;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserValidationService {

    /**
     * Maneja la validación de un objeto y su BindingResult.
     *
     * @param target        objeto a validar
     * @param bindingResult resultado de la validación de las anotaciones
     * @param <T>           tipo del objeto a validar
     * @return ResponseEntity con errores de validación o null si es válido
     */
    public <T> ResponseEntity<ApiResponse<UserResponse>> handleValidation(T target, BindingResult bindingResult) {
        if (!(target instanceof User || target instanceof UserUpdateRequest)) {
            throw new IllegalArgumentException("Invalid target type");
        }

        Map<String, String> errors = validate(target);

        if (!errors.isEmpty()) {
            return validationError(errors);
        }

        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        return null;
    }

    /**
     * Crea una respuesta de error de validación con los errores proporcionados.
     *
     * @param <T>    tipo genérico para la respuesta
     * @param errors mapa de errores donde la clave es el campo y el valor es el mensaje de error
     * @return ResponseEntity con estado HTTP 400 (Bad Request) y los errores en el cuerpo
     */
    public <T> ResponseEntity<ApiResponse<T>> validationError(Map<String, String> errors) {
        ApiResponse<T> apiResponse = new ApiResponse<>(errors);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * Procesa los errores de validación del BindingResult y los convierte en una respuesta de error.
     *
     * @param <T>           tipo genérico para la respuesta
     * @param bindingResult resultado de la validación que contiene los errores
     * @return ResponseEntity con estado HTTP 400 (Bad Request) y los errores formateados en el cuerpo
     */
    public <T> ResponseEntity<ApiResponse<T>> validate(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors()
                .forEach(error -> errors.put(error.getField(),
                        error.getField() + " field: " + error.getDefaultMessage()));
        return validationError(errors);
    }

    /**
     * Verifica si un número tiene una longitud válida dentro del rango especificado.
     *
     * @param number    número a validar
     * @param minLength longitud mínima permitida
     * @param maxLength longitud máxima permitida
     * @return true si el número tiene una longitud válida, false en caso contrario
     */
    public boolean isValidLength(Number number, int minLength, int maxLength) {
        if (number == null) {
            return true;
        }
        int length = String.valueOf(number).length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Valida los datos de un usuario.
     *
     * @param user usuario a validar, puede ser de tipo User o UserUpdateRequest
     * @return mapa con los mensajes de validación si hay errores, o mapa vacío si es válido
     * @throws IllegalArgumentException si el tipo de usuario no es válido
     */
    public Map<String, String> validate(Object user) {
        Map<String, String> validationsMessage = new HashMap<>();
        Long nationalId = 0L;
        Long phoneNumber;

        switch (user) {
            case User u -> {
                nationalId = u.getNationalId();
                phoneNumber = u.getPhoneNumber();
            }
            case UserUpdateRequest ur -> phoneNumber = ur.getPhoneNumber();
            default -> throw new IllegalArgumentException("Invalid user type");
        }

        if (!isValidLength(nationalId, 7, 10) && user instanceof User) {
            validationsMessage.put("nationalId", "National id must be between 7 and 10 digits");
        } else if (!isValidLength(phoneNumber, 10, 10)) {
            validationsMessage.put("phoneNumber", "Phone number must be 10 digits");
        }

        return validationsMessage;
    }
}