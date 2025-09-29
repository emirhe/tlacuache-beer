<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Birra · Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>
  <jsp:include page="/components/navigation.jsp" />

  <div class="container my-4">

    <h3 class="mb-3">
      <c:choose>
        <c:when test="${birra.id == 0}">Nuova Birra</c:when>
        <c:otherwise>Modifica Birra · ${birra.nome}</c:otherwise>
      </c:choose>
    </h3>

    <!-- Errores -->
    <c:if test="${not empty errors}">
      <div class="alert alert-danger">
        <ul class="mb-0">
          <c:forEach var="e" items="${errors}">
            <li>${e}</li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <form class="card shadow-sm border-0 p-3 p-md-4" method="POST" action="${pageContext.request.contextPath}/BirreCreateServlet">
      <input type="hidden" name="action" value="salvare"/>
      <input type="hidden" name="id" value="${birra.id}"/>

      <div class="row g-3">
        <!-- Nome -->
        <div class="col-md-6">
          <label class="form-label">Nome</label>
          <input type="text" class="form-control" name="nome" required value="${birra.nome}" placeholder="Tlacuache IPA"/>
        </div>

        <!-- Gradazione -->
        <div class="col-md-3">
          <label class="form-label">Gradazione (% ABV)</label>
          <input type="number" class="form-control" name="gradazione" step="0.1" min="0" value="${birra.gradazione}" placeholder="5.5"/>
        </div>

        <!-- Stile -->
        <div class="col-md-3">
          <label class="form-label">Stile</label>
          <select name="stileId" class="form-select" required>
            <option value="">Seleziona…</option>
            <c:forEach var="s" items="${stili}">
              <option value="${s.id}" ${(!empty birra && !empty birra.stile && birra.stile.id == s.id) ? 'selected' : ''}>
                ${s.nome}
              </option>
            </c:forEach>
          </select>
        </div>

        <!-- Origine -->
        <div class="col-md-6">
          <label class="form-label">Origine</label>
          <select name="origineId" class="form-select" required>
            <option value="">Seleziona…</option>
            <c:forEach var="o" items="${origini}">
              <option value="${o.id}" ${(!empty birra && !empty birra.origine && birra.origine.id == o.id) ? 'selected' : ''}>
                ${o.nome}
              </option>
            </c:forEach>
          </select>
        </div>

        <!-- Produttore -->
        <div class="col-md-6">
          <label class="form-label">Produttore</label>
          <select name="produttoreId" class="form-select" required>
            <option value="">Seleziona…</option>
            <c:forEach var="p" items="${produttori}">
              <option value="${p.id}" ${(!empty birra && !empty birra.produttore && birra.produttore.id == p.id) ? 'selected' : ''}>
                ${p.nome}
              </option>
            </c:forEach>
          </select>
        </div>

        <!-- Flags -->
        <div class="col-md-3 d-flex align-items-end">
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="senzaGlutine" name="senzaGlutine" value="1"
                   ${birra.senzaGlutine ? 'checked' : ''}>
            <label class="form-check-label" for="senzaGlutine">Senza glutine</label>
          </div>
        </div>

        <div class="col-md-3 d-flex align-items-end">
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="biologico" name="biologico" value="1"
                   ${birra.biologico ? 'checked' : ''}>
            <label class="form-check-label" for="biologico">Biologico</label>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-end gap-2 mt-4">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/BirreListServlet">Annulla</a>
        <button class="btn btn-primary" type="submit"><i class="fa fa-save"></i> Salva</button>
      </div>
    </form>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js"></script>
</body>
</html>
