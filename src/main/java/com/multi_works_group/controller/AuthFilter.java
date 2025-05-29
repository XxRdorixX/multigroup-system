// Autor: Rodrigo Escobar Fecha: 26/5/2022

package com.multi_works_group.controller;

import com.multi_works_group.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Filtro de autenticación y autorización que se aplica a TODAS las solicitudes de la aplicación
@WebFilter("/*") // Intercepta todas las rutas ("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // Convertir solicitud/respuesta genéricas a HTTP
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Obtener la ruta solicitada sin el contexto de la aplicación
        // Ejemplo: De "/mi-app/admin/dashboard" extrae "/admin/dashboard"
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // Rutas públicas (no requieren autenticación)
        if (path.startsWith("/login") ||      // Páginas de login
                path.startsWith("/logout") ||     // Páginas de logout
                path.startsWith("/css/") ||       // Recursos CSS
                path.startsWith("/js/")) {        // Recursos JavaScript

            // Permite el acceso sin verificación
            chain.doFilter(request, response);
            return; // Termina el procesamiento del filtro para estas rutas
        }

        // Verificar autenticación del usuario
        HttpSession session = request.getSession(false); // Obtener sesión existente sin crear una nueva
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Redirigir a login si no está autenticado
        if (!isLoggedIn) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return; // Bloquea el acceso
        }

        //  Obtener información del usuario logueado
        User user = (User) session.getAttribute("user");
        String role = user.getRole(); // Ej: "ADMIN", "EMPLOYEE"

        // 7. Control de acceso basado en roles (Autorización)
        if (role.equals("EMPLOYEE")) {
            // Restricciones específicas para empleados:
            if (path.startsWith("/admin") ||          // Rutas de administración
                    path.startsWith("/employees/edit")) { // Edición de empleados

                // Acceso prohibido (error 403)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return; // Bloquea el acceso
            }
        }

        // Si pasa todas las validaciones, permite continuar con la solicitud
        chain.doFilter(request, response);
    }
}