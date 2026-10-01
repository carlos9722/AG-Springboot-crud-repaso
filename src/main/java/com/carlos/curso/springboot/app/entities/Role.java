package com.carlos.curso.springboot.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa un rol dentro de la aplicación.
 *
 * Un rol permite identificar el tipo de acceso que tiene un usuario.
 *
 * Ejemplos:
 *
 * ROLE_USER
 * ROLE_ADMIN
 *
 * Esta clase se relaciona con la tabla "roles" de la base de datos.
 */
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Identificador único del rol.
     *
     * @Id indica que es la clave primaria.
     *
     * @GeneratedValue(strategy = IDENTITY) indica que el ID
     * será generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol.
     *
     * unique = true indica que no pueden existir dos roles
     * con el mismo nombre en la base de datos.
     *
     * Ejemplo:
     *
     * ROLE_USER   → válido
     * ROLE_ADMIN  → válido
     * ROLE_USER   → ❌ no se puede repetir
     */
    @Column(unique = true)
    private String name;

    /**
     * CONSTRUCTOR VACÍO
     * -----------------
     *
     * Un constructor es un método especial que se ejecuta cuando
     * creamos un objeto utilizando "new".
     *
     * Este constructor permite crear un Role sin proporcionar
     * ningún dato inicialmente:
     *
     *     Role role = new Role();
     *
     * En ese momento el objeto se crea, pero sus atributos todavía
     * tienen sus valores iniciales:
     *
     *     id   = null
     *     name = null
     *
     * Este constructor es especialmente importante para JPA/Hibernate,
     * porque JPA necesita poder crear objetos de la entidad sin
     * tener que recibir parámetros en el constructor.
     *
     * Por eso normalmente las entidades JPA deben tener un
     * constructor vacío.
     */
    public Role() {
    }

    /**
     * CONSTRUCTOR CON PARÁMETROS
     * --------------------------
     *
     * Este constructor permite crear un Role indicando directamente
     * el nombre del rol.
     *
     * Por ejemplo:
     *
     *     Role role = new Role("ROLE_ADMIN");
     *
     * Es equivalente a crear el objeto y después asignarle el nombre:
     *
     *     Role role = new Role();
     *     role.setName("ROLE_ADMIN");
     *
     * La diferencia es que con este constructor podemos hacer ambas
     * cosas en una sola línea.
     *
     * El ID no se recibe como parámetro porque el ID es generado
     * automáticamente por la base de datos gracias a:
     *
     *     @GeneratedValue(strategy = GenerationType.IDENTITY)
     *
     * @param name nombre del rol.
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Obtiene el identificador del rol.
     *
     * @return id del rol.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del rol.
     *
     * @param id identificador del rol.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return nombre del rol.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param name nombre del rol.
     */
    public void setName(String name) {
        this.name = name;
    }
}