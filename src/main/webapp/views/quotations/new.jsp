<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nueva Cotización</title>
    <style>
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        .btn { padding: 8px 15px; margin: 5px; cursor: pointer; }
    </style>
    <script>
        let activityCount = 0;

        function addAssignment() {
            const container = document.getElementById("assignments-container");
            const template = document.getElementById("assignment-template").content.cloneNode(true);

            // Actualizar nombres de campos para agrupación
            template.querySelectorAll('[name]').forEach(el => {
                el.name = `activities[\${activityCount}].${el.name}`;
            });

            container.appendChild(template);
            activityCount++;
        }
    </script>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/quotations/list" class="btn">← Volver</a>
    <h1>Nueva Cotización</h1>

    <form action="${pageContext.request.contextPath}/quotations/create" method="POST">
        <!-- Sección Cliente Principal -->
        <div class="form-group">
            <label>Cliente Principal:</label>
            <select name="clientId" required>
                <c:forEach items="${clients}" var="client">
                    <option value="${client.id}">${client.name} (${client.company})</option>
                </c:forEach>
            </select>
        </div>

        <!-- Fechas Principales -->
        <div class="form-group">
            <label>Fecha Inicio Tentativa:</label>
            <input type="date" name="startDate" required
                   pattern="\d{4}-\d{2}-\d{2}"
                   title="Formato: YYYY-MM-DD">
        </div>

        <div class="form-group">
            <label>Fecha Fin Tentativa:</label>
            <input type="date" name="endDate" required
                   pattern="\d{4}-\d{2}-\d{2}"
                   title="Formato: YYYY-MM-DD">
        </div>

        <!-- Sección de Actividades -->
        <h3>Actividades</h3>
        <div id="assignments-container">
            <template id="assignment-template">
                <div class="activity" style="border: 1px solid #ccc; padding: 15px; margin: 10px 0;">
                    <div class="form-group">
                        <label>Título Actividad:</label>
                        <input type="text" name="title" required>
                    </div>

                    <div class="form-group">
                        <label>Trabajador:</label>
                        <select name="employeeId" required>
                            <c:forEach items="${employees}" var="emp">
                                <option value="${emp.id}">${emp.name} - ${emp.specialty}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Fecha/Hora Inicio:</label>
                        <input type="datetime-local" name="startTime" required
                               pattern="\d{4}-\d{2}-\d{2}T\d{2}:\d{2}">
                    </div>

                    <div class="form-group">
                        <label>Fecha/Hora Fin:</label>
                        <input type="datetime-local" name="endTime" required
                               pattern="\d{4}-\d{2}-\d{2}T\d{2}:\d{2}">
                    </div>

                    <div class="form-group">
                        <label>Coste por Hora (USD):</label>
                        <input type="number" name="hourlyCost" step="0.01" min="0" required>
                    </div>

                    <div class="form-group">
                        <label>Incremento Extra (%):</label>
                        <input type="number" name="extraPercentage" step="0.1" min="0" required>
                    </div>
                </div>
            </template>
        </div>

        <button type="button" class="btn" onclick="addAssignment()">➕ Nueva Actividad</button>
        <button type="submit" class="btn" style="background-color: #4CAF50; color: white;">Guardar Cotización</button>
    </form>
</div>
</body>
</html>