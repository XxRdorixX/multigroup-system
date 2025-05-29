<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Empleados</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            padding: 20px;
        }

        h1 {
            color: #333;
        }

        a {
            text-decoration: none;
            color: #4a148c;
            font-weight: bold;
            margin-right: 10px;
        }

        .btn-menu {
            display: inline-block;
            margin-bottom: 15px;
            color: #007bff;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: left;
        }

        thead, tr:first-child {
            background-color: #3f51b5;
            color: #fff;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .status.active {
            color: green;
            font-weight: bold;
        }

        .status.inactive {
            color: red;
            font-weight: bold;
        }

        .action-link {
            padding: 5px 10px;
            border-radius: 4px;
            margin-right: 5px;
        }

        .edit-btn {
            background-color: #4caf50;
            color: white;
        }

        .delete-btn {
            background-color: #f44336;
            color: white;
        }

        .edit-btn:hover {
            background-color: #45a049;
        }

        .delete-btn:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
<h1>Lista de Empleados</h1>

<a href="${pageContext.request.contextPath}/views/admin/dashboard.jsp" class="btn-menu">🏠 Menú Principal</a>
<a href="${pageContext.request.contextPath}/employees/new" class="btn-menu">➕ Nuevo Empleado</a>

<table>
    <tr>
        <th>Nombre</th>
        <th>Documento</th>
        <th>Tipo</th>
        <th>Contrato</th>
        <th>Estado</th>
        <th>Acciones</th>
    </tr>
    <c:forEach items="${employees}" var="employee">
        <tr>
            <td>${employee.name}</td>
            <td>${employee.document}</td>
            <td>${employee.type}</td>
            <td>${employee.contractType}</td>
            <td>
                <span class="status ${employee.status == 'ACTIVE' ? 'active' : 'inactive'}">
                        ${employee.status}
                </span>
            </td>
            <td>
                <a href="edit?id=${employee.id}" class="action-link edit-btn">Editar</a>
                <a href="delete?id=${employee.id}" class="action-link delete-btn">Desactivar</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
