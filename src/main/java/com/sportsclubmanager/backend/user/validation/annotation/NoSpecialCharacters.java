package com.sportsclubmanager.backend.user.validation.annotation;

import static java.lang.annotation.ElementType.*;

import com.sportsclubmanager.backend.user.validation.NoSpecialCharactersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para validar que una cadena de texto no contenga
 * caracteres especiales.
 * <p>
 * Esta anotación se utiliza para restringir los valores de campos, métodos,
 * parámetros, constructores
 * o usos de tipos, permitiendo únicamente caracteres alfanuméricos (letras y
 * números, sin símbolos).
 * Su validación es realizada por la clase {@link NoSpecialCharactersValidator}.
 */
@Constraint(validatedBy = NoSpecialCharactersValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpecialCharacters {
    String message() default "El campo no puede contener caracteres especiales.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
