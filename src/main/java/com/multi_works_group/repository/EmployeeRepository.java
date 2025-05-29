// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.repository;

import com.multi_works_group.model.Employee;
import com.multi_works_group.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    /**
     * GUARDA UN NUEVO EMPLEADO EN LA BASE DE DATOS.
     * @param employee Objeto empleado a persistir
     */
    public void save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (name, document, type, contract_type, phone, email, address, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection conn = DatabaseConnection.getConnection();
             // Recuperar ID generado automáticamente
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Establecer parámetros desde el objeto employee
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDocument());
            pstmt.setString(3, employee.getType());
            pstmt.setString(4, employee.getContractType());
            pstmt.setString(5, employee.getPhone());
            pstmt.setString(6, employee.getEmail());
            pstmt.setString(7, employee.getAddress());
            pstmt.executeUpdate();

            // Asignar ID generado al objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) employee.setId(rs.getLong(1));
            }
        }
    }

    /**
     * ACTUALIZA LOS DATOS DE UN EMPLEADO EXISTENTE.
     * @param employee Objeto con datos actualizados
     */
    public void update(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET name = ?, document = ?, type = ?, contract_type = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Parámetros de actualización
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDocument());
            pstmt.setString(3, employee.getType());
            pstmt.setString(4, employee.getContractType());
            pstmt.setString(5, employee.getPhone());
            pstmt.setString(6, employee.getEmail());
            pstmt.setString(7, employee.getAddress());
            pstmt.setLong(8, employee.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * DESACTIVA UN EMPLEADO (BORRADO LÓGICO).
     * @param id ID del empleado a desactivar
     */
    public void deactivate(Long id) throws SQLException {
        String sql = "UPDATE employees SET status = 'INACTIVE', inactivated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * BUSCA UN EMPLEADO POR SU ID.
     * @param id Identificador único del empleado
     * @return Objeto Employee o null si no existe
     */
    public Employee findById(Long id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapResultSetToEmployee(rs) : null;
            }
        }
    }

    /**
     * OBTIENE TODOS LOS EMPLEADOS (ACTIVOS E INACTIVOS).
     * @return Lista completa de empleados
     */
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) employees.add(mapResultSetToEmployee(rs));
        }
        return employees;
    }

    // --- MÉTODOS AUXILIARES ---

    /**
     * MAPEA UN ResultSet A OBJETO EMPLOYEE.
     * @param rs ResultSet con datos de BD
     * @return Objeto Employee poblado
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setDocument(rs.getString("document"));
        employee.setType(rs.getString("type"));
        employee.setContractType(rs.getString("contract_type")); // Mapea columna contract_type
        employee.setPhone(rs.getString("phone"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs.getString("address"));
        employee.setStatus(rs.getString("status"));
        return employee;
    }
}