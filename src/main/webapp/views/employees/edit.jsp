<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Editar Empleado</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<h1>Editar Empleado</h1>
<a href="${pageContext.request.contextPath}/employees/list">← Volver a la lista</a>

<form action="${pageContext.request.contextPath}/employees/update" method="POST">
  <input type="hidden" name="id" value="${employee.id}">

  <label>Nombre: <input type="text" name="name" value="${employee.name}" required></label><br>
  <label>Documento: <input type="text" name="document" value="${employee.document}" required></label><br>
  <label>Tipo:
    <select name="type">
      <option value="NATURAL" ${employee.type == 'NATURAL' ? 'selected' : ''}>Natural</option>
      <option value="JURIDICA" ${employee.type == 'JURIDICA' ? 'selected' : ''}>Jurídica</option>
    </select>
  </label><br>
  <label>Contrato:
    <select name="contractType">
      <option value="PERMANENT" ${employee.contractType == 'PERMANENT' ? 'selected' : ''}>Permanente</option>
      <option value="HOURLY" ${employee.contractType == 'HOURLY' ? 'selected' : ''}>Por Horas</option>
    </select>
  </label><br>
  <label>Teléfono: <input type="tel" name="phone" value="${employee.phone}"></label><br>
  <label>Email: <input type="email" name="email" value="${employee.email}"></label><br>
  <label>Dirección: <input type="text" name="address" value="${employee.address}"></label><br>
  <button type="submit">Actualizar</button>
</form>
</body>
</html>