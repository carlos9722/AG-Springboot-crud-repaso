package com.carlos.curso.springboot.app.entities;

//import com.carlos.curso.springboot.app.validation.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad JPA que representa un producto en la base de datos.
 *
 * JPA utiliza esta clase para mapear el objeto Java con la tabla "products".
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * Identificador único del producto.
     *
     * JPA genera automáticamente el valor utilizando la estrategia
     * de identidad de la base de datos (por ejemplo, AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto.
     * No puede estar vacío y debe tener entre 3 y 50 caracteres.
     */
    @NotEmpty(message = "{NotEmpty.product.name}")
    @Size(min = 3, max = 50)
    private String name;

    /**
     * Precio del producto.
     * No puede ser null y debe ser como mínimo 500.
     */
    @NotNull(message = "{NotNull.product.price}")
    @Min(value = 500, message = "{Min.product.price}")
    private Integer price;

    /**
     * Descripción del producto.
     * No puede ser null, estar vacía ni contener únicamente espacios.
     */
    //@IsRequired  usando anotación personalizada del package validation, también puedo usar desde el properties
    @NotBlank(message = "{NotBlank.product.description}")
    private String description;

    // Getters y setters: permiten consultar y modificar los atributos.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}