package com.emirhg.sistema.ecommerce.controllers;

import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.FormatoDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.StileDao;
import com.emirhg.sistema.ecommerce.model.Prodotto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet(name = "HomeServlet", urlPatterns = {"/HomeServlet"})
public class HomeServlet extends HttpServlet {

    private ProdottoDao prodottoDao = new ProdottoDao();
    private BirraDao birraDao = new BirraDao();
    private FormatoDao formatoDao = new FormatoDao();
    private StileDao stileDao = new StileDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // Lee filtros
            Integer stileId = parseInt(request.getParameter("stileId"));
            Integer formatoId = parseInt(request.getParameter("formatoId"));
            boolean senzaGlut = "1".equals(request.getParameter("senzaGlutine"));
            boolean bio = "1".equals(request.getParameter("biologico"));
            String q = safe(request.getParameter("q")).toLowerCase();

            // Aplica filtros en memoria
            var prodotti = new ArrayList<Prodotto>(prodottoDao.findAllActive());

            // filtro en memoria con removeIf            
            if (stileId != null) {
                prodotti.removeIf(p -> p.getBirra() == null || p.getBirra().getStile() == null
                        || p.getBirra().getStile().getId() != stileId);
            }
            if (formatoId != null) {
                prodotti.removeIf(p -> p.getFormato() == null || p.getFormato().getId() != formatoId);
            }
            if (senzaGlut) {
                prodotti.removeIf(p -> p.getBirra() == null || !p.getBirra().isSenzaGlutine());
            }
            if (bio) {
                prodotti.removeIf(p -> p.getBirra() == null || !p.getBirra().isBiologico());
            }
            if (!q.isEmpty()) {
                prodotti.removeIf(p -> {
                    String sku = p.getSku() == null ? "" : p.getSku().toLowerCase();
                    String nome = (p.getBirra() != null && p.getBirra().getNome() != null)
                            ? p.getBirra().getNome().toLowerCase() : "";
                    return !sku.contains(q) && !nome.contains(q);
                });
            } 

            // 4) catálogos para los <select>
            request.setAttribute("stili", stileDao.findAll());
            request.setAttribute("formati", formatoDao.findAll());

            // 5) resultados → JSP
            request.setAttribute("prodotti", prodotti);
            request.getRequestDispatcher("/home.jsp").forward(request, response);

           

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    //helpers
    private static Integer parseInt(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

}
