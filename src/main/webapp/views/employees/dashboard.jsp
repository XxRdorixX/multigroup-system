<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Panel de Empleado</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .welcome { color: #2c3e50; }
        .menu { margin-top: 20px; }
        .menu a {
            display: block;
            padding: 10px;
            margin: 5px 0;
            background: #3498db;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
<h1>Bienvenido, ${user.username} <span>(Empleado)</span></h1>
<div class="menu">
    <a href="${pageContext.request.contextPath}/clients/list">Gestionar Clientes</a>
    <a href="${pageContext.request.contextPath}/quotations/list">Gestionar Cotizaciones</a>
    <a href="${pageContext.request.contextPath}/logout" style="background: #3498db;">Cerrar Sesión</a>
</div>
</body>
</html>