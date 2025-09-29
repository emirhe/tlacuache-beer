<%-- 
    Document   : show
    Created on : 16 set 2025, 12:58:57
    Author     : Emir Hernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />

        <title>Prodotto</title>
    </head>
    <body>
        <jsp:include page="/components/navigation.jsp" />

        <div class="container my-4">

            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home.jsp">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/views/prodotti/list.jsp">Prodotti</a></li>
                    <li class="breadcrumb-item active" aria-current="page">${prodotto.sku}</li>
                </ol>
            </nav>

            <!-- Título + acciones -->
            <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
                <div>
                    <h3 class="m-0">${prodotto.birra.nome}</h3>
                    <div class="text-muted">SKU: ${prodotto.sku}</div>
                </div>
                <div class="d-flex gap-2">
                    <a class="btn btn-outline-secondary"
                       href="${pageContext.request.contextPath}/views/prodotti/list.jsp">← Indietro</a>      
                </div>
            </div>

            <div class="row g-3">
                <!-- Columna izquierda: Imagen + estado -->
                <div class="col-12 col-md-4">
                    <div class="card shadow-sm border-0">
                        <div class="card-body">
                            <div class="position-relative">
                                <c:choose>
                                    <c:when test="${not empty prodotto.urlImg}">
                                        <img src="${prodotto.urlImg}" alt="${prodotto.birra.nome}"
                                             class="w-100 rounded" style="aspect-ratio:1/1; object-fit:cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="bg-light border rounded w-100 d-flex align-items-center justify-content-center"
                                             style="aspect-ratio:1/1;">
                                        <img src="views/assets/img/prodotti/tlacuache-product-b.png" alt="${prodotto.birra.nome}"
                                             class="w-100 rounded" style="aspect-ratio:1/1; object-fit:cover;">
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="mt-3 d-flex flex-wrap gap-2">
                                <c:choose>
                                    <c:when test="${prodotto.active}">
                                        <span class="badge text-bg-success">Attivo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge text-bg-secondary">Disattivo</span>
                                    </c:otherwise>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${prodotto.stock == 0}">
                                        <span class="badge text-bg-danger">Senza stock</span>
                                    </c:when>
                                    <c:when test="${prodotto.stock <= 5}">
                                        <span class="badge text-bg-warning">Stock basso</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge text-bg-info">In stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Columna derecha: Detalles -->
                <div class="col-12 col-md-8">
                    <div class="card shadow-sm border-0">
                        <div class="card-body">

                            <!-- Precio grande -->
                            <div class="d-flex align-items-baseline gap-2 mb-3">
                                <h2 class="m-0">
                                    ${prodotto.prezzo}
                                </h2>
                                <span class="text-muted">EUR</span>
                            </div>

                            <!-- Detalles clave -->
                            <dl class="row mb-0">
                                <dt class="col-sm-4">Birra</dt>
                                <dd class="col-sm-8">${prodotto.birra.nome}</dd>

                                <dt class="col-sm-4">Stile</dt>
                                <dd class="col-sm-8">${prodotto.birra.stile.nome}</dd>

                                <dt class="col-sm-4">Gradazione</dt>
                                <dd class="col-sm-8">${prodotto.birra.gradazione}% ABV</dd>

                                <dt class="col-sm-4">Origine</dt>
                                <dd class="col-sm-8">${prodotto.birra.origine.nome}</dd>

                                <dt class="col-sm-4">Produttore</dt>
                                <dd class="col-sm-8">${prodotto.birra.produttore.nome}</dd>

                                <dt class="col-sm-4">Formato</dt>
                                <dd class="col-sm-8 text-uppercase">${prodotto.formato.codice}</dd>

                                <dt class="col-sm-4">Volume</dt>
                                <dd class="col-sm-8">${prodotto.volumeMl} ml</dd>

                                <dt class="col-sm-4">Pack</dt>
                                <dd class="col-sm-8">x${prodotto.packSize}</dd>

                                <dt class="col-sm-4">Stock</dt>
                                <dd class="col-sm-8">${prodotto.stock}</dd>

                                <dt class="col-sm-4">Senza glutine</dt>
                                <dd class="col-sm-8">
                                    <c:choose>
                                        <c:when test="${prodotto.birra.senzaGlutine}">Sì</c:when>
                                        <c:otherwise>No</c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt class="col-sm-4">Biologico</dt>
                                <dd class="col-sm-8">
                                    <c:choose>
                                        <c:when test="${prodotto.birra.biologico}">Sì</c:when>
                                        <c:otherwise>No</c:otherwise>
                                    </c:choose>
                                </dd>
                            </dl>
                        </div>

                        <!-- Footer acciones secundarias -->
                        <div class="card-footer bg-white d-flex justify-content-end gap-2">
                            <button type="button" class="btn btn-secondary btn-sm" title="Modifica prodotto ${prodotto.birra.nome}"
                                    onclick="location.href = 'ProdottiCreateServlet?action=modifica&id=${prodotto.id}'";                                                            
                                    }">
                                <i class="fa fa-edit"></i> Modifica

                            </button>

                            <button type="button" class="btn btn-outline-danger btn-sm"
                                    onclick="submitDelete(${prodotto.id})">Elimina</button>

                            <form id="delForm" method="post"
                                  action="${pageContext.request.contextPath}/ProdottiDeleteServlet"
                                  style="display:none">                                
                                <input type="hidden" name="id" id="delId"/>
                            </form>
                            <script>
                                function submitDelete(id) {
                                    if (confirm("Confermi l'eliminazione?")) {
                                        document.getElementById('delId').value = id;
                                        document.getElementById('delForm').submit();
                                    }
                                }
                            </script>                        



                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
