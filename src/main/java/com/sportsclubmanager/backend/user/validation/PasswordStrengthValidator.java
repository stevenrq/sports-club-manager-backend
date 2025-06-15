package com.sportsclubmanager.backend.user.validation;

import com.sportsclubmanager.backend.user.validation.annotation.PasswordStrength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordStrengthValidator
        implements ConstraintValidator<PasswordStrength, String> {

    /**
     * Valida que una contraseña cumpla con los requisitos de fortaleza.
     * La contraseña debe contener:
     * <ul>
     * <li>Al menos una letra minúscula</li>
     * <li>Al menos una letra mayúscula</li>
     * <li>Al menos un número</li>
     * <li>Al menos un carácter especial</li>
     * <li>Longitud mínima de 6 caracteres</li>
     * </ul>
     *
     * @param value   Valor de la contraseña a validar
     * @param context Contexto de la validación
     * @return true si la contraseña cumple con los requisitos, false en caso
     *         contrario
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{}\\[\\]:;\"'<>,.?/~`|\\\\])[A-Za-z\\d!@#$%^&*()\\-_=+{}\\[\\]:;\"'<>,.?/~`|\\\\]{6,}$");
    }
}
