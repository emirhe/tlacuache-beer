package com.emirhg.sistema.ecommerce.controllers.cataloghi;

import com.emirhg.sistema.ecommerce.dao.jdbc.OrigineDao;
import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrigineListServlet", urlPatterns = {"/OrigineListServlet"})
public class OrigineListServlet extends HttpServlet {

    private OrigineDao origineDao = new OrigineDao();
    private String pagListOrigini = "/views/cataloghi/origini.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null || action.isBlank()) {
            action = "listar";
        }

        switch (action) {
            case "listar":
                Listar(request, response);
                break;
            default:
                throw new AssertionError();
        }

    }

    private void Listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // 1) Leer filtros
        String idStr = trimToNull(request.getParameter("id"));
        String nome = trimToNull(request.getParameter("nome"));

        List<Origine> risultati = new ArrayList<>();

        try {
            // 2) Decidir estrategia
            if (idStr != null) {
                Integer id = parseIntSafe(idStr);
                if (id == null || id <= 0) {
                    request.setAttribute("error", "ID non valido.");
                } else {
                    Optional<Origine> opt = origineDao.findById(id);
                    opt.ifPresent(risultati::add);
                    if (risultati.isEmpty()) {
                        request.getSession().setAttribute("error", "Nessun prodotto trovato per id=" + id);
                    }
                }
            } else if (nome != null && !nome.isBlank()) {
                List<Origine> trovate = origineDao.findByNome(nome);
                if (!trovate.isEmpty()) {
                    risultati.addAll(trovate);
                } else {
                    request.getSession().setAttribute("error", "Nessun origine trovato per luogo \"" + nome + "\"");

                }

            } else {
                // Sin filtros “específicos”: listar
                risultati = origineDao.findAll();
                if (risultati.isEmpty()) {
                    request.setAttribute("error", "Non c'e nessun formato da mostrare.");
                }
            }

            //atributi da inviare alla pagina listar.jsp, utilizando il metodo le query
            request.setAttribute("origini", origineDao.findAll());
            request.getRequestDispatcher(pagListOrigini).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    // ---------- helpers ----------    
    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static Integer parseIntSafe(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
