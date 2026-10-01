package com.carlos.curso.springboot.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlos.curso.springboot.app.entities.Role;
import com.carlos.curso.springboot.app.entities.User;
import com.carlos.curso.springboot.app.repositories.RoleRepository;
import com.carlos.curso.springboot.app.repositories.UserRepository;

/**
 * Implementación del servicio de usuarios.
 *
 * @Service indica que esta clase pertenece a la capa de servicios
 * y que Spring debe registrarla como un componente administrado.
 *
 * Esta clase implementa el contrato definido por UserService.
 *
 * Flujo:
 *
 * Controller
 *     ↓
 * UserService
 *     ↓
 * UserServiceImpl
 *     ↓
 * ┌─────────────────────────────┐
 * │ UserRepository              │ → usuarios
 * │ RoleRepository              │ → roles
 * │ PasswordEncoder             │ → contraseñas
 * └─────────────────────────────┘
 *     ↓
 * Base de datos
 */
@Service
public class UserServiceImpl implements UserService {

    /*
     * Repositorio encargado de acceder a los usuarios.
     *
     * Se utiliza, por ejemplo, para:
     * - buscar usuarios.
     * - guardar usuarios.
     * - actualizar usuarios.
     * - eliminar usuarios.
     */
    private UserRepository repository;

    /*
     * Repositorio encargado de consultar los roles.
     *
     * Lo utilizamos para buscar roles como:
     * ROLE_USER
     * ROLE_ADMIN
     */
    private RoleRepository roleRepository;

    /*
     * Componente encargado de convertir una contraseña
     * original en un hash seguro.
     *
     * El PasswordEncoder fue registrado anteriormente
     * como @Bean en SpringSecurityConfig.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias.
     *
     * Spring utiliza este constructor para proporcionar
     * automáticamente las dependencias que necesita el servicio.
     *
     * @param repository repositorio para trabajar con usuarios.
     * @param roleRepository repositorio para consultar roles.
     * @param passwordEncoder componente para cifrar/hash de contraseñas.
     *
     * Flujo:
     *
     * Spring
     *   ↓
     * crea UserServiceImpl
     *   ↓
     * proporciona UserRepository
     * proporciona RoleRepository
     * proporciona PasswordEncoder
     *   ↓
     * UserServiceImpl queda listo para trabajar.
     */
    public UserServiceImpl(
            UserRepository repository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene todos los usuarios.
     *
     * @Transactional(readOnly = true) indica que esta operación
     * solamente realiza lectura de datos.
     *
     * readOnly = true ayuda a indicar a Spring/JPA que dentro
     * de esta transacción no esperamos realizar modificaciones.
     *
     * @return lista de usuarios.
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {

        /*
         * repository.findAll() obtiene todos los usuarios.
         *
         * CrudRepository.findAll() devuelve un Iterable<User>,
         * por eso aquí se realiza el cast a List<User>.
         */
        return (List<User>) repository.findAll();
    }

    /**
     * Guarda un nuevo usuario.
     *
     * Antes de guardar:
     *
     * 1. Busca el rol ROLE_USER.
     * 2. Si el usuario es administrador, también busca ROLE_ADMIN.
     * 3. Asigna los roles al usuario.
     * 4. Convierte la contraseña en un hash mediante BCrypt.
     * 5. Guarda el usuario en la base de datos.
     *
     * @param user usuario que se desea guardar.
     * @return usuario guardado.
     */
    @Override
    @Transactional
    public User save(User user) {

        /*
         * Buscamos el rol ROLE_USER en la base de datos.
         *
         * findByName() devuelve Optional<Role> porque el rol
         * podría existir o no existir.
         *
         * Ejemplo:
         *
         * ROLE_USER existe
         *     ↓
         * Optional contiene Role
         *
         * ROLE_USER no existe
         *     ↓
         * Optional vacío
         */
        Optional<Role> optionaRoleUser =
                roleRepository.findByName("ROLE_USER");

        /*
         * Creamos una lista donde almacenaremos los roles
         * que tendrá el nuevo usuario.
         */
        List<Role> roles = new ArrayList<>();

        /*
         * ifPresent() ejecuta roles::add únicamente si
         * Optional contiene un Role.
         *
         * roles::add es una referencia al método:
         *
         * roles.add(role)
         *
         * Por lo tanto:
         *
         * ROLE_USER encontrado
         *      ↓
         * se agrega a la lista roles.
         */
        optionaRoleUser.ifPresent(roles::add);

        /*
         * isAdmin() devuelve el valor del atributo boolean
         * "admin" del usuario.
         *
         * Este atributo está marcado con @Transient en la entidad User,
         * por lo que sirve como información temporal y no se guarda
         * directamente como una columna en la tabla users.
         */
        if (user.isAdmin()) {

            /*
             * Si el usuario fue marcado como administrador,
             * buscamos también el rol ROLE_ADMIN.
             */
            Optional<Role> optionaRoleAdmin =
                    roleRepository.findByName("ROLE_ADMIN");

            /*
             * Si ROLE_ADMIN existe, lo agregamos a la lista.
             */
            optionaRoleAdmin.ifPresent(roles::add);
        }

        /*
         * Asignamos al usuario la lista final de roles.
         *
         * Ejemplo:
         *
         * Usuario normal:
         * roles = [ROLE_USER]
         *
         * Administrador:
         * roles = [ROLE_USER, ROLE_ADMIN]
         */
        user.setRoles(roles);

        /*
         * La contraseña NO debe guardarse directamente.
         *
         * Antes:
         *
         * user.getPassword()
         *       ↓
         * "123456"
         *
         * Después:
         *
         * passwordEncoder.encode(...)
         *       ↓
         * "$2a$10$..."
         *
         * El resultado es un hash generado mediante BCrypt.
         */
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        /*
         * Finalmente guardamos el usuario mediante el repositorio.
         *
         * repository.save(user)
         *       ↓
         * UserRepository
         *       ↓
         * Base de datos
         *
         * El usuario guardado se devuelve como resultado.
         */
        return repository.save(user);
    }
}