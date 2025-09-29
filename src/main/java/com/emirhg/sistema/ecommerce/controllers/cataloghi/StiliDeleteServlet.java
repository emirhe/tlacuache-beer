package com.emirhg.sistema.ecommerce.controllers.cataloghi;

import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.jdbc.StileDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StiliDeleteServlet", urlPatterns = {"/StiliDeleteServlet"})
public class StiliDeleteServlet extends HttpServlet {

    private StileDao stileDao = new StileDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Integer id = parseIntSafe(request.getParameter("id"));
            if (id == null || id <= 0) {
                request.getSession().setAttribute("error", "ID non valido");
                response.sendRedirect(request.getContextPath() + "/StiliListServlet");
                return;
            }           
                        
            boolean ok = stileDao.deleteById(id);
            if (ok) {
                request.getSession().setAttribute("success", "Stile con id " + id + " eliminato!");
            } else {
                request.getSession().setAttribute("error", "Nessun record eliminato (id: " + id + ")");
            }          
            
            
        } catch (DomainConstraintException dce) {
            request.getSession().setAttribute("error", dce.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Errore inatteso: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/StiliListServlet?action=listar");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
