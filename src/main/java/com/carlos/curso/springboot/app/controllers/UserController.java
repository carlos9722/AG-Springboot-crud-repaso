package com.carlos.curso.springboot.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlos.curso.springboot.app.entities.User;
import com.carlos.curso.springboot.app.services.UserService;

import jakarta.validation.Valid;

/**
 * Controlador REST encargado de recibir las peticiones HTTP relacionadas
 * con los usuarios.
 *
 * El controlador pertenece a la capa de presentación/API.
 *
 * Flujo general:
 *
 * Cliente
 *    ↓
 * UserController
 *    ↓
 * UserService
 *    ↓
 * UserRepository
 *    ↓
 * Base de datos
 *
 * @RestController indica que los métodos de esta clase responden
 * directamente con datos, normalmente en formato JSON.
 */
@RestController

/**
 * Define la ruta base para todos los endpoints de este controlador.
 *
 * Por lo tanto:
 *
 * GET  /users
 * POST /users
 */
@RequestMapping("/users")
public class UserController {

    /**
     * Dependencia del servicio de usuarios.
     *
     * El Controller no debería encargarse directamente de acceder
     * a la base de datos.
     *
     * En su lugar:
     *
     * Controller → Service → Repository
     *
     * final significa que la referencia al servicio se asigna una vez
     * mediante el constructor y no se cambia posteriormente.
     */
    private final UserService service;

    /**
     * Constructor del controlador.
     *
     * Spring utiliza inyección de dependencias para proporcionar
     * automáticamente una implementación de UserService.
     *
     * @param service servicio que contiene la lógica relacionada
     *                con los usuarios.
     */
    UserController(UserService service) {
        this.service = service;
    }

    /**
     * Obtiene todos los usuarios.
     *
     * Endpoint:
     *
     * GET /users
     *
     * El Controller recibe la petición y delega la operación
     * al UserService.
     *
     * @return lista de usuarios.
     */
    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    /**
     * Crea un nuevo usuario.
     *
     * Endpoint:
     *
     * POST /users
     *
     * @RequestBody convierte el JSON recibido en el cuerpo de la
     * petición HTTP en un objeto User.
     *
     * @Valid indica a Spring que debe ejecutar las validaciones
     * definidas sobre User.
     *
     * BindingResult recibe los errores producidos por @Valid.
     *
     * @param user usuario construido a partir del JSON recibido.
     * @param result resultado de las validaciones.
     *
     * @return HTTP 400 si existen errores de validación.
     *         HTTP 201 si el usuario fue creado correctamente.
     */
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody User user,
            BindingResult result) {

        /*
         * Comprobamos si existen errores producidos por @Valid.
         *
         * Si hay errores, no continuamos con el guardado y devolvemos
         * una respuesta HTTP 400 (Bad Request).
         */
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        /*
         * Si no existen errores:
         *
         * 1. Enviamos el User al servicio.
         * 2. El servicio se encarga de guardarlo.
         * 3. Devolvemos HTTP 201 (Created).
         *
         * body(...) contiene el usuario creado que se devolverá
         * en la respuesta.
         */
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(user));
    }

    /**
     * Construye una respuesta con los errores de validación.
     *
     * BindingResult contiene los errores producidos por @Valid.
     *
     * Se recorren los errores de cada campo y se construye un
     * Map<String, String>.
     *
     * Ejemplo de respuesta:
     *
     * {
     *     "username": "El campo username es requerido",
     *     "password": "El campo password es requerido"
     * }
     *
     * @param result resultado de las validaciones.
     * @return HTTP 400 con un mapa de errores.
     */
    private ResponseEntity<?> validation(BindingResult result) {

        /*
         * HashMap permite almacenar los errores utilizando:
         *
         * clave   → nombre del campo
         * valor   → mensaje del error
         *
         * Ejemplo:
         *
         * "username" → "El campo username es requerido"
         */
        Map<String, String> errors = new HashMap<>();

        /*
         * getFieldErrors() obtiene los errores relacionados
         * específicamente con los campos del objeto User.
         *
         * forEach() recorre cada error encontrado.
         */
        result.getFieldErrors().forEach(err -> {

            /*
             * err.getField()
             * → obtiene el nombre del campo que tiene el error.
             *
             * err.getDefaultMessage()
             * → obtiene el mensaje definido para esa validación.
             */
            errors.put(
                err.getField(),
                "El campo " + err.getField() + " " + err.getDefaultMessage()
            );
        });

        /*
         * badRequest() genera una respuesta HTTP 400.
         *
         * body(errors) coloca nuestro mapa de errores
         * dentro del cuerpo de la respuesta.
         */
        return ResponseEntity.badRequest().body(errors);
    }

}