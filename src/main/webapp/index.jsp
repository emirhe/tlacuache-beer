<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Tlacuache Beer · Admin Demo</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { background: #f6f7fb; }
    .brand { letter-spacing:.5px; }
    .card { border: 0; border-radius: 1rem; box-shadow: 0 10px 30px rgba(0,0,0,.06);}
    .form-control:disabled { background: #f1f3f5; color:#495057; }
  </style>
</head>
<body class="d-flex min-vh-100 align-items-center">

  <div class="container">
    <div class="row justify-content-center">
      <div class="col-sm-11 col-md-8 col-lg-5">
        <div class="text-center mb-4">
          <div class="display-6 fw-semibold brand">Tlacuache Beer</div>
          <div class="text-muted">Sistema Gestione E-commerce</div>
        </div>
          <!-- valor simulado; es solo una demo UI -->
        <div class="card p-3 p-md-4">
          <div class="card-body">
            <h5 class="card-title mb-3">Accedi</h5>
            <p class="text-muted small mb-4">
              Demo: credenziali già compilate. Premi <strong>Accedi</strong> per entrare alla demo.
            </p>

            <div class="mb-3">
              <label class="form-label">Utente</label>
              <input type="text" class="form-control" value="admin" disabled>
            </div>

            <div class="mb-4">
              <label class="form-label">Password</label>              
              <input type="password" class="form-control" value="admin123" disabled>
            </div>

            
            <div class="d-grid gap-2">
              <a class="btn btn-primary btn-lg"
                 href="${pageContext.request.contextPath}/home.jsp">
                Accedi
              </a>
              <a class="btn btn-outline-secondary"
                 href="#" disabled>
                Registrati
              </a>
            </div>

            <hr class="my-4">
            <div class="d-flex align-items-center justify-content-between">
              <span class="text-muted small">Sistema demo, dati fissi</span>
              <span class="badge text-bg-light">v0.1</span>
            </div>
          </div>
        </div>

        <p class="text-center text-muted small mt-3">
          © <script>document.write(new Date().getFullYear())</script> Tlacuache Beer | by Emir Hernandez
        </p>
      </div>
    </div>
  </div>

  <!-- Bootstrap JS (opcional) -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

