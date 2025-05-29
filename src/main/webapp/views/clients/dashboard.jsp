<c:if test="${user.role == 'CLIENT'}">
    <!-- Ver solo sus cotizaciones -->
    <a href="${pageContext.request.contextPath}/dashboard">Ir al menú principal</a>
</c:if>