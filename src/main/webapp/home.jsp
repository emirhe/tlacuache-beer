

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <title>Catalogo</title>


    </head>
    <body>        
        <jsp:include page="components/navigation.jsp" />
        



        <div class="container-fluid mt-2">
             <jsp:include page="components/subnav-search-home.jsp" />
            

            <div class="row">
                <jsp:include page="components/messagio.jsp" />
                <c:forEach items="${prodotti}" var="item">
                    <div class="col-sm-3 mt-1">
                        <div class="card">
                            <img src="views/assets/img/prodotti/tlacuache-product-b.png" width="100%" alt="${item.birra.nome}"/>
                            <div class="card-body">
                                <small>${item.birra.stile.nome}</small>
                                <p class="fw-bold">${item.birra.nome}</p>
                                <div class="d-flex justify-content-between align-items-center">
                                    <button type="button" class="btn btn-sm btn-primary"
                                            onclick="location.href = '${pageContext.request.contextPath}/ProdotoShowServlet?action=show&id=${item.id}'">                                        
                                        Visualizzare
                                    </button>
                                    <small class="fw-bold">€/${item.prezzo}</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>

        </div>






        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js" integrity="sha384-G/EV+4j2dNv+tEPo3++6LCgdCROaejBqfUeNjuKAiuXbjrxilcCdDz6ZAVfHWe1Y" crossorigin="anonymous"></script>
    </body>
</html>

