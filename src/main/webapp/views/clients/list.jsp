<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Lista de Clientes</title>
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

    thead {
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
<h1>Clientes</h1>

<a href="${pageContext.request.contextPath}/dashboard" class="btn-menu">🏠 Menú Principal</a>
<a href="${pageContext.request.contextPath}/clients/new" class="btn-menu">➕ Nuevo Cliente</a>

<table>
  <thead>
  <tr>
    <th>Nombre</th>
    <th>Documento</th>
    <th>Tipo</th>
    <th>Teléfono</th>
    <th>Estado</th>
    <th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${clients}" var="client">
    <tr>
      <td>${client.name}</td>
      <td>${client.document}</td>
      <td>${client.type}</td>
      <td>${client.phone}</td>
      <td>
          <span class="status ${client.status == 'ACTIVE' ? 'active' : 'inactive'}">
              ${client.status}
          </span>
      </td>
      <td>
        <a href="edit?id=${client.id}" class="action-link edit-btn">Editar</a>
        <a href="delete?id=${client.id}" class="action-link delete-btn">Desactivar</a>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</body>
</html>
