<%--
  Autor: Rodrigo Escobar
  Date: 26/5/2025
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>

    <a href="${pageContext.request.contextPath}/clients/list" class="btn-back">← Volver a la lista</a>
    <h1>Registrar Nuevo Cliente</h1>

    <form action="${pageContext.request.contextPath}/clients/create" method="POST">
        <div class="form-group">
            <label>Nombre:</label>
            <input type="text" name="name" required>
        </div>

        <div class="form-group">
            <label>Documento:</label>
            <input type="text" name="document" required>
        </div>

        <div class="form-group">
            <label>Tipo:</label>
            <select name="type">
                <option value="NATURAL">Natural</option>
                <option value="JURIDICA">Jurídica</option>
            </select>
        </div>

        <div class="form-group">
            <label>Teléfono:</label>
            <input type="tel" name="phone" required>
        </div>

        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>

        <div class="form-group">
            <label>Dirección:</label>
            <input type="text" name="address" required>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
</body>
</html>