package com.carlos.curso.springboot.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración relacionada con Spring Security.
 *
 * @Configuration indica que esta clase contiene configuración
 * que Spring debe procesar.
 *
 * En este caso configuramos un PasswordEncoder para que pueda
 * ser utilizado en diferentes partes de la aplicación.
 */
@Configuration
public class SpringSecurityConfig {

    /**
     * Registra un PasswordEncoder dentro del contenedor de Spring.
     *
     * @Bean le indica a Spring:
     *
     * "Ejecuta este método y guarda el objeto que devuelve
     * dentro del contenedor de Spring."
     *
     * Después, cualquier otra clase que necesite un PasswordEncoder
     * puede recibirlo mediante inyección de dependencias.
     *
     * El método devuelve PasswordEncoder porque esa es la interfaz
     * que define las operaciones necesarias para trabajar con
     * contraseñas.
     *
     * BCryptPasswordEncoder es una implementación concreta
     * de esa interfaz que utiliza BCrypt para generar hashes
     * de contraseñas.
     *
     * Flujo:
     *
     * @Bean
     *   ↓
     * passwordEncoder()
     *   ↓
     * new BCryptPasswordEncoder()
     *   ↓
     * Spring guarda el objeto en su contenedor
     *   ↓
     * Otras clases pueden solicitar PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {

        /*
         * Creamos la implementación concreta de PasswordEncoder.
         *
         * BCrypt se utiliza para almacenar las contraseñas
         * de forma segura mediante un hash.
         *
         * No debemos guardar la contraseña original directamente
         * en la base de datos.
         */
        return new BCryptPasswordEncoder();
    }
}