// Autor: Rodrigo Escobar Fecha: 27/5/2022
package com.multi_works_group.controller;

import com.multi_works_group.exceptions.ServiceException;
import com.multi_works_group.model.Client;
import com.multi_works_group.model.Employee;
import com.multi_works_group.model.Quotation;
import com.multi_works_group.service.ClientService;
import com.multi_works_group.service.EmployeeService;
import com.multi_works_group.service.QuotationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Controlador para gestionar operaciones relacionadas con cotizaciones
@WebServlet("/quotations/*") // Mapeado a rutas bajo /quotations
public class QuotationController extends HttpServlet {
    // Servicios para operaciones de negocio
    private final QuotationService quotationService = new QuotationService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    // Formateador de fechas para conversión
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Manejo de solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determina acción desde la URL (default: /create)
        String action = request.getPathInfo() != null ? request.getPathInfo() : "/create";

        switch (action) {
            case "/create": // Crear nueva cotización
                createQuotation(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Crear nueva cotización
    private void createQuotation(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, IOException {

        // Obtener fechas del formulario
        String startDateString = request.getParameter("startDate");
        String endDateString = request.getParameter("endDate");

        // Validar campos obligatorios
        if (startDateString == null || startDateString.trim().isEmpty()) {
            throw new ServiceException("La fecha de inicio es requerida.");
        }
        if (endDateString == null || endDateString.trim().isEmpty()) {
            throw new ServiceException("La fecha de fin es requerida.");
        }

        try {
            Quotation quotation = new Quotation();

            // Parsear fechas a objetos Date
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            // Obtener y validar cliente
            Long clientId = Long.parseLong(request.getParameter("clientId"));
            Client client = clientService.findById(clientId)
                    .orElseThrow(() -> new ServiceException("Cliente no encontrado"));

            // Configurar entidad cotización
            quotation.setClient(client);
            quotation.setTentativeStartDate(startDate);
            quotation.setTentativeEndDate(endDate);
            quotation.setCreatedAt(new Date()); // Fecha actual
            quotation.setCreatedBy(1); // ID de usuario hardcodeado (temporal)

            // Calcular totales basados en actividades
            quotation.calculateTotals();

            // Guardar en base de datos
            quotationService.save(quotation);

            // Redirigir a listado
            response.sendRedirect(request.getContextPath() + "/quotations/list");

        } catch (ParseException e) {
            throw new ServiceException("Formato de fecha inválido");
        }
    }

    // Manejo de solicitudes GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Determinar acción desde URL (default: /list)
            String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";

            switch (action) {
                case "/new": // Mostrar formulario de creación
                    showNewForm(request, response);
                    break;
                case "/list": // Listar cotizaciones
                    listQuotations(request, response);
                    break;
                case "/details": // Ver detalles de cotización
                    showDetails(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    // Mostrar formulario para nueva cotización
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener datos necesarios para el formulario
        List<Client> clients = clientService.findAll();
        List<Employee> employees = employeeService.findAllActive();

        // Enviar datos a la vista
        request.setAttribute("clients", clients);
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/views/quotations/new.jsp").forward(request, response);
    }

    // Listar todas las cotizaciones
    private void listQuotations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener cotizaciones desde servicio
        List<Quotation> quotations = quotationService.findAll();

        // Enviar datos a la vista
        request.setAttribute("quotations", quotations);
        request.getRequestDispatcher("/views/quotations/list.jsp").forward(request, response);
    }

    // Mostrar detalles de una cotización específica
    private void showDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener ID de parámetro
            Long id = Long.parseLong(request.getParameter("id"));

            // Buscar cotización
            Quotation quotation = quotationService.findById(id)
                    .orElseThrow(() -> new ServiceException("Cotización no encontrada"));

            // Enviar a vista de detalles
            request.setAttribute("quotation", quotation);
            request.getRequestDispatcher("/views/quotations/details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}