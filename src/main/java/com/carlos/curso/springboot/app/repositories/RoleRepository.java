package com.carlos.curso.springboot.app.repositories;

import java.util.Optional;

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
 *
 * Además, podemos declarar métodos personalizados y Spring Data
 * puede generar automáticamente la consulta basándose en el
 * nombre del método.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * Spring Data interpreta el nombre del método:
     *
     * findByName
     *    │
     *    ├── find → buscar
     *    │
     *    └── ByName → utilizando el campo "name"
     *
     * Por lo tanto, Spring genera internamente una consulta
     * equivalente conceptualmente a:
     *
     * SELECT * FROM roles WHERE name = ?
     *
     * No necesitamos escribir el SQL ni implementar el método.
     *
     * Optional<Role> indica que el rol puede existir o no:
     *
     * - Si encuentra el rol → Optional contiene un Role.
     * - Si no lo encuentra → Optional vacío.
     *
     * @param name nombre del rol que queremos buscar.
     * @return Optional con el rol encontrado o vacío si no existe.
     */
    Optional<Role> findByName(String name);
}