package com.carlos.curso.springboot.app.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotación personalizada para validar que un campo sea obligatorio.
 *
 * Esta anotación se utiliza junto con RequiredValidation, que contiene
 * la lógica que determina si el valor es válido o no.
 *
 * Flujo:
 *
 * @IsRequired
 *      ↓
 * RequiredValidation
 *      ↓
 * StringUtils.hasText(value)
 */
@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IsRequired {

    /**
     * Mensaje que se mostrará cuando la validación falle.
     */
    String message() default "Es requerido usando anotaciones";

    /**
     * Permite agrupar diferentes validaciones.
     *
     * Normalmente se deja vacío cuando no se necesitan grupos.
     */
    Class<?>[] groups() default {};

    /**
     * Permite transportar información adicional sobre la validación.
     *
     * Normalmente se deja vacío cuando no se necesita información
     * adicional.
     */
    Class<? extends Payload>[] payload() default {};
}