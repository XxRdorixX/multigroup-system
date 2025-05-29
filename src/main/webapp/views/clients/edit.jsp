<%--
  Autor: Rodrigo Escobar
  Fecha: 26/5/2025
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Editar Cliente</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:if test="${not empty error}">
  <div class="alert error">${error}</div>
</c:if>
<h1>Editar Cliente</h1>
<a href="${pageContext.request.contextPath}/clients/list">← Volver a la lista</a>

<form action="${pageContext.request.contextPath}/clients/update" method="POST">
  <input type="hidden" name="id" value="${client.id}">

  <label>Nombre: <input type="text" name="name" value="${client.name}" required></label><br>
  <label>Documento: <input type="text" name="document" value="${client.document}" required></label><br>
  <label>Tipo:
    <select name="type">
      <option value="NATURAL">Natural</option> <!-- Valor en mayúsculas -->
      <option value="JURIDICA">Jurídica</option> <!-- Sin tilde y mayúsculas -->
    </select>
  </label><br>
  <label>Teléfono: <input type="tel" name="phone" value="${client.phone}" required></label><br>
  <label>Email: <input type="email" name="email" value="${client.email}" required></label><br>
  <label>Dirección: <input type="text" name="address" value="${client.address}" required></label><br>
  <button type="submit">Actualizar</button>
</form>
</body>
</html>