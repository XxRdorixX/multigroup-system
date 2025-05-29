// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.service;

import com.multi_works_group.model.User;
import com.multi_works_group.repository.UserRepository;
import java.sql.SQLException;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    // Autentifica un usuario con credenciales
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Asegúrate de que el rol se obtenga desde la base de datos
            System.out.println("Rol del usuario: " + user.getRole());
            return user;
        }
        return null;
    }

    // crea un nuevo usuario al sistema
    public void create(User user) {
        try {
            userRepository.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }
}