package com.carlos.curso.springboot.app.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
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
     * @JsonProperty(access = WRITE_ONLY) permite recibir la contraseña
     * cuando llega un JSON, pero evita incluirla en el JSON de respuesta.
     *
     * WRITE_ONLY significa que la propiedad se puede escribir al
     * deserializar JSON, pero no se expone al serializar la respuesta.
     *
     * Importante: esta anotación no cifra la contraseña.
     * El hash debe generarse mediante PasswordEncoder, por ejemplo,
     * BCryptPasswordEncoder, antes de guardar el usuario.
     */
    @NotBlank
    @Size(min = 4, max = 12)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Relación muchos a muchos entre User y Role.
     *
     * Un usuario puede tener varios roles y un mismo rol puede
     * pertenecer a muchos usuarios.
     *
     * @ManyToMany indica el tipo de relación entre las entidades.
     */
    @ManyToMany

    /**
     * Define la tabla intermedia que relaciona usuarios y roles.
     *
     * En la base de datos tendremos:
     *
     * users
     * roles
     * users_roles ← tabla intermedia
     */
    @JoinTable(
        name = "users_roles",

        /**
         * Columna que apunta desde users_roles hacia la entidad User.
         *
         * user_id es la clave foránea que identifica al usuario.
         */
        joinColumns = @JoinColumn(name="user_id"),

        /**
         * Columna que apunta desde users_roles hacia la entidad Role.
         *
         * role_id es la clave foránea que identifica al rol.
         *
         * inverseJoinColumns representa la columna correspondiente
         * a la entidad relacionada, en este caso Role.
         */
        inverseJoinColumns = @JoinColumn(name="role_id"),

        /**
         * Impide registrar dos veces la misma combinación de usuario
         * y rol en la tabla intermedia.
         *
         * Ejemplo: user_id = 1, role_id = 2 no podrá repetirse.
         */
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "role_id"})
        }
    )
    private List<Role> roles;

    /**
     * Indica si la cuenta del usuario está habilitada.
     *
     * A diferencia de admin, este atributo no tiene @Transient,
     * por lo que JPA lo considera persistente y normalmente lo mapea
     * a una columna de la tabla "users".
     *
     * enabled = true  → cuenta habilitada.
     * enabled = false → cuenta deshabilitada.
     *
     * Este atributo por sí solo no bloquea el inicio de sesión:
     * la configuración de seguridad debe utilizarlo para comprobar
     * si la cuenta puede autenticarse.
     */
    private boolean enabled;

    /**
     * Método de ciclo de vida de JPA.
     *
     * @PrePersist indica que este método se ejecuta antes de que
     * una entidad nueva sea insertada por JPA en la base de datos.
     *
     * Aquí establecemos enabled = true para que un usuario nuevo
     * quede habilitado por defecto.
     *
     * Importante: este método se ejecuta antes de insertar una entidad,
     * no cada vez que se actualiza un usuario existente.
     */
    @PrePersist
    public void prePersist() {
        enabled = true;
    }

    /**
     * Indica si el usuario tiene permisos de administrador
     * durante el proceso de registro.
     *
     * @Transient indica que este atributo NO se guarda como columna
     * en la tabla "users".
     *
     * Sigue existiendo en el objeto Java y puede recibir un valor
     * desde una petición JSON.
     *
     * Por ejemplo, UserServiceImpl consulta user.isAdmin() para decidir
     * si agrega ROLE_ADMIN a la lista de roles.
     *
     * Importante: este booleano no concede permisos automáticamente.
     * La aplicación debe controlar quién está autorizado a asignar
     * roles administrativos; no se debe confiar en un valor admin
     * enviado libremente por el cliente.
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
     * Obtiene la contraseña desde el objeto Java.
     *
     * Aunque este método existe, @JsonProperty(WRITE_ONLY) evita
     * que Jackson incluya la propiedad password en las respuestas JSON.
     *
     * @return contraseña o hash almacenado en el objeto, según
     *         el punto del flujo en que se consulte.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña en el objeto Java.
     *
     * Normalmente recibe el valor deserializado desde el JSON.
     * Antes de persistirlo, el servicio debe convertirlo en un hash.
     *
     * @param password contraseña recibida.
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
     * Obtiene si el usuario fue marcado como administrador
     * durante el proceso de registro.
     *
     * Como admin es boolean, Java utiliza la convención isAdmin()
     * en lugar de getAdmin().
     *
     * @return true si admin está activo; false en caso contrario.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Establece la marca temporal de administrador.
     *
     * Este valor puede ser utilizado por el servicio para decidir
     * qué roles asignar al usuario.
     *
     * @param admin true para marcarlo como administrador;
     *              false para indicar que no lo es.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Indica si la cuenta está habilitada.
     *
     * @return true si la cuenta está habilitada; false si no.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Permite cambiar el estado de habilitación de la cuenta.
     *
     * @param enabled true para habilitar la cuenta;
     *                false para deshabilitarla.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}