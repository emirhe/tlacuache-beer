<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <title>Tlacuache Beer - form</title>
    </head>
    <body>
        <jsp:include page="/components/navigation.jsp" />   

        <div class="container my-4">

            <h3 class="mb-3">
                <c:choose>
                    <c:when test="${prodotto.id == 0}">Nuovo Prodotto</c:when>
                    <c:otherwise>Modifica Prodotto · ${prodotto.sku}</c:otherwise>
                </c:choose>
            </h3>

            <form class="card shadow-sm border-0 p-3 p-md-4"
                  method="POST"
                  action="${pageContext.request.contextPath}/ProdottiCreateServlet">

                <input type="hidden" name="action" value="salvare"/>
                <input type="hidden" name="id" value="${prodotto.id}"/>

                <div class="row g-3">
                    <!-- Birra -->
                    <div class="col-md-6">
                        <label class="form-label">Birra</label>
                        <select name="birraId" class="form-select" required>
                            <option value="">Seleziona…</option>
                            <c:forEach items="${birre}" var="b">
                                <option value="${b.id}"
                                        ${(!empty prodotto && !empty prodotto.birra && prodotto.birra.id == b.id) ? 'selected' : ''}>
                                    ${b.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- SKU -->
                    <div class="col-md-6">
                        <label class="form-label">SKU</label>
                        <input type="text" name="sku" class="form-control" required
                               value="${prodotto.sku}" placeholder="TLA-IPA-330-LAT-P6"/>
                    </div>

                    <!-- Formato -->
                    <div class="col-md-6">
                        <label class="form-label">Formato</label>
                        <select name="formatoId" class="form-select" required>
                            <option value="">Seleziona…</option>
                            <c:forEach items="${formati}" var="f">
                                <option value="${f.id}"
                                        ${(!empty prodotto && !empty prodotto.formato && prodotto.formato.id == f.id) ? 'selected' : ''}>
                                    ${f.codice}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Volume -->
                    <div class="col-md-3">
                        <label class="form-label">Volume (ml)</label>
                        <input type="number" min="100" step="10" name="volumeMl" class="form-control" required
                               value="${prodotto.volumeMl}" placeholder="330"/>
                    </div>

                    <!-- Pack -->
                    <div class="col-md-3">
                        <label class="form-label">Pack</label>
                        <select name="packSize" class="form-select" required>
                            <option value="">Seleziona…</option>
                            <c:forEach var="n" items="${[1,4,6,12,24]}">
                                <option value="${n}" ${prodotto.packSize == n ? 'selected' : ''}>x${n}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Prezzo -->
                    <div class="col-md-4">
                        <label class="form-label">Prezzo (EUR)</label>
                        <input type="number" min="0" step="0.01" name="prezzo" class="form-control" required
                               value="${prodotto.prezzo}" placeholder="319.00"/>
                    </div>

                    <!-- Stock -->
                    <div class="col-md-4">
                        <label class="form-label">Stock</label>
                        <input type="number" min="0" step="1" name="stock" class="form-control"
                               value="${prodotto.stock}" placeholder="25"/>
                    </div>

                    <!-- Attivo -->
                    <div class="col-md-4 d-flex align-items-end">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="active" name="active" value="1"
                                   ${prodotto.active ? 'checked' : ''}>
                            <label class="form-check-label" for="active">Attivo</label>
                        </div>
                    </div>

                    <!-- URL Imagen -->
                    <div class="col-12">
                        <label class="form-label">URL immagine (opzionale)</label>
                        <input type="url" name="urlImg" class="form-control" value="${prodotto.urlImg}"
                               placeholder="https://.../beer.jpg"/>
                    </div>
                </div>

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <a class="btn btn-outline-secondary"
                       href="${pageContext.request.contextPath}/views/prodotti/list.jsp">Annulla</a>                       

                    <input type="hidden" name="action" value="salvare">
                    <button class="btn btn-primary" type="submit">
                        <i class="fa fa-save"></i>Salva</button>

                </div>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js" integrity="sha384-G/EV+4j2dNv+tEPo3++6LCgdCROaejBqfUeNjuKAiuXbjrxilcCdDz6ZAVfHWe1Y" crossorigin="anonymous"></script>

    </body>
</html>
