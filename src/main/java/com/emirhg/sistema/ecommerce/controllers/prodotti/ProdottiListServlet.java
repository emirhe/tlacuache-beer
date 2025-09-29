
package com.emirhg.sistema.ecommerce.controllers.prodotti;


import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import com.emirhg.sistema.ecommerce.model.Prodotto;
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


@WebServlet(name = "ProdottiListServlet", urlPatterns = {"/ProdottiListServlet"})
public class ProdottiListServlet extends HttpServlet {

    private ProdottoDao prodottoDao = new ProdottoDao();
private String pagListarProdotti = "/views/prodotti/list.jsp"; 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null || action.isBlank()) action = "listar";
        switch(action){
            case "listar":
                Listar(request,response);
                break;
            default:
                throw new AssertionError();
        }
        
        
    }

    
    private void Listar(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        
         // 1) Leer filtros
        String idStr = trimToNull(request.getParameter("id"));
        String sku    = trimToNull(request.getParameter("sku"));
        String onlyActiveParam = trimToNull(request.getParameter("onlyActive"));
        boolean onlyActive = "1".equals(onlyActiveParam) || "true".equalsIgnoreCase(onlyActiveParam) || "on".equalsIgnoreCase(onlyActiveParam);

        List<Prodotto> risultati = new ArrayList<>();

        try {
            // 2) Decidir estrategia
            if (idStr != null) {
                Integer id = parseIntSafe(idStr);
                if (id == null || id <= 0) {
                    request.getSession().setAttribute("error", "ID non valido."); 
                    
                } else {
                    Optional<Prodotto> opt = prodottoDao.findById(id);
                    opt.ifPresent(risultati::add);
                    if (risultati.isEmpty()) {
                        request.getSession().setAttribute("error", "Nessun prodotto trovato per id=" + id);                         
                    }
                }
            } else if (sku != null) {
                Optional<Prodotto> opt = prodottoDao.findBySku(sku);
                opt.ifPresent(risultati::add);
                if (risultati.isEmpty()) {
                    request.getSession().setAttribute("error", "Nessun prodotto trovato per SKU \"" + sku + "\"");                      
                }
            } else {
                // Sin filtros “específicos”: listar
                risultati = onlyActive ? prodottoDao.findAllActive() : prodottoDao.findAll();
                if (risultati.isEmpty()) {
                    request.getSession().setAttribute("error", "Non ci sono prodotti da mostrare.");    
                    
                }
            }

            // 3) Poner resultados y reenviar al JSP de lista
            request.setAttribute("prodotti", risultati);
            request.getRequestDispatcher("/views/prodotti/list.jsp").forward(request, response);

        } catch (SQLException e) {            
            throw new ServletException(e);
        } catch (Exception e) {            
            throw new ServletException(e);
        }
        
        
            }
   
    
    
     // ---------- helpers ----------
    private static String trimToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static Integer parseIntSafe(String s) {
        try { return Integer.valueOf(s); } catch (Exception e) { return null; }
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
