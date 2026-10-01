package com.carlos.curso.springboot.app;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.carlos.curso.springboot.app.entities.Product;

/**
 * Validador personalizado para la entidad Product.
 *
 * Implementa la interfaz Validator de Spring para realizar
 * validaciones manuales sobre un objeto Product.
 *
 * A diferencia de las validaciones realizadas mediante anotaciones
 * como @NotNull, @NotBlank o @Min, aquí nosotros escribimos
 * directamente la lógica de validación.
 */
@Component
public class ProductValidation implements Validator {

    /**
     * Indica qué tipo de objeto puede validar esta clase.
     *
     * Class<?> significa que recibimos información sobre una clase
     * cuyo tipo concreto no conocemos.
     *
     * Product.class representa la clase Product.
     *
     * isAssignableFrom() permite comprobar si la clase recibida
     * corresponde a Product o a una clase compatible con Product.
     *
     * @param clazz clase del objeto que se quiere validar.
     * @return true si esta clase puede validar el objeto; false si no.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    /**
     * Ejecuta las reglas de validación sobre el objeto recibido.
     *
     * Object target representa el objeto que Spring quiere validar.
     * Como este método trabaja con Product, hacemos un casting:
     *
     * Product product = (Product) target;
     *
     * El parámetro Errors permite registrar los errores encontrados
     * durante la validación.
     *
     * @param target objeto que se desea validar.
     * @param errors objeto donde se almacenan los errores encontrados.
     */
    @Override
    public void validate(Object target, Errors errors) {

        /*
         * Convertimos el Object recibido en Product para poder acceder
         * a sus propiedades, como getDescription() y getPrice().
         */
        Product product = (Product) target;

        /*
         * Comprueba si el campo "name" está vacío o contiene
         * únicamente espacios en blanco.
         *
         * Si encuentra un error, lo registra dentro de Errors.
         *
         * Parámetros:
         * - errors: objeto donde se almacenan los errores.
         * - "name": campo de Product que estamos validando.
         * - null: código del error.
         * - mensaje: texto que se mostrará al usuario.
         */
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "name",
                null,
                "es requerido!"
        );

        /*
         * Validamos manualmente la descripción.
         *
         * Primero comprobamos si es null.
         * Después comprobamos si está vacía o contiene únicamente
         * espacios mediante isBlank().
         */
        if (product.getDescription() == null || product.getDescription().isBlank()) {

            /*
             * rejectValue() registra un error asociado específicamente
             * al campo "description".
             */
            errors.rejectValue(
                    "description",
                    null,
                    "es requerido, por favor"
            );
        }

        /*
         * Validamos el precio.
         *
         * Primero comprobamos si es null porque Integer es un objeto
         * y puede no tener ningún valor.
         */
        if (product.getPrice() == null) {

            /*
             * Si el precio es null, registramos un error en el campo price.
             */
            errors.rejectValue(
                    "price",
                    null,
                    "No puede ser nulo, ok!"
            );

        /*
         * Si el precio no es null, podemos comparar su valor.
         *
         * La regla indica que el precio debe ser mayor o igual a 500.
         */
        } else if (product.getPrice() < 500) {

            errors.rejectValue(
                    "price",
                    null,
                    "debe ser un valor numerico mayot o igual que 500"
            );
        }
    }
}