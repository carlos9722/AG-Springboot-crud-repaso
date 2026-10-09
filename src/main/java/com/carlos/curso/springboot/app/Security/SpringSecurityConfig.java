package com.carlos.curso.springboot.app.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de la aplicación.
 *
 * @Configuration indica que Spring debe procesar esta clase
 * como una clase de configuración.
 *
 * Aquí definimos:
 *
 * 1. Cómo se generan los hashes de las contraseñas.
 * 2. Qué peticiones HTTP son públicas.
 * 3. Qué peticiones requieren autenticación.
 * 4. Cómo se gestionan las sesiones.
 * 5. La configuración de CSRF.
 */
@Configuration
public class SpringSecurityConfig {

    /**
     * Registra un PasswordEncoder en el contenedor de Spring.
     *
     * @Bean indica que el objeto devuelto por este método
     * debe ser administrado por Spring.
     *
     * PasswordEncoder es la interfaz que define operaciones
     * para generar y comprobar hashes de contraseñas.
     *
     * BCryptPasswordEncoder es una implementación concreta
     * que utiliza el algoritmo BCrypt.
     *
     * Otras clases, como UserServiceImpl, pueden recibir
     * esta dependencia mediante inyección por constructor.
     *
     * @return implementación de PasswordEncoder basada en BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     *
     * SecurityFilterChain define cómo Spring Security debe
     * procesar las peticiones HTTP antes de que lleguen
     * a los controladores.
     *
     * HttpSecurity permite configurar las reglas de acceso,
     * CSRF y la gestión de sesiones.
     *
     * @param http objeto que permite construir la configuración
     *             de seguridad HTTP.
     *
     * @return cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error al configurar la seguridad.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

            /*
             * Define qué peticiones pueden realizarse sin
             * autenticación y cuáles requieren estar autenticado.
             */
            .authorizeHttpRequests(authz -> authz

                /*
                 * Permite consultar /api/users mediante GET
                 * sin iniciar sesión.
                 *
                 * IMPORTANTE:
                 * La ruta debe coincidir con la ruta real del
                 * controlador, incluidos sus posibles prefijos.
                 */
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()

                /*
                 * Permite realizar peticiones POST a /api/register
                 * sin autenticación.
                 */
                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()

                /*
                 * Permite acceder a /users sin autenticación,
                 * independientemente del método HTTP utilizado.
                 *
                 * Esta regla puede ser demasiado amplia si se
                 * pretende permitir únicamente el registro público.
                 */
                .requestMatchers("/users").permitAll()

                /*
                 * Cualquier petición que no coincida con las
                 * reglas públicas anteriores requiere que el
                 * usuario esté autenticado.
                 *
                 * authenticated() NO significa que el usuario
                 * sea administrador; solamente exige autenticación.
                 */
                .anyRequest().authenticated()
            )

            /*
             * Desactiva la protección CSRF de Spring Security.
             *
             * CSRF protege frente a determinados ataques que
             * aprovechan credenciales enviadas automáticamente
             * por el navegador.
             *
             * Esta configuración suele utilizarse en APIs
             * sin estado que autentican mediante tokens enviados
             * explícitamente, pero debe evaluarse según el mecanismo
             * real de autenticación de la aplicación.
             */
            .csrf(csrf -> csrf.disable())

            /*
             * Configura la política de creación de sesiones.
             *
             * STATELESS indica que Spring Security no debe
             * utilizar una sesión HTTP para conservar el contexto
             * de autenticación entre peticiones.
             *
             * Es habitual en APIs REST autenticadas mediante tokens,
             * por ejemplo, JWT.
             *
             * IMPORTANTE:
             * STATELESS no crea un token ni configura JWT por sí solo.
             * La autenticación debe implementarse por separado.
             */
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            /*
             * Construye y devuelve la configuración final.
             */
            .build();
    }
}