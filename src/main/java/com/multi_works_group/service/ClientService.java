// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.service;

import com.multi_works_group.exceptions.ServiceException;
import com.multi_works_group.model.Client;
import com.multi_works_group.repository.ClientRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService {
    // Repositorio JDBC para operaciones de base de datos
    private final ClientRepository clientRepository = new ClientRepository();
    // Logger para registro de errores
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());

     // Método que crea un cliente
    public void create(Client client) {
        try {
            clientRepository.save(client);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear cliente", e);
            throw new ServiceException("Error en base de datos", e);
        }
    }

    // Método que verifica si hay un cliente copn un documento dado
    public boolean existsByDocument(String document) {
        try {
            return clientRepository.existsByDocument(document);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar documento", e);
            throw new ServiceException("Error de verificación", e);
        }
    }

    // Método que muestra lista completa de clientes

    public List<Client> findAll() {
        try {
            return clientRepository.findAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener clientes", e);
            throw new ServiceException("Error al listar clientes", e);
        }
    }


    // Método que busca un cliente por id
    public Optional<Client> findById(Long id) {
        try {
            // Usa Optional para manejar posibles valores nulos
            return Optional.ofNullable(clientRepository.findById(id));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar cliente ID: " + id, e);
            return Optional.empty();  // Retorna Optional vacío en caso de error
        }
    }

    // Método para actualizar empleado
    public void update(Client client) {
        try {
            clientRepository.update(client);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar cliente", e);
            throw new ServiceException("Error al actualizar", e);
        }
    }

    // Método para desactivar empleado
    public void deactivate(Long id) {
        try {
            clientRepository.deactivate(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al desactivar cliente", e);
            throw new ServiceException("Error al desactivar", e);
        }
    }
}