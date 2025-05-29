<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo Empleado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/employees/list" class="btn-back">← Volver a la lista</a>
    <h1>Registrar Nuevo Empleado</h1>

    <form action="${pageContext.request.contextPath}/employees/create" method="POST">
        <!-- Campos específicos de empleado -->
        <div class="form-group">
            <label>Nombre:</label>
            <input type="text" name="name" required>
        </div>

        <div class="form-group">
            <label>Documento:</label>
            <input type="text" name="document" required>
        </div>

        <div class="form-group">
            <label>Tipo de persona:</label>
            <select name="type">
                <option value="NATURAL">Natural</option>
                <option value="JURIDICA">Jurídica</option>
            </select>
        </div>

        <div class="form-group">
            <label>Tipo de contratación:</label>
            <select name="contractType">
                <option value="PERMANENTE">Permanente</option>
                <option value="POR_HORAS">Por Horas</option>
            </select>
        </div>

        <div class="form-group">
            <label>Teléfono:</label>
            <input type="tel" name="phone" required>
        </div>

        <div class="form-group">
            <label>Correo electrónico:</label>
            <input type="email" name="email" required>
        </div>

        <div class="form-group">
            <label>Dirección:</label>
            <input type="text" name="address" required>
        </div>

        <!-- Campos de autenticación -->
        <div class="form-group">
            <label>Usuario:</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>Contraseña:</label>
            <input type="password" name="password" required>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
</body>
</html>