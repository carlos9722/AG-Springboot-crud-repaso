package com.carlos.curso.springboot.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.carlos.curso.springboot.app.entities.User;

/**
 * Repositorio encargado de acceder y gestionar los usuarios
 * en la base de datos.
 *
 * Al extender CrudRepository, Spring Data nos proporciona
 * automáticamente las operaciones CRUD básicas:
 *
 * - save()      → guardar o actualizar un usuario.
 * - findAll()   → obtener todos los usuarios.
 * - findById()  → buscar un usuario por su ID.
 * - deleteById()→ eliminar un usuario por su ID.
 *
 * No necesitamos implementar estos métodos manualmente.
 */
public interface UserRepository extends CrudRepository<User, Long> {

}