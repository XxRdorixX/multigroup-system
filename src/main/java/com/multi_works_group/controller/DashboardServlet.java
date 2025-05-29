// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import com.multi_works_group.model.User;

// Controlador para redirigir al panel de control (dashboard) según el rol del usuario
@WebServlet("/dashboard") // Mapeado a la ruta /dashboard
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Verificar sesión activa
        HttpSession session = request.getSession(false); // Obtiene sesión existente sin crear nueva

        // Comprobar autenticación
        if (session == null || session.getAttribute("user") == null) {
            // Si no hay sesión o usuario, redirige a login
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return; // Corta ejecución
        }

        // Obtener información del usuario logueado
        User user = (User) session.getAttribute("user");
        String role = user.getRole().toUpperCase(); // Normaliza rol a mayúsculas (ej: "ADMIN" o "EMPLOYEE")

        // Registro de depuración (DEBUG)
        System.out.println("[DEBUG] Rol detectado: " + role);
        System.out.println("[DEBUG] Ruta base: " + request.getContextPath());

        // Redirección basada en roles
        switch (role) {
            case "ADMIN":
                // Panel de administración
                response.sendRedirect(request.getContextPath() + "/views/admin/dashboard.jsp");
                System.out.println("[DEBUG] Redirigiendo a ADMIN dashboard");
                break;

            case "EMPLOYEE":
                // Panel de empleado
                response.sendRedirect(request.getContextPath() + "/views/employees/dashboard.jsp");
                System.out.println("[DEBUG] Redirigiendo a EMPLOYEE dashboard");
                break;

            default:
                // Rol no reconocido
                System.out.println("[ERROR] Rol no reconocido: " + role);
                response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}