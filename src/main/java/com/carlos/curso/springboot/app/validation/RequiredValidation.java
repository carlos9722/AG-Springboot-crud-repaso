package com.carlos.curso.springboot.app.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para la anotación @IsRequired.
 *
 * Se encarga de comprobar que un String tenga contenido.
 *
 * ConstraintValidator<IsRequired, String> significa:
 * - IsRequired: anotación que este validador implementa.
 * - String: tipo de dato que será validado.
 */
public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    /**
     * Ejecuta la validación del valor recibido.
     *
     * @param value   valor que se desea validar.
     * @param context contexto proporcionado por Bean Validation.
     * @return true si el valor es válido; false si no lo es.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        /*
         * Validación manual equivalente:
         *
         * - value != null: comprueba que el valor no sea null.
         * - !value.isEmpty(): comprueba que no sea una cadena vacía.
         * - !value.isBlank(): comprueba que no contenga únicamente espacios.
         *
         * if(value != null && !value.isEmpty() && !value.isBlank()){
         *     return true;
         * }
         * return false;
         */

        /*
         * StringUtils.hasText() realiza esta comprobación de forma
         * más sencilla y legible.
         *
         * "Hola" → true
         * ""     → false
         * "   "  → false
         * null   → false
         */
        return StringUtils.hasText(value);
    }
}