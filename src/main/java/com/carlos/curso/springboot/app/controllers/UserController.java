package com.carlos.curso.springboot.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * POST /users/register
 */
@RequestMapping("/users")
public class UserController {

    /**
     * Dependencia del servicio de usuarios.
     *
     * El Controller no debería acceder directamente a la base de datos.
     * Delega esa responsabilidad al servicio.
     *
     * final significa que la referencia se asigna una vez
     * mediante el constructor y no puede reasignarse después.
     */
    private final UserService service;

    /**
     * Constructor con inyección de dependencias.
     *
     * Spring proporciona automáticamente una implementación de UserService.
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
     * Endpoint: GET /users
     *
     * Delega la consulta a UserService y devuelve la lista obtenida.
     *
     * @return lista de usuarios, que Spring normalmente serializa a JSON.
     */
    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    /**
     * Crea un usuario utilizando la lógica general de creación.
     *
     * Endpoint: POST /users
     *
     * @RequestBody convierte el JSON del cuerpo de la petición
     * en un objeto User.
     *
     * @Valid activa las validaciones declaradas en User, como
     * @NotBlank y @Size.
     *
     * BindingResult recoge los errores de validación para poder
     * responder de forma controlada cuando hay campos inválidos.
     *
     * @param user usuario construido a partir del JSON recibido.
     * @param result resultado de las validaciones.
     *
     * @return HTTP 400 si existen errores de validación;
     *         HTTP 201 si el usuario se guarda correctamente.
     */
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody User user,
            BindingResult result) {

        /*
         * Si hay errores en los campos, devolvemos HTTP 400
         * y no continuamos con el guardado.
         */
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        /*
         * Si la validación es correcta:
         *
         * 1. Delegamos el guardado a UserService.
         * 2. Recibimos el usuario guardado.
         * 3. Respondemos con HTTP 201 (Created).
         *
         * ResponseEntity permite controlar tanto el estado HTTP
         * como el contenido de la respuesta.
         */
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(user));
    }

    /**
     * Registra un usuario mediante el endpoint público de registro.
     *
     * Endpoint: POST /users/register
     *
     * Antes de reutilizar create(), fuerza admin = false.
     *
     * Esto evita que el valor de admin enviado en el JSON sea
     * utilizado para solicitar el rol administrativo durante
     * este proceso de registro.
     *
     * @param user usuario construido a partir del JSON recibido.
     * @param result resultado de las validaciones ejecutadas por @Valid.
     *
     * @return la misma respuesta que genera create():
     *         HTTP 400 si hay errores o HTTP 201 si se crea el usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody User user,
            BindingResult result) {

        /*
         * Regla de seguridad para el registro público:
         * el usuario nuevo no puede solicitar ser administrador
         * enviando admin = true en el JSON.
         *
         * Este valor se fuerza antes de delegar en create().
         */
        user.setAdmin(false);

        /*
         * Reutilizamos el método create() para no duplicar
         * la validación, el guardado y la construcción de la respuesta.
         *
         * Como result pertenece al mismo objeto User, los errores
         * recogidos durante la validación se mantienen.
         */
        return create(user, result);
    }

    /**
     * Construye una respuesta con los errores de validación.
     *
     * BindingResult contiene los errores generados por @Valid.
     *
     * Se recorren los errores de campo y se construye un mapa
     * donde la clave es el nombre del campo y el valor es el mensaje.
     *
     * Ejemplo conceptual:
     *
     * {
     *     "username": "El campo username no debe estar vacío",
     *     "password": "El campo password no debe estar vacío"
     * }
     *
     * @param result resultado de las validaciones.
     * @return respuesta HTTP 400 con un mapa de errores.
     */
    private ResponseEntity<?> validation(BindingResult result) {

        /*
         * HashMap almacena pares clave-valor:
         *
         * clave → nombre del campo.
         * valor → mensaje de error.
         *
         * Ejemplo:
         * "username" → "El campo username no debe estar vacío".
         */
        Map<String, String> errors = new HashMap<>();

        /*
         * getFieldErrors() obtiene los errores asociados
         * específicamente a los campos del objeto User.
         *
         * forEach() recorre cada error encontrado.
         */
        result.getFieldErrors().forEach(err -> {

            /*
             * err.getField() obtiene el nombre del campo.
             *
             * err.getDefaultMessage() obtiene el mensaje definido
             * en la anotación de validación correspondiente.
             */
            errors.put(
                err.getField(),
                "El campo " + err.getField() + " " + err.getDefaultMessage()
            );
        });

        /*
         * badRequest() establece HTTP 400 (Bad Request).
         *
         * body(errors) coloca el mapa dentro del cuerpo de la respuesta.
         */
        return ResponseEntity.badRequest().body(errors);
    }

}