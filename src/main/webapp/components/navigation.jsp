

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <i class="fas fa-basket-shopping"></i>
                    Tlacuache Beer</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/home.jsp">
                                <i class="fas fa-rectangle-list"></i>
                                Home</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/ProdottiListServlet?action=listar">
                                <i class="fas fa-cube"></i>
                                Prodotti</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/BirreListServlet?action=listar">
                                <i class="fas fa-beer"></i>
                                Birre</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link active dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-list"></i>
                                Catalogui
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/StiliListServlet?action=listar">Stile</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/OrigineListServlet?action=listar">Origini</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ProduttoreListServlet?action=listar">Produttori</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/FormatiListServlet?action=listar">Formati</a></li>
                            </ul>
                        </li>
                        
                    </ul>
                    <form class="d-flex" role="search">                        
                        
                        <a href="${pageContext.request.contextPath}/index.jsp" class="btn">
                            <i class="fas fa-user"></i>
                            Logout
                        </a>
                    </form>
                </div>
            </div>
        </nav> 
