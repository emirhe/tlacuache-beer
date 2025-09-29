



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<nav class="navbar navbar-expand-lg bg-body-tertiary rounded-3 shadow-sm mb-3">

    <div class="container-fluid">    

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#homeFilters">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="homeFilters">
            <form class="ms-auto w-100" method="GET" action="${pageContext.request.contextPath}/HomeServlet">
                <div class="row g-2 align-items-center justify-content-end row-cols-auto">
                    <!-- Stile -->
                    <div class="col ms-1">
                    <div class="input-group input-group-sm" style="min-width: 220px;">
                        <span class="input-group-text">Stile</span>
                        <select name="stileId" class="form-select">
                            <option value="">Tutti</option>
                            <c:forEach items="${stili}" var="s">
                                <option value="${s.id}" ${param.stileId == s.id ? 'selected' : ''}>
                                    ${s.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    </div>

                    <!-- Formato -->
                    <div class="col ms-1">
                    <div class="input-group input-group-sm" style="min-width: 200px;">
                        <span class="input-group-text">Formato</span>
                        <select name="formatoId" class="form-select">
                            <option value="">Tutti</option>
                            <c:forEach items="${formati}" var="f">
                                <option value="${f.id}" ${param.formatoId == f.id ? 'selected' : ''}>
                                    ${f.codice}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    </div>

                    <!-- Senza glutine -->
                    <div class="col ms-1">
                    <div class="form-check form-switch d-flex align-items-center">
                        
                        <input class="form-check-input" type="checkbox" id="sg" name="senzaGlutine" value="1"
                               ${param.senzaGlutine == '1' ? 'checked' : ''}>
                        <label class="form-check-label small ms-2 text-nowrap" for="sg">Senza glutine</label>
                        
                    </div>
                    </div>

                    <!-- Biologico -->
                    <div class="col ms-1">
                    <div class="form-check form-switch d-flex align-items-center">
                        
                        <input class="form-check-input" type="checkbox" id="bio" name="biologico" value="1"
                               ${param.biologico == '1' ? 'checked' : ''}>
                        <label class="form-check-label small ms-2 text-nowrap" for="bio">Biologico</label>
                        
                    </div>
                    </div>

                    <!-- Ricerca testo -->
                    <div class="col ms-1" >
                    <div class="input-group input-group-sm  " style="min-width: 260px;">
                        <span class="input-group-text " >Sku/Birra</span>
                        <input type="text" class="form-control" name="q" placeholder="SKU o nome birra"
                               value="${fn:escapeXml(param.q)}">
                    </div>
                    </div>
                    
                    <div class="col ms-2">
                    <button class="btn btn-primary btn-sm" type="submit">
                        <i class="fa fa-search"></i> Cerca
                    </button>
                    <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/HomeServlet">
                         <i class="fa fa-repeat"></i> Reset
                    </a>
                    </div>
            </form>
        </div>
    </div>
</div>
</nav>
