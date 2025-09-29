
package com.emirhg.sistema.ecommerce.controllers.birre;

import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.controllers.prodotti.ProdottiDeleteServlet;
import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "BirreDeleteServlet", urlPatterns = {"/BirreDeleteServlet"})
public class BirreDeleteServlet extends HttpServlet {
    
    private BirraDao birraDao = new BirraDao();

   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        
        try {
            Integer id = parseIntSafe(request.getParameter("id"));
            if (id == null || id <= 0) {
                request.getSession().setAttribute("error", "ID non valido");
                response.sendRedirect(request.getContextPath() + "/BirreListServlet");
                return;
            }           
                        
            boolean ok = birraDao.deleteById(id);
            if (ok) {
                request.getSession().setAttribute("success", "Birra con id " + id + " eliminata!");
            } else {
                request.getSession().setAttribute("error", "Nessun record eliminato (id: " + id + ")");
            }          
            
            
        } catch (DomainConstraintException dce) {
            request.getSession().setAttribute("error", dce.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Errore inatteso: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/BirreListServlet?action=listar");
    }

    // Si alguien hace GET, respondemos 405 o redirigimos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST per eliminare.");
    }

    private static Integer parseIntSafe(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
   

}
