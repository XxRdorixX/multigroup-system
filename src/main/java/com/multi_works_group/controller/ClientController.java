// Autor: Rodrigo Escobar Fecha: 26/5/2022

package com.multi_works_group.controller;

import com.multi_works_group.exceptions.ServiceException;
import com.multi_works_group.model.Client;
import com.multi_works_group.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

// Controlador para gestionar todas las operaciones CRUD de clientes
@WebServlet("/clients/*") // Mapeado a la ruta base
public class ClientController extends HttpServlet {
    // Servicio para operaciones de negocio con clientes
    private final ClientService clientService = new ClientService();

    // Logger para registrar eventos y errores
    private static final Logger logger = Logger.getLogger(ClientController.class.getName());

    // Manejo de solicitudes GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extrae la acción de la URL (ej: /new, /edit, /list)
        String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";

        try {
            // Router de acciones
            switch (action) {
                case "/new":   // Mostrar formulario de creación
                    showNewForm(request, response);
                    break;
                case "/edit":  // Mostrar formulario de edición
                    showEditForm(request, response);
                    break;
                case "/list":  // Listar todos los clientes
                    listClients(request, response);
                    break;
                case "/delete": // Eliminar cliente (desactivar)
                    deleteClient(request, response);
                    break;
                default:       // Ruta no reconocida
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ServiceException e) {
            // Manejo de excepciones del servicio
            handleServiceException(response, e);
        } catch (NumberFormatException e) {
            // Error en formato de ID
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        }
    }

    // Manejo de solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Acción desde la URL
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/create": // Crear nuevo cliente
                    createClient(request, response);
                    break;
                case "/update": // Actualizar cliente existente
                    updateClient(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ServiceException e) {
            handleServiceException(response, e);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    // Muestra formulario para crear nuevo cliente
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Despacha a la vista JSP
        request.getRequestDispatcher("/views/clients/new.jsp").forward(request, response);
    }

    // Muestra formulario de edición para cliente existente
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ServiceException {
        // Obtiene ID del parámetro de solicitud
        Long id = parseId(request);

        // Busca cliente y maneja ambos casos (existente/no existente)
        clientService.findById(id).ifPresentOrElse(
                client -> {
                    try {
                        // Muestra formulario con datos del cliente
                        forwardWithClient(request, response, client, "/views/clients/edit.jsp");
                    } catch (ServletException | IOException e) {
                        // Manejo de errores al cargar la vista
                        try {
                            logger.severe("Error al mostrar formulario de edición: " + e.getMessage());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno al cargar la vista");
                        } catch (IOException ex) {
                            logger.severe("Error crítico: " + ex.getMessage());
                        }
                    }
                },
                () -> {
                    try {
                        // Cliente no encontrado
                        sendError(response, "Cliente no encontrado", HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException e) {
                        logger.severe("Error enviando respuesta: " + e.getMessage());
                    }
                }
        );
    }

    // Lista todos los clientes activos
    private void listClients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtiene lista de clientes desde el servicio
        List<Client> clients = clientService.findAll();
        // Envía datos a la vista
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/views/clients/list.jsp").forward(request, response);
    }

    // Desactiva un cliente (eliminación lógica)
    private void deleteClient(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, IOException {
        Long id = parseId(request);
        clientService.deactivate(id); // Cambia estado en lugar de borrar físicamente
        redirectToList(response, request);
    }

    // Crea un nuevo cliente
    private void createClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ServiceException {
        String document = request.getParameter("document");

        // Validación: documento único
        if (clientService.existsByDocument(document)) {
            request.setAttribute("error", "El documento ya está registrado");
            showNewForm(request, response); // Muestra formulario con error
        } else {
            // Mapea datos del formulario a objeto Client
            Client client = mapRequestToClient(request);
            clientService.create(client);
            redirectToList(response, request);
        }
    }

    // Actualiza cliente existente
    private void updateClient(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, IOException {
        Long id = parseId(request);

        clientService.findById(id).ifPresentOrElse(
                client -> {
                    try {
                        // Actualiza datos desde el formulario
                        updateClientFromRequest(client, request);
                        clientService.update(client);
                        redirectToList(response, request);
                    } catch (IOException e) {
                        try {
                            logger.severe("Error en redirección: " + e.getMessage());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al redirigir");
                        } catch (IOException ex) {
                            logger.severe("Fallo crítico: " + ex.getMessage());
                        }
                    }
                },
                () -> {
                    try {
                        sendError(response, "Cliente no encontrado", HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException e) {
                        logger.severe("Error enviando respuesta: " + e.getMessage());
                    }
                }
        );
    }

    // ==================== HELPERS ====================

    // Parsea ID de cliente desde parámetro de solicitud
    private Long parseId(HttpServletRequest request) throws NumberFormatException {
        return Long.parseLong(request.getParameter("id"));
    }

    // Mapea parámetros del request a objeto Client
    private Client mapRequestToClient(HttpServletRequest request) {
        Client client = new Client();
        client.setName(request.getParameter("name"));
        client.setDocument(request.getParameter("document"));
        client.setType(request.getParameter("type"));
        client.setPhone(request.getParameter("phone"));
        client.setEmail(request.getParameter("email"));
        client.setAddress(request.getParameter("address"));
        return client;
    }

    // Redirige a la lista de clientes
    private void redirectToList(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect(request.getContextPath() + "/clients/list");
    }

    // Actualiza objeto Client con datos del formulario
    private void updateClientFromRequest(Client client, HttpServletRequest request) {
        client.setName(request.getParameter("name"));
        client.setDocument(request.getParameter("document"));
        client.setType(request.getParameter("type"));
        client.setPhone(request.getParameter("phone"));
        client.setEmail(request.getParameter("email"));
        client.setAddress(request.getParameter("address"));
    }

    // Envía cliente a una vista JSP específica
    private void forwardWithClient(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Client client,
                                   String view) throws ServletException, IOException {
        request.setAttribute("client", client);
        request.getRequestDispatcher(view).forward(request, response);
    }

    // Envía error HTTP con mensaje personalizado
    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.sendError(status, message);
    }

    // Maneja excepciones del servicio enviando error 500
    private void handleServiceException(HttpServletResponse response, ServiceException e) throws IOException {
        logger.severe("Error en servicio: " + e.getMessage());
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
}