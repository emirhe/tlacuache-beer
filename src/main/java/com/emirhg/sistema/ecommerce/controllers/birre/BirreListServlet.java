package com.emirhg.sistema.ecommerce.controllers.birre;

import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import com.emirhg.sistema.ecommerce.model.Birra;
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

@WebServlet(name = "BirreListServlet", urlPatterns = {"/BirreListServlet"})
public class BirreListServlet extends HttpServlet {

    private BirraDao birraDao = new BirraDao();
    private String pagListBirre = "/views/birre/list.jsp";

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

        // 1) Leer filtros
        String idStr = trimToNull(request.getParameter("id"));
        String nome = trimToNull(request.getParameter("nome"));
        String stile = trimToNull(request.getParameter("stile"));

        List<Birra> risultati = new ArrayList<>();

        try {
            // 2) Decidir estrategia
            if (idStr != null) {
                Integer id = parseIntSafe(idStr);
                if (id == null || id <= 0) {
                    request.setAttribute("error", "ID non valido.");
                } else {
                    Optional<Birra> opt = birraDao.findById(id);
                    opt.ifPresent(risultati::add);
                    if (risultati.isEmpty()) {
                        request.getSession().setAttribute("error", "Nessun prodotto trovato per id=" + id);                        
                    }
                }
            } else if (nome != null && !nome.isBlank()) {
                List<Birra> trovate = birraDao.findByNomeLike(nome.trim());
                if (!trovate.isEmpty()) {
                    risultati.addAll(trovate);
                } else {
                    request.getSession().setAttribute("error", "Nessuna birra trovata per nome \"" + nome + "\"");                     

                }

            } else if (stile != null && !stile.isBlank()) {
                List<Birra> trovate = birraDao.listByStileNome(stile);
                if (!trovate.isEmpty()) {
                    risultati.addAll(trovate);
                } else {
                     request.getSession().setAttribute("error", "Nessuna birra trovata per stile \"" + stile + "\"");                     

                }

            } else {
                // Sin filtros “específicos”: listar
                risultati = birraDao.findAll();
                if (risultati.isEmpty()) {
                    request.setAttribute("error", "Non ci sono birre da mostrare.");
                }
            }

            //atributi da inviare alla pagina listar.jsp, utilizando il metodo le query
            request.setAttribute("birre", risultati);
            request.getRequestDispatcher(pagListBirre).forward(request, response);

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
            Logger.getLogger(BirreListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(BirreListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
