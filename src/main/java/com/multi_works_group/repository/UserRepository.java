// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.repository;

import com.multi_works_group.model.User;
import com.multi_works_group.util.DatabaseConnection;
import java.sql.*;

public class UserRepository {

    // Método para buscar usuario por username
    public User findByUsername(String username) {
        System.out.println("[DEBUG] Buscando usuario: " + username);
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setRelatedId(rs.getInt("related_id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para guardar usuario
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, related_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getRelatedId());

            pstmt.executeUpdate();
        }
    }
}
