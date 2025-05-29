// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Servicio para cerrar sesión de usuarios
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Obtener sesión actual (sin crear nueva)
        HttpSession session = request.getSession(false);

        // Invalidar sesión existente
        if (session != null) {
            session.invalidate(); // Destruye completamente la sesión
            System.out.println("[DEBUG] Sesión cerrada correctamente");
        }

        // Redirigir a página de login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}