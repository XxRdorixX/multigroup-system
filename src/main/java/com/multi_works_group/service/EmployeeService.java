// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.service;

import com.multi_works_group.model.Employee;
import com.multi_works_group.repository.EmployeeRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EmployeeService {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    // Método que crea un empleado
    public void create(Employee employee) {
        try {
            employeeRepository.save(employee); // Llama al repositorio
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear empleado", e);
            throw new RuntimeException("Error en la base de datos: " + e.getMessage());
        }
    }

    // Método que muestra lista de empleados Activados
    public List<Employee> findAllActive() {
        try {
            return employeeRepository.findAll().stream()
                    .filter(e -> "ACTIVE".equals(e.getStatus()))
                    .collect(Collectors.toList()); // Ahora funciona
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener empleados activos", e);
            return List.of();
        }
    }

    // Método para actualizar empleado
    public void update(Employee employee) {
        try {
            employeeRepository.update(employee);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar empleado", e);
            throw new RuntimeException("Error al actualizar: " + e.getMessage());
        }
    }

    // Método para desactivar empleado
    public void deactivate(Long id) {
        try {
            employeeRepository.deactivate(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al desactivar empleado", e);
            throw new RuntimeException("Error al desactivar: " + e.getMessage());
        }
    }

    // Método para buscar por ID
    public Employee findById(Long id) {
        try {
            return employeeRepository.findById(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar empleado ID: " + id, e);
            return null;
        }
    }

    // Método para listar empleados
    public List<Employee> findAll() {
        try {
            return employeeRepository.findAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener empleados", e);
            return List.of();
        }
    }

}