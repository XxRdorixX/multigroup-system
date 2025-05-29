// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.repository;

import com.multi_works_group.model.Client;
import com.multi_works_group.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientRepository {
    // Logger para capturar errores y eventos
    private static final Logger logger = Logger.getLogger(ClientRepository.class.getName());

    /**
     * GUARDA UN NUEVO CLIENTE EN LA BASE DE DATOS.
     * @param client Objeto cliente a persistir
     */
    public void save(Client client) throws SQLException {
        // SQL con parámetros y estado predeterminado 'ACTIVE'
        String sql = "INSERT INTO clients (name, document, type, phone, email, address, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection conn = DatabaseConnection.getConnection();
             // Preparar statement con capacidad para recuperar IDs generados
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Establecer parámetros desde el objeto client
            setClientParameters(pstmt, client);
            pstmt.executeUpdate();

            // Recuperar ID generado automáticamente
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) client.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar cliente", e);
            throw e; // Relanzar excepción para manejo superior
        }
    }

    /**
     * OBTIENE TODOS LOS CLIENTES ACTIVOS E INACTIVOS.
     * @return Lista completa de clientes
     */
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients"; // Consulta sin filtros

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Mapear cada fila del ResultSet a objeto Client
            while (rs.next()) clients.add(mapResultSetToClient(rs));
        }
        return clients;
    }

    /**
     * BUSCA UN CLIENTE POR SU ID.
     * @param id Identificador único del cliente
     * @return Objeto Client o null si no existe
     */
    public Client findById(Long id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapResultSetToClient(rs) : null;
            }
        }
    }

    /**
     * VERIFICA SI EXISTE UN CLIENTE CON UN DOCUMENTO ESPECÍFICO.
     * @param document Número de documento a verificar
     * @return true si existe, false si no
     */
    public boolean existsByDocument(String document) throws SQLException {
        String sql = "SELECT COUNT(id) FROM clients WHERE document = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, document);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * ACTUALIZA LOS DATOS DE UN CLIENTE EXISTENTE.
     * @param client Objeto con datos actualizados
     */
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, document = ?, type = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setClientParameters(pstmt, client);
            pstmt.setLong(7, client.getId()); // ID como último parámetro
            pstmt.executeUpdate();
        }
    }

    /**
     * DESACTIVA UN CLIENTE (BORRADO LÓGICO).
     * @param id Identificador del cliente a desactivar
     */
    public void deactivate(Long id) throws SQLException {
        // Actualiza estado y registra timestamp de desactivación
        String sql = "UPDATE clients SET status = 'INACTIVE', inactivated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    // --- MÉTODOS PRIVADOS DE APOYO ---

    /**
     * ESTABLECE PARÁMETROS COMUNES EN PreparedStatement.
     * @param pstmt Statement a configurar
     * @param client Fuente de datos
     */
    private void setClientParameters(PreparedStatement pstmt, Client client) throws SQLException {
        pstmt.setString(1, client.getName());
        pstmt.setString(2, client.getDocument());
        pstmt.setString(3, client.getType());
        pstmt.setString(4, client.getPhone());
        pstmt.setString(5, client.getEmail());
        pstmt.setString(6, client.getAddress());
    }

    /**
     * MAPEA UN ResultSet A OBJETO CLIENT.
     * @param rs ResultSet con datos de BD
     * @return Objeto Client poblado
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setDocument(rs.getString("document"));
        client.setType(rs.getString("type"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setAddress(rs.getString("address"));
        client.setStatus(rs.getString("status"));
        return client;
    }
}