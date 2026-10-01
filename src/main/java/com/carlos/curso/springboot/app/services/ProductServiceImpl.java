package com.carlos.curso.springboot.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlos.curso.springboot.app.entities.Product;
import com.carlos.curso.springboot.app.repositories.ProductRepository;

/**
 * Implementación de ProductService.
 *
 * Contiene la lógica de negocio relacionada con los productos
 * y utiliza ProductRepository para acceder a la base de datos.
 *
 * Flujo:
 * Controller → ProductServiceImpl → ProductRepository → Base de datos
 */
@Service
public class ProductServiceImpl implements ProductService {

    /*
     * Dependencia necesaria para acceder a los productos en la base de datos.
     *
     * Spring la inyecta mediante el constructor (Dependency Injection).
     */
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene todos los productos.
     *
     * readOnly = true indica que esta transacción solo realiza consultas
     * y no está destinada a modificar información.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id identificador del producto.
     * @return Optional con el producto si existe.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda un producto en la base de datos.
     *
     * @param product producto que se desea guardar.
     * @return producto guardado.
     */
    @Transactional
    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    /**
     * Actualiza un producto existente.
     *
     * Primero busca el producto por su ID. Si existe, modifica sus datos
     * y guarda nuevamente la entidad.
     *
     * @param id identificador del producto a actualizar.
     * @param product nuevos datos del producto.
     * @return Optional con el producto actualizado si existe.
     */
    @Transactional
    @Override
    public Optional<Product> update(Long id, Product product) {

        // Buscamos en la base de datos el producto que queremos modificar.
        Optional<Product> productOptionalDb = repository.findById(id);

        if (productOptionalDb.isPresent()) {

            // Obtenemos el Product real contenido dentro del Optional.
            Product productDb = productOptionalDb.orElseThrow();

            // Actualizamos únicamente los datos permitidos.
            productDb.setName(product.getName());
            productDb.setDescription(product.getDescription());
            productDb.setPrice(product.getPrice());

            // Guardamos la entidad modificada.
            return Optional.of(repository.save(productDb));
        }

        // Si no existe, devolvemos Optional.empty().
        return productOptionalDb;
    }

    /**
     * Elimina un producto por su ID.
     *
     * Primero verifica que el producto exista y después lo elimina.
     *
     * @param id identificador del producto.
     * @return Optional con el producto eliminado si existía.
     */
    @Transactional
    @Override
    public Optional<Product> delete(Long id) {

        Optional<Product> productOptionalDb = repository.findById(id);

        // Si existe, ejecutamos el DELETE.
        productOptionalDb.ifPresent(productDb -> {
            repository.delete(productDb);
        });

        return productOptionalDb;
    }
}