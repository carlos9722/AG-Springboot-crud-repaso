package com.carlos.curso.springboot.app.services;

import java.util.List;
import java.util.Optional;

import com.carlos.curso.springboot.app.entities.Product;

/**
 * Contrato de la capa de servicios para gestionar productos.
 *
 * Define las operaciones que puede realizar la aplicación sobre Product.
 * La implementación de esta interfaz contendrá la lógica de negocio.
 *
 * El Service actúa como intermediario entre el Controller y el Repository:
 *
 * Controller → Service → Repository → Base de datos
 */
public interface ProductService {

    /**
     * Obtiene todos los productos.
     *
     * @return lista de productos.
     */
    List<Product> findAll();

    /**
     * Busca un producto por su ID.
     *
     * Optional<Product> indica que el producto puede existir o no.
     * Si existe, Optional contiene el Product; si no existe,
     * se devuelve un Optional vacío en lugar de null.
     *
     * @param id identificador del producto.
     * @return Optional con el producto si existe; vacío si no existe.
     */
    Optional<Product> findById(Long id);

    /**
     * Guarda un nuevo producto o actualiza uno existente.
     *
     * @param product producto que se desea guardar.
     * @return producto guardado.
     */
    Product save(Product product);

    /**
     * Actualiza un producto existente utilizando su ID.
     *
     * Optional<Product> indica que el producto puede no existir.
     *
     * @param id identificador del producto a actualizar.
     * @param product datos nuevos del producto.
     * @return Optional con el producto actualizado si existe;
     *         vacío si no existe.
     */
    Optional<Product> update(Long id, Product product);

    /**
     * Elimina un producto utilizando su ID.
     *
     * Optional<Product> permite devolver el producto eliminado
     * si existía o un Optional vacío si no se encontró.
     *
     * @param id identificador del producto a eliminar.
     * @return Optional con el producto eliminado si existía;
     *         vacío si no existía.
     */
    Optional<Product> delete(Long id);
}