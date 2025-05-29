<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Lista de Cotizaciones</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    body {
      font-family: 'Segoe UI', Arial, sans-serif;
      background-color: #f8f9fa;
      padding: 25px;
      margin: 0;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
    }

    h1 {
      color: #2c3e50;
      border-bottom: 3px solid #3f51b5;
      padding-bottom: 10px;
      margin-bottom: 25px;
    }

    .action-buttons {
      margin-bottom: 25px;
    }

    .btn {
      padding: 10px 20px;
      border-radius: 5px;
      text-decoration: none;
      transition: all 0.3s ease;
      margin-right: 10px;
    }

    .btn-primary {
      background-color: #3f51b5;
      color: white !important;
    }

    .btn-primary:hover {
      background-color: #303f9f;
    }

    .btn-success {
      background-color: #4CAF50;
      color: white !important;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      box-shadow: 0 1px 10px rgba(0,0,0,0.1);
      background: white;
    }

    th, td {
      padding: 15px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }

    th {
      background-color: #3f51b5;
      color: white;
      font-weight: 600;
    }

    tr:hover {
      background-color: #f5f5f5;
    }

    .toggle-activities {
      color: #3f51b5;
      cursor: pointer;
      font-weight: 500;
      text-decoration: underline;
    }

    .activity-section {
      background: #f8f9fa;
      padding: 20px;
      margin-top: 15px;
      border-radius: 5px;
    }

    .activity-table {
      width: 100%;
      margin-top: 15px;
    }

    .activity-table th {
      background-color: #607d8b;
    }

    .timestamp {
      color: #666;
      font-size: 0.9em;
    }
  </style>
  <script>
    function toggleActivities(quotationId) {
      const div = document.getElementById(`activities-${quotationId}`);
      const icon = document.getElementById(`icon-${quotationId}`);
      if (div.style.display === "none") {
        div.style.display = "block";
        icon.textContent = "▼";
      } else {
        div.style.display = "none";
        icon.textContent = "▶";
      }
    }
  </script>
</head>
<body>
<div class="container">
  <h1>📋 Listado de Cotizaciones</h1>

  <div class="action-buttons">
    <a href="${pageContext.request.contextPath}/quotations/new" class="btn btn-success">
      ➕ Nueva Cotización
    </a>
    <a href="${pageContext.request.contextPath}/views/admin/dashboard.jsp" class="btn btn-primary">
      🏠 Menú Principal
    </a>
  </div>

  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Cliente</th>
      <th>Fecha Inicio</th>
      <th>Fecha Fin</th>
      <th>Total</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${quotations}" var="quotation">
      <tr>
        <td>${quotation.id}</td>
        <td>${quotation.client.name}</td>
        <td>
          <fmt:formatDate value="${quotation.tentativeStartDate}"
                          pattern="dd/MM/yyyy" />
        </td>
        <td>
          <fmt:formatDate value="${quotation.tentativeEndDate}"
                          pattern="dd/MM/yyyy" />
        </td>
        <td>
          <fmt:formatNumber value="${quotation.total}"
                            type="currency"
                            currencySymbol="$"
                            maxFractionDigits="2"/>
        </td>
        <td>
                        <span class="toggle-activities"
                              onclick="toggleActivities(${quotation.id})">
                            <span id="icon-${quotation.id}">▶</span> Detalles
                        </span>
        </td>
      </tr>

      <!-- Sección de actividades desplegable -->
      <tr>
        <td colspan="6" style="padding: 0; border: none;">
          <div id="activities-${quotation.id}"
               style="display: none; padding: 20px 0;">
            <div class="activity-section">
              <!-- Formulario para nueva actividad -->
              <form action="${pageContext.request.contextPath}/quotations/addActivity"
                    method="POST"
                    style="margin-bottom: 25px;">
                <input type="hidden" name="quotationId" value="${quotation.id}">

                <h3 style="color: #3f51b5; margin-bottom: 15px;">
                  🛠️ Agregar Nueva Actividad
                </h3>

                <div style="display: grid;
                                              grid-template-columns: repeat(3, 1fr);
                                              gap: 15px;">
                  <div>
                    <label>Título:</label>
                    <input type="text"
                           name="title"
                           required
                           style="width: 100%;
                                                          padding: 8px;">
                  </div>

                  <div>
                    <label>Empleado:</label>
                    <select name="employeeId"
                            required
                            style="width: 100%;
                                                           padding: 8px;">
                      <c:forEach items="${employees}" var="employee">
                        <option value="${employee.id}">
                            ${employee.name} - ${employee.specialty}
                        </option>
                      </c:forEach>
                    </select>
                  </div>

                  <div>
                    <label>Costo por hora:</label>
                    <input type="number"
                           name="hourlyCost"
                           step="0.01"
                           required
                           style="width: 100%;
                                                          padding: 8px;">
                  </div>
                </div>

                <button type="submit"
                        style="margin-top: 15px;
                                                   background-color: #4CAF50;
                                                   color: white;
                                                   padding: 10px 20px;
                                                   border: none;
                                                   border-radius: 5px;
                                                   cursor: pointer;">
                  💾 Guardar Actividad
                </button>
              </form>

              <!-- Listado de actividades existentes -->
              <c:if test="${not empty quotation.activities}">
                <h4 style="color: #607d8b; margin: 20px 0 10px 0;">
                  🗂️ Actividades Registradas
                </h4>
                <table class="activity-table">
                  <thead>
                  <tr>
                    <th>Título</th>
                    <th>Especialista</th>
                    <th>Costo/Hora</th>
                    <th>Horas</th>
                    <th>Subtotal</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${quotation.activities}" var="activity">
                    <tr>
                      <td>${activity.title}</td>
                      <td>${activity.employee.name}</td>
                      <td>
                        <fmt:formatNumber value="${activity.hourlyCost}"
                                          type="currency"
                                          currencySymbol="$"/>
                      </td>
                      <td>${activity.estimatedHours}</td>
                      <td>
                        <fmt:formatNumber value="${activity.hourlyCost * activity.estimatedHours}"
                                          type="currency"
                                          currencySymbol="$"/>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </c:if>
            </div>
          </div>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>