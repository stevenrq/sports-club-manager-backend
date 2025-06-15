package com.sportsclubmanager.backend.shared.validation;

import com.sportsclubmanager.backend.member.model.PlayerResponse;
import com.sportsclubmanager.backend.user.dto.ApiResponse;
import com.sportsclubmanager.backend.user.dto.UserResponse;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ValidationService {

    /**
     * Maneja la validación de un objeto de tipo usuario y sus resultados de
     * validación.
     *
     * @param target        objeto a validar
     * @param bindingResult resultados de la validación
     * @param <T>           tipo genérico del objeto a validar
     * @return ResponseEntity con la respuesta de la API que contiene UserResponse
     */
    public <T> ResponseEntity<ApiResponse<UserResponse>> handleValidation(
            T target,
            BindingResult bindingResult) {
        if (!(target instanceof User || target instanceof UserUpdateRequest)) {
            throw new IllegalArgumentException("Tipo de objeto no válido");
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
     * Maneja la validación de un objeto de tipo jugador y sus resultados de
     * validación.
     *
     * @param target        objeto a validar
     * @param bindingResult resultados de la validación
     * @param <T>           tipo genérico del objeto a validar
     * @return ResponseEntity con la respuesta de la API que contiene PlayerResponse
     */
    public <T> ResponseEntity<ApiResponse<PlayerResponse>> handlePlayerValidation(T target,
            BindingResult bindingResult) {
        if (!(target instanceof User || target instanceof UserUpdateRequest)) {
            throw new IllegalArgumentException("Tipo de objeto no válido");
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
     * Crea una respuesta de error de validación.
     *
     * @param errors mapa con los errores de validación
     * @param <T>    tipo genérico para la respuesta
     * @return ResponseEntity con estado HTTP 400 (Bad Request) y los errores en el
     *         cuerpo
     */
    public <T> ResponseEntity<ApiResponse<T>> validationError(
            Map<String, String> errors) {
        ApiResponse<T> apiResponse = new ApiResponse<>(errors);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * Procesa los errores de validación del BindingResult y los convierte en una
     * respuesta de error.
     *
     * @param <T>           tipo genérico para la respuesta
     * @param bindingResult resultado de la validación que contiene los errores
     * @return ResponseEntity con estado HTTP 400 (Bad Request) y los errores
     *         formateados en el cuerpo
     */
    public <T> ResponseEntity<ApiResponse<T>> validate(
            BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return validationError(errors);
    }

    /**
     * Verifica si un número tiene una longitud válida dentro del rango
     * especificado.
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
     * @param user usuario a validar, debe ser de tipo <code>User</code> o
     *             <code>UserUpdateRequest</code>
     * @return mapa con los mensajes de validación si hay errores, o mapa vacío si
     *         es válido
     * @throws IllegalArgumentException si el tipo de usuario no es válido
     */
    public Map<String, String> validate(Object user) {
        Map<String, String> validationsMessage = new HashMap<>();
        Long nationalId = 0L;
        Long phoneNumber;

        if (user instanceof User u) {
            nationalId = u.getNationalId();
            phoneNumber = u.getPhoneNumber();
        } else if (user instanceof UserUpdateRequest ur) {
            phoneNumber = ur.getPhoneNumber();
        } else {
            throw new IllegalArgumentException("Tipo de usuario no válido");
        }

        if (!isValidLength(nationalId, 7, 10) && user instanceof User) {
            validationsMessage.put(
                    "nationalId",
                    "El ID nacional debe tener entre 7 y 10 dígitos");
        } else if (!isValidLength(phoneNumber, 10, 10)) {
            validationsMessage.put(
                    "phoneNumber",
                    "El número de teléfono debe tener 10 dígitos");
        }

        return validationsMessage;
    }
}
