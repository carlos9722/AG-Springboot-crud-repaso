package com.carlos.curso.springboot.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.carlos.curso.springboot.app.entities.Role;

/**
 * Repositorio encargado de acceder y gestionar los roles
 * en la base de datos.
 *
 * Al extender CrudRepository, Spring Data proporciona
 * automáticamente las operaciones CRUD básicas:
 *
 * - save()       → guardar o actualizar un Role.
 * - findAll()    → obtener todos los roles.
 * - findById()   → buscar un Role por su ID.
 * - deleteById() → eliminar un Role por su ID.
 *
 * No necesitamos implementar estos métodos manualmente.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

}