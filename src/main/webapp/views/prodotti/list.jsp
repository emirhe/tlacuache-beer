

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <title>Tlacuache Beer - prodotti</title>
    </head>
    <body>
        <jsp:include page="/components/navigation.jsp" />    

        <div class="container mt-4">
            <jsp:include page="/components/subnav-search-prod.jsp" />    
            <div class="card">
                <div class="card-body"> 

                    <jsp:include page="/components/messagio.jsp" />

                    <div class="table-responsive">
                        <table class="table align-middle table-light table-hover table-bordered table-striped mt-2">
                            <thead class="table-light">
                                <tr class="text-nowrap">
                                    <th style="width:56px">Img</th>
                                    <th>SKU</th>
                                    <th>Birra</th>
                                    <th>Formato</th>
                                    <th >Volume</th>
                                    <th >Pack</th>
                                    <th >Prezzo</th>
                                    <th >Stock</th>
                                    <th>Stato</th>
                                    <th >Azioni</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${empty prodotti}">
                                        <tr>
                                            <td colspan="10" class="text-center text-muted py-4">
                                                Nessun prodotto trovato.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${prodotti}" var="p">
                                            <tr>
                                                <!-- Img -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty p.urlImg}">
                                                            <img src="${p.urlImg}" alt="${p.birra.nome}"
                                                                 class="rounded" style="width:56px;height:56px;object-fit:cover;">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="bg-light border rounded d-flex align-items-center justify-content-center"
                                                                 style="width:56px;height:56px;">
                                                                 <img src="views/assets/img/prodotti/tlacuache-product-b.png" alt="${prodotto.birra.nome}"
                                             class="w-100 rounded" style="aspect-ratio:1/1; object-fit:cover;">
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- SKU -->
                                                <td class="fw-semibold">${p.sku}</td>

                                                <!-- Birra -->
                                                <td>
                                                    <div class="fw-semibold">${p.birra.nome}</div>
                                                    <div class="text-muted small">
                                                        ${p.birra.stile.nome} · ${p.birra.gradazione}% ABV
                                                    </div>
                                                </td>

                                                <!-- Formato -->
                                                <td class="text-nowrap">${p.formato.codice}</td>

                                                <!-- Volume -->
                                                <td class="text-end text-nowrap">${p.volumeMl} ml</td>

                                                <!-- Pack -->
                                                <td class="text-end">x${p.packSize}</td>

                                                <!-- Prezzo -->
                                                <td class="text-end"> €${p.prezzo} </td>



                                                <!-- Stock -->
                                                <td class="text-end">
                                                    <c:choose>
                                                        <c:when test="${p.stock == 0}">
                                                            <span class="badge rounded-pill text-bg-secondary">0</span>                                                    
                                                        </c:when>                                                
                                                        <c:otherwise>
                                                            <span class="badge rounded-pill text-bg-info">${p.stock}</span>                                                    
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Stato -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${p.active}">
                                                            <span class="badge rounded-pill text-bg-success">Attivo</span>                                                    
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge rounded-pill text-bg-secondary">Disattivo</span>                                                    
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Azioni -->
                                                <td>
                                                    <div class="d-flex justify-content-between align-items-center mt-3">                                                    



                                                        <button type="button" class="btn btn-secondary btn-sm" title="Modifica prodotto ${p.birra.nome}"
                                                                onclick="location.href = '${pageContext.request.contextPath}/ProdottiCreateServlet?action=modifica&id=${p.id}'";                                                            
                                                                }">
                                                            <i class="fa fa-edit"></i>

                                                        </button>

                                                        &nbsp;
                                                        <button type="button" class="btn btn-secondary btn-sm" title="Disattiva Prodotto ${p.birra.nome}"
                                                                onclick="#';
                                                                    }">
                                                            <i class="fa fa-eye-slash"></i>
                                                        </button>                                                        


                                                        &nbsp;
                                                        <button type="button" class="btn btn-outline-danger btn-sm"
                                                                onclick="submitDelete(${p.id})">Elimina</button>

                                                        <form id="delForm" method="post"
                                                              action="${pageContext.request.contextPath}/ProdottiDeleteServlet"
                                                              style="display:none">
                                                            <input type="hidden" name="id" id="delId">
                                                        </form>
                                                        <script>
                                                            function submitDelete(id){
                                                            if (confirm("Confermi l'eliminazione?")){
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
