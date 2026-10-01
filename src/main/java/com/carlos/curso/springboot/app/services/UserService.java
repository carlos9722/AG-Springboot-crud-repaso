package com.carlos.curso.springboot.app.services;

import java.util.List;

import com.carlos.curso.springboot.app.entities.User;

/**
 * Contrato de la capa de servicios para trabajar con usuarios.
 *
 * La interfaz define QUÉ operaciones puede realizar el servicio,
 * pero no define CÓMO se realizan.
 *
 * La implementación concreta (por ejemplo, UserServiceImpl)
 * será la encargada de implementar la lógica.
 *
 * Flujo habitual:
 *
 * Controller
 *     ↓
 * UserService
 *     ↓
 * UserServiceImpl
 *     ↓
 * UserRepository
 *     ↓
 * Base de datos
 */
public interface UserService {

    /**
     * Obtiene todos los usuarios.
     *
     * @return lista de usuarios encontrados.
     */
    List<User> findAll();

    /**
     * Guarda un usuario.
     *
     * El objeto User contiene los datos que queremos guardar.
     * La implementación del servicio normalmente delegará
     * la persistencia al UserRepository.
     *
     * @param user usuario que se desea guardar.
     * @return usuario guardado, normalmente incluyendo el ID
     *         generado por la base de datos.
     */
    User save(User user);
}