// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.controller;

import com.multi_works_group.model.User;
import com.multi_works_group.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Servicio de autenticación para usuarios del sistema
@WebServlet("/login") // Mapeado a la ruta /login
public class LoginServlet extends HttpServlet {
    // Servicio para operaciones de usuario (autenticación, CRUD)
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Obtener credenciales del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.authenticate(username, password);

        if (user != null) {
            // Crear sesión de usuario
            HttpSession session = request.getSession(); // Obtiene o crea sesión
            session.setAttribute("user", user); // Almacena objeto usuario en sesión

            // Mensaje de depuración (DEBUG)
            System.out.println("[DEBUG] Redirigiendo a: " + request.getContextPath() + "/views/employees/dashboard.jsp");

            // Redirección basada en roles
            switch (user.getRole()) {
                case "ADMIN":
                    response.sendRedirect(request.getContextPath() + "/views/admin/dashboard.jsp");
                    break;

                case "EMPLOYEE":
                    response.sendRedirect(request.getContextPath() + "/views/employees/dashboard.jsp");
                    break;

                default:
                    // Rol no reconocido - vuelve a login con error
                    response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            }
        } else {
            // En que caso que falle la Autenticación
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }
}