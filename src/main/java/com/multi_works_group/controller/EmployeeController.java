// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.controller;

import com.multi_works_group.model.Employee;
import com.multi_works_group.model.User;
import com.multi_works_group.service.EmployeeService;
import com.multi_works_group.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Controlador para gestionar operaciones CRUD de empleados
@WebServlet("/employees/*") // Mapeado a rutas bajo /employees
public class EmployeeController extends HttpServlet {
    // Servicios para operaciones de negocio
    private final EmployeeService employeeService = new EmployeeService();
    private final UserService userService = new UserService();

    // Manejo de solicitudes GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determina la acción a partir de la ruta
        String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";

        // Router de acciones
        switch (action) {
            case "/new":    // Muestra formulario creación
                showNewForm(request, response);
                break;
            case "/edit":   // Muestra formulario edición
                showEditForm(request, response);
                break;
            case "/list":   // Lista empleados
                listEmployees(request, response);
                break;
            case "/delete": // Elimina (desactiva) empleado
                deleteEmployee(request, response);
                break;
            default:        // Ruta no reconocida
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Manejo de solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determina acción desde la URL
        String action = request.getPathInfo();

        switch (action) {
            case "/create": // Crea nuevo empleado
                createEmployee(request, response);
                break;
            case "/update": // Actualiza empleado
                updateEmployee(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // =============== MÉTODOS AUXILIARES ===============

    // Muestra formulario de creación
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/employees/new-employee.jsp").forward(request, response);
    }

    // Muestra formulario de edición
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Employee employee = employeeService.findById(id);

        if (employee != null) {
            request.setAttribute("employee", employee);  // Envía empleado a la vista
            request.getRequestDispatcher("/views/employees/edit.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // Empleado no existe
        }
    }

    // Lista todos los empleados
    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employees = employeeService.findAll();
        request.setAttribute("employees", employees);  // Envía lista a la vista
        request.getRequestDispatcher("/views/employees/list.jsp").forward(request, response);
    }

    // Desactiva empleado (eliminación lógica)
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        employeeService.deactivate(id); // Cambia estado en lugar de borrar
        response.sendRedirect(request.getContextPath() + "/employees/list"); // Recarga lista
    }

    // Crea nuevo empleado y usuario asociado
    private void createEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Crear entidad Empleado
            Employee employee = mapRequestToEmployee(request);
            employeeService.create(employee);

            // Crear al  usuario asociado
            User user = new User();
            user.setUsername(request.getParameter("username")); // Credenciales
            user.setPassword(request.getParameter("password"));  // (debería encriptarse)
            user.setRole("EMPLOYEE"); // Rol fijo para empleados
            user.setRelatedId(employee.getId().intValue()); // Vincula usuario con empleado

            userService.create(user);

            // Redirigir a lista
            response.sendRedirect(request.getContextPath() + "/employees/list");

        } catch (Exception e) {
            // Manejo centralizado de errores
            throw new ServletException("Error en creación de empleado: " + e.getMessage(), e);
        }
    }

    // Actualiza empleado existente
    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Employee existingEmployee = employeeService.findById(id);

        if (existingEmployee != null) {
            // Actualiza datos desde formulario
            updateEmployeeFromRequest(existingEmployee, request);
            employeeService.update(existingEmployee);
            response.sendRedirect(request.getContextPath() + "/employees/list");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // Empleado no existe
        }
    }

    // =============== UTILIDADES ===============

    // Mapea parámetros del request a objeto Employee
    private Employee mapRequestToEmployee(HttpServletRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getParameter("name"));
        employee.setDocument(request.getParameter("document"));
        employee.setType(request.getParameter("type"));
        employee.setContractType(request.getParameter("contractType"));
        employee.setPhone(request.getParameter("phone"));
        employee.setEmail(request.getParameter("email"));
        employee.setAddress(request.getParameter("address"));
        return employee;
    }

    // Actualiza objeto Employee con datos del formulario
    private void updateEmployeeFromRequest(Employee employee, HttpServletRequest request) {
        employee.setName(request.getParameter("name"));
        employee.setDocument(request.getParameter("document"));
        employee.setType(request.getParameter("type"));
        employee.setContractType(request.getParameter("contractType"));
        employee.setPhone(request.getParameter("phone"));
        employee.setEmail(request.getParameter("email"));
        employee.setAddress(request.getParameter("address"));
    }
}