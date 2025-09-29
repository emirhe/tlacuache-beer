

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />

        <title>Catalogo Birre</title>
    </head>
    <body>
        <jsp:include page="/components/navigation.jsp" />

        <div class="container mt-4"> 
            <jsp:include page="/components/subnav-search-birra.jsp" />
            <div class="card">
                <div class="card-body">
                    <jsp:include page="/components/messagio.jsp" />


                    <div class="table-responsive">
                        <table class="table align-middle table-light table-hover table-bordered table-striped mt-2">
                            <thead class="table-light">
                                <tr class="text-nowrap">                                    
                                    <th>Id</th>
                                    <th>Birra</th>
                                    <th>Gradazione</th>
                                    <th >Stile</th>
                                    <th >Origine</th>
                                    <th >Senza-Glutine</th>
                                    <th >Biologico</th>
                                    <th>Produttore</th>
                                    <th >Azioni</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${empty birre}">
                                        <tr>
                                            <td colspan="10" class="text-center text-muted py-4">
                                                Nessun prodotto trovato.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${birre}" var="b">
                                            <tr>                                                                                               

                                                <!-- ID -->
                                                <td class="fw-semibold">${b.id}</td>

                                                <!-- Birra -->
                                                <td class="fw-semibold">${b.nome}</td>

                                                <!-- Gradazione -->
                                                <td class="text-end text-nowrap">${b.gradazione}% ABV</td>

                                                <!-- Stsile -->
                                                <td>
                                                    <div class="fw-semibold">${b.stile.nome}</div>
                                                    <div class="text-muted small">                                                    
                                                        ${b.stile.descrizione}
                                                    </div>
                                                </td> 

                                                <!-- Origine -->
                                                <td class="text-end text-nowrap">${b.origine.nome}</td>

                                                <!-- SenzaGlutine -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.senzaGlutine}">
                                                            <span class="badge rounded-pill text-bg-info">Senza Glutine</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            No
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <!-- Biologico -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.biologico}">
                                                            <span class="badge rounded-pill text-bg-info">Biologico</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            No
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Produttore -->
                                                <td class="text-end"> ${b.produttore.nome} </td>




                                                <!-- Azioni -->
                                                <td class="text-end text-nowrap">
                                                    <div class="d-flex justify-content-between align-items-center mt-3">                                                  



                                                        <button type="button" class="btn btn-secondary btn-sm" title="Modifica ${b.nome}"
                                                                onclick="location.href = '${pageContext.request.contextPath}/BirreCreateServlet?action=modifica&id=${b.id}'";
                                                                }">
                                                            <i class="fa fa-edit"></i>
                                                        </button>



                                                        &nbsp;
                                                        <button type="button" class="btn btn-outline-danger btn-sm"
                                                                onclick="submitDelete(${b.id})">Elimina</button>

                                                        <form id="delForm" method="post"
                                                              action="${pageContext.request.contextPath}/BirreDeleteServlet"
                                                              style="display:none">
                                                            <input type="hidden" name="id" id="delId">
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
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>





                        </table>

                    </div>

                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js" integrity="sha384-G/EV+4j2dNv+tEPo3++6LCgdCROaejBqfUeNjuKAiuXbjrxilcCdDz6ZAVfHWe1Y" crossorigin="anonymous"></script>


    </body>
</html>
