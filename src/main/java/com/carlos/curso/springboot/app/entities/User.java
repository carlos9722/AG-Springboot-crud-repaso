package com.carlos.curso.springboot.app.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un usuario dentro de la aplicación.
 *
 * Esta clase se relaciona con la tabla "users" de la base de datos.
 *
 * Además, un usuario puede tener varios roles y un mismo rol
 * puede pertenecer a varios usuarios.
 *
 * Ejemplo:
 *
 * Carlos → ROLE_USER, ROLE_ADMIN
 * Ana    → ROLE_USER
 *
 * Por eso utilizamos una relación @ManyToMany.
 */
@Entity

/**
 * Indica que esta entidad se almacena en la tabla "users".
 */
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     *
     * @Id indica que es la clave primaria.
     *
     * @GeneratedValue(strategy = IDENTITY) indica que el valor
     * será generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario.
     *
     * @Column(unique = true) indica que no pueden existir dos
     * usuarios con el mismo username en la base de datos.
     *
     * @NotBlank indica que no puede ser null, vacío o contener
     * únicamente espacios en blanco.
     *
     * @Size indica que debe tener entre 4 y 12 caracteres.
     */
    @Column(unique = true)
    @NotBlank
    @Size(min = 4, max = 12)
    private String username;

    /**
     * Contraseña del usuario.
     *
     * @NotBlank indica que no puede ser null, vacía o contener
     * únicamente espacios en blanco.
     *
     * @Size indica que debe tener entre 4 y 12 caracteres.
     *
     * En una aplicación real, la contraseña no debería almacenarse
     * directamente en texto plano, sino utilizando un algoritmo
     * de hashing como BCrypt.
     */
    @NotBlank
    @Size(min = 4, max = 12)
    private String password;

    /**
     * Relación muchos a muchos entre User y Role.
     *
     * Un usuario puede tener varios roles:
     *
     * User → ROLE_USER
     *      → ROLE_ADMIN
     *
     * Y un mismo Role puede pertenecer a muchos usuarios:
     *
     * ROLE_USER → Carlos
     *           → Ana
     *           → Pedro
     *
     * Como una relación ManyToMany necesita una tabla intermedia,
     * utilizamos @JoinTable.
     */
    @ManyToMany

    /**
     * Define la tabla intermedia que relaciona usuarios y roles.
     *
     * En la base de datos tendremos:
     *
     * users
     * roles
     * users_roles  ← tabla intermedia
     */
    @JoinTable(
        name = "users_roles",

        /**
         * Columna que apunta desde users_roles hacia la entidad User.
         *
         * user_id será la FK que identifica al usuario.
         */
        joinColumns = @JoinColumn(name="user_id"),

        /**
         * Columna que apunta desde users_roles hacia la entidad Role.
         *
         * role_id será la FK que identifica al rol.
         *
         * "inverse" indica que esta columna corresponde al lado
         * de la otra entidad de la relación, en este caso Role.
         */
        inverseJoinColumns = @JoinColumn(name="role_id"),

        /**
         * Impide registrar dos veces la misma combinación:
         *
         * user_id = 1, role_id = 2
         *
         * Esto evita tener repetida la misma relación entre un usuario
         * y un rol.
         */
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "role_id"})
        }
    )
    private List<Role> roles;

    /**
     * Indica si el usuario tiene permisos de administrador.
     *
     * @Transient indica que este atributo NO se debe guardar
     * como una columna en la tabla "users".
     *
     * Es decir, Hibernate/JPA ignora este atributo al generar
     * o consultar la estructura de la base de datos.
     *
     * Por ejemplo, la tabla users puede tener:
     *
     * id
     * username
     * password
     *
     * pero NO tendrá:
     *
     * admin
     *
     * Esto permite utilizar "admin" como un dato temporal o
     * calculado dentro de la aplicación sin almacenarlo directamente
     * en la tabla.
     *
     * Importante:
     *
     * @Transient de JPA no significa que la variable desaparezca.
     * El atributo sigue existiendo normalmente en el objeto Java.
     * Simplemente JPA no lo persiste en la base de datos.
     */
    @Transient
    private boolean admin;

    /**
     * Obtiene el identificador del usuario.
     *
     * @return id del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id identificador del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return username del usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña.
     *
     * @return contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     *
     * @param password contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la lista de roles asociados al usuario.
     *
     * @return lista de roles.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Establece la lista de roles asociados al usuario.
     *
     * @param roles lista de roles.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Obtiene si el usuario tiene condición de administrador.
     *
     * Como admin es boolean, Java utiliza la convención "isAdmin()"
     * en lugar de "getAdmin()".
     *
     * @return true si es administrador; false si no.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Establece si el usuario tiene condición de administrador.
     *
     * @param admin true para marcarlo como administrador;
     *              false para indicar que no lo es.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}