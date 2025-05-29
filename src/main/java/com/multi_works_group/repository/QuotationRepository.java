// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.repository;

import com.multi_works_group.model.Quotation;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import com.multi_works_group.util.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.multi_works_group.model.Client;

public class QuotationRepository {

    /**
     * GUARDA UNA NUEVA COTIZACIÓN EN LA BASE DE DATOS.
     * @param quotation Objeto cotización a persistir
     */
    public void save(Quotation quotation) throws SQLException {
        // SQL con parámetros para inserción
        String sql = "INSERT INTO quotations (client_id, tentative_start_date, tentative_end_date, total_hours, assignment_cost, additional_costs, total, created_by, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             // Recuperar ID generado automáticamente
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Establecer parámetros desde el objeto quotation
            pstmt.setLong(1, quotation.getClient().getId());  // ID del cliente asociado
            pstmt.setDate(2, new java.sql.Date(quotation.getTentativeStartDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(quotation.getTentativeEndDate().getTime()));
            pstmt.setInt(4, quotation.getTotalHours());
            pstmt.setDouble(5, quotation.getAssignmentCost());
            pstmt.setDouble(6, quotation.getAdditionalCosts());
            pstmt.setDouble(7, quotation.getTotal());
            pstmt.setInt(8, quotation.getCreatedBy());  // ID del usuario creador
            pstmt.setTimestamp(9, new java.sql.Timestamp(quotation.getCreatedAt().getTime()));

            pstmt.executeUpdate();

            // Recuperar y asignar ID generado
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) quotation.setId(rs.getLong(1));
            }
        }
    }

    /**
     * BUSCA UNA COTIZACIÓN POR SU ID (NO IMPLEMENTADO).
     * @param id Identificador único de la cotización
     * @return null (pendiente de implementación)
     */
    public Quotation findById(Long id) throws SQLException {
        // TODO: Implementar lógica de búsqueda por ID
        return null;
    }

    /**
     * OBTIENE TODAS LAS COTIZACIONES CON INFORMACIÓN DEL CLIENTE.
     * @return Lista completa de cotizaciones
     */
    public List<Quotation> findAll() throws SQLException {
        // Consulta con JOIN para obtener nombre del cliente
        String sql = "SELECT q.*, c.name AS client_name FROM quotations q " +
                "INNER JOIN clients c ON q.client_id = c.id";

        List<Quotation> quotations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Mapear cada fila del ResultSet a objeto Quotation
            while (rs.next()) {
                Quotation quotation = new Quotation();
                quotation.setId(rs.getLong("id"));
                quotation.setTentativeStartDate(rs.getDate("tentative_start_date"));
                quotation.setTentativeEndDate(rs.getDate("tentative_end_date"));

                // Campos numéricos y de costo
                quotation.setTotalHours(rs.getInt("total_hours"));
                quotation.setAssignmentCost(rs.getDouble("assignment_cost"));
                quotation.setAdditionalCosts(rs.getDouble("additional_costs"));
                quotation.setTotal(rs.getDouble("total"));

                // Construir objeto Client mínimo (solo con nombre)
                Client client = new Client();
                client.setName(rs.getString("client_name"));
                quotation.setClient(client);

                quotations.add(quotation);
            }
        }
        return quotations;
    }
}