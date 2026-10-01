package com.carlos.curso.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Clase de configuración adicional de la aplicación.
 *
 * Su función principal en este caso es indicarle a Spring que debe
 * cargar el archivo messages.properties que se encuentra dentro
 * de src/main/resources.
 */
@Configuration

/**
 * Indica a Spring que debe cargar un archivo de propiedades
 * adicional dentro del contexto de la aplicación.
 *
 * "classpath:" significa que Spring buscará el archivo dentro
 * de los recursos disponibles en el classpath de la aplicación.
 *
 * En este caso:
 *
 * src/main/resources/messages.properties
 */
@PropertySource("classpath:messages.properties")
public class AppConfig {

}