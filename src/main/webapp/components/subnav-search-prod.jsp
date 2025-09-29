<%-- 
    Document   : subnav-search-prod
    Created on : 18 set 2025, 09:16:52
    Author     : Emir Hernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg bg-body-tertiary rounded-3 shadow-sm mb-3">
    <div class="container-fluid">

        <!-- Botón agregar a la izquierda -->
        <div class="d-flex align-items-center gap-2">
            <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/ProdottiCreateServlet?action=nuovo">
                <i class="fa fa-plus-circle"></i> Aggiungi Prodotto
            </a>

            <!-- Toggler (móvil) -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#prodFilters"
                    aria-controls="prodFilters" aria-expanded="false" aria-label="Toggle filters">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse mt-2 mt-lg-0" id="prodFilters">

            <form class="ms-auto d-flex gap-2 align-items-center"  method="GET" action="${pageContext.request.contextPath}/ProdottiListServlet">               


                <!-- ID -->
                <div class="input-group input-group-sm" style="max-width: 160px;">
                    <span class="input-group-text">ID</span>
                    <input type="number" min="1" class="form-control" name="id"
                           placeholder="Es. 1001"
                           value="${param.id}">
                </div>

                <!-- SKU -->
                <div class="input-group input-group-sm" style="min-width: 240px;">
                    <span class="input-group-text">SKU</span>
                    <input type="text" class="form-control" name="sku"
                           placeholder="Es. TLA-IPA-330-LAT-P6"
                           value="${param.sku}">
                </div>

                <!-- Solo attivi -->               
                
                <div class="form-check form-switch ms-1 mb-0 flex-shrink-0 d-flex align-items-center">
  <input class="form-check-input" type="checkbox" id="onlyActive" name="onlyActive" value="1"
         ${param.onlyActive == '1' ? 'checked' : ''}>
  <label class="form-check-label small ms-2 text-nowrap" for="onlyActive">Solo attivi</label>
</div>


               

                <!-- Acciones -->
                <button class="btn btn-outline-success btn-sm ms-2" type="submit">Cerca</button>
                <a class="btn btn-outline-secondary btn-sm"
                   href="${pageContext.request.contextPath}/ProdottiListServlet?action">Reset</a>
            </form>
        </div>
    </div>
</nav>

