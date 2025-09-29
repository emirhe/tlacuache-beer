package com.emirhg.sistema.ecommerce.controllers.prodotti;

import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProdottiDeleteServlet", urlPatterns = {"/ProdottiDeleteServlet"})
public class ProdottiDeleteServlet extends HttpServlet {
    
    
    private  ProdottoDao prodottoDao = new ProdottoDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        
        try {
            Integer id = parseIntSafe(request.getParameter("id"));
            if (id == null || id <= 0) {
                request.getSession().setAttribute("error", "ID non valido");
                response.sendRedirect(request.getContextPath() + "/ProdottiListServlet");
                return;
            }
            
            
            
            boolean ok = prodottoDao.deleteById(id);
            if (ok) {
                request.getSession().setAttribute("success", "Prodotto con id " + id + " eliminato!");
            } else {
                request.getSession().setAttribute("error", "Nessun record eliminato (id: " + id + ")");
            }            
            
            
        } catch (DomainConstraintException dce) {
            request.getSession().setAttribute("error", dce.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Errore inatteso: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/ProdottiListServlet?action=listar");
    }

    // Si alguien hace GET, respondemos 405 o redirigimos
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST per eliminare.");
    }

    private static Integer parseIntSafe(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
