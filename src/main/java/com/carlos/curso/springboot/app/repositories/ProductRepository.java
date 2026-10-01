package com.carlos.curso.springboot.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.carlos.curso.springboot.app.entities.Product;

/**
 * Repositorio para acceder y gestionar productos en la base de datos.
 *
 * CrudRepository recibe dos tipos genéricos:
 *
 * CrudRepository<Entidad, TipoDelId>
 *
 * En este caso:
 * - Product: entidad que administra el repositorio.
 * - Long: tipo de dato del ID de Product.
 *
 * Spring Data genera automáticamente la implementación de este repositorio,
 * proporcionando operaciones CRUD como guardar, buscar y eliminar productos.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

}