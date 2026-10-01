package com.carlos.curso.springboot.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlos.curso.springboot.app.ProductValidation;
import com.carlos.curso.springboot.app.entities.Product;
import com.carlos.curso.springboot.app.services.ProductService;

import jakarta.validation.Valid;

/**
 * Controlador REST encargado de gestionar las peticiones HTTP de productos.
 *
 * Flujo:
 *
 * Cliente → Controller → Service → Repository → Base de datos
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Servicio encargado de ejecutar la lógica de negocio de los productos.
     */
    private final ProductService service;

    //private  ProductValidation validation;

    public ProductController(ProductService service, ProductValidation validation) {
        this.service = service;
        //this.validation = validation;
    }

    /**
     * Inyección de ProductService mediante constructor.
     */
    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * Obtiene todos los productos.
     *
     * GET /api/products
     *
     * @return lista de productos.
     */
    @GetMapping
    public List<Product> list() {
        return service.findAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * ResponseEntity permite controlar la respuesta HTTP completa:
     * código de estado, encabezados y cuerpo de la respuesta.
     *
     * El ? significa que el cuerpo puede contener diferentes tipos de datos.
     * Por ejemplo, un Product cuando existe o ningún cuerpo cuando no existe.
     *
     * Optional<Product> indica que el producto puede existir o no.
     *
     * @param id identificador del producto.
     * @return 200 OK con el producto si existe,
     *         o 404 NOT FOUND si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {

        Optional<Product> productOptional = service.findById(id);

        // isPresent() comprueba si Optional contiene un Product.
        if (productOptional.isPresent()) {

            // orElseThrow() obtiene el Product contenido en Optional.
            return ResponseEntity.ok(productOptional.orElseThrow());
        }

        // Si no existe, devolvemos HTTP 404.
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuevo producto.
     *
     * @Valid ejecuta las validaciones definidas en Product.
     * BindingResult contiene los posibles errores encontrados.
     *
     * @RequestBody convierte el JSON recibido en un objeto Product.
     *
     * @return 201 CREATED con el producto creado,
     *         o 400 BAD REQUEST si existen errores de validación.
     */
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody Product product,
            BindingResult result) {

        //validation.validate(product, result);
                
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(product));
    }

    /**
     * Actualiza un producto existente.
     *
     * PUT /api/products/{id}
     *
     * @param product nuevos datos del producto.
     * @param result resultado de las validaciones.
     * @param id identificador del producto.
     * @return producto actualizado o 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Valid @RequestBody Product product,
            BindingResult result,
            @PathVariable Long id) {

        //validation.validate(product, result);

        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<Product> productOptional = service.update(id, product);

        // Comprobamos si el producto que se quería actualizar existe.
        if (productOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(productOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto.
     * @return 200 OK con el producto eliminado,
     *         o 404 NOT FOUND si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Optional<Product> productOptionalDb = service.delete(id);

        if (productOptionalDb.isPresent()) {
            return ResponseEntity.ok(productOptionalDb.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Construye una respuesta con los errores de validación.
     *
     * BindingResult contiene los errores producidos por @Valid.
     *
     * @param result resultado de las validaciones.
     * @return HTTP 400 con un mapa de errores.
     */
    private ResponseEntity<?> validation(BindingResult result) {

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(
                err.getField(),
                "El campo " + err.getField() + " " + err.getDefaultMessage()
            );
        });

        return ResponseEntity.badRequest().body(errors);
    }
}