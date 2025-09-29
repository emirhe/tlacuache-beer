package com.emirhg.sistema.ecommerce.controllers.cataloghi;

import com.emirhg.sistema.ecommerce.dao.jdbc.FormatoDao;
import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "FormatiListServlet", urlPatterns = {"/FormatiListServlet"})
public class FormatiListServlet extends HttpServlet {

    private FormatoDao formatoDao = new FormatoDao();
    private String pagListFormati = "/views/cataloghi/formati.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        // 1) Leer filtros
        String idStr = trimToNull(request.getParameter("id"));
        String codice = trimToNull(request.getParameter("codice"));

        List<Formato> risultati = new ArrayList<>();

        try {
            // 2) Decidir estrategia
            if (idStr != null) {
                Integer id = parseIntSafe(idStr);
                if (id == null || id <= 0) {
                    request.setAttribute("error", "ID non valido.");
                } else {
                    Optional<Formato> opt = formatoDao.findById(id);
                    opt.ifPresent(risultati::add);
                    if (risultati.isEmpty()) {
                        request.getSession().setAttribute("error", "Nessun prodotto trovato per id=" + id);
                    }
                }
            } else if (codice != null && !codice.isBlank()) {
                List<Formato> trovate = formatoDao.findByNome(codice);
                if (!trovate.isEmpty()) {
                    risultati.addAll(trovate);
                } else {
                    request.getSession().setAttribute("error", "Nessun formato trovato per codice \"" + codice + "\"");

                }

            } else {
                // Sin filtros “específicos”: listar
                risultati = formatoDao.findAll();
                if (risultati.isEmpty()) {
                    request.setAttribute("error", "Non c'e nessun formato da mostrare.");
                }
            }

            //atributi da inviare alla pagina listar.jsp, utilizando il metodo le query
            request.setAttribute("formati", formatoDao.findAll());
            request.getRequestDispatcher(pagListFormati).forward(request, response);

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

        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FormatiListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FormatiListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
