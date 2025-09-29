<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Stile · Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="/components/navigation.jsp" />
  <div class="container my-4">
    <h3 class="mb-3">
      <c:choose>
        <c:when test="${stile.id == 0}">Nuovo Stile</c:when>
        <c:otherwise>Modifica Stile · ${stile.nome}</c:otherwise>
      </c:choose>
    </h3>

    <c:if test="${not empty errors}">
      <div class="alert alert-danger">
        <ul class="mb-0">
          <c:forEach var="e" items="${errors}">
            <li>${e}</li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <form class="card shadow-sm border-0 p-3 p-md-4" method="POST" action="${pageContext.request.contextPath}/StiliCreateServlet">
      <input type="hidden" name="action" value="salvare"/>
      <input type="hidden" name="id" value="${stile.id}"/>

      <div class="row g-3">
        <div class="col-md-6">
          <label class="form-label">Nome</label>
          <input class="form-control" name="nome" required value="${stile.nome}" placeholder="IPA"/>
        </div>
        <div class="col-12">
          <label class="form-label">Descrizione</label>
          <textarea class="form-control" name="descrizione" rows="3" placeholder="Note di stile...">${stile.descrizione}</textarea>
        </div>
      </div>

      <div class="d-flex justify-content-end gap-2 mt-4">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/StiliListServlet">Annulla</a>
        <button class="btn btn-primary" type="submit"><i class="fa fa-save"></i> Salva</button>
      </div>
    </form>
  </div>
</body>
</html>

