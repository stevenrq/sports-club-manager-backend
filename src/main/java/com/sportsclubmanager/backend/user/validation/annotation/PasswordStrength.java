package com.sportsclubmanager.backend.user.validation.annotation;

import com.sportsclubmanager.backend.user.validation.PasswordStrengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Anotación personalizada para validar la fortaleza de una contraseña.
 * <p>
 * Esta anotación se utiliza para garantizar que una contraseña cumpla con los siguientes requisitos:
 * <ul>
 *   <li>Al menos una letra minúscula</li>
 *   <li>Al menos una letra mayúscula</li>
 *   <li>Al menos un número</li>
 *   <li>Al menos un carácter especial</li>
 *   <li>Longitud mínima de 6 caracteres</li>
 * </ul>
 * Su validación es realizada por la clase {@link PasswordStrengthValidator}.
 */
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordStrength {

    String message() default "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character and must be at least 6 characters long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}