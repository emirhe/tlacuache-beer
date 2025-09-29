
package com.emirhg.sistema.ecommerce.controllers.prodotti;

import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "ProdotoShowServlet", urlPatterns = {"/ProdotoShowServlet"})
public class ProdotoShowServlet extends HttpServlet {
    
    private ProdottoDao prodottoDao = new ProdottoDao();
    private String pagShowProdotto = "/views/prodotti/show.jsp";
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null || action.isBlank()) action = "show";
        
        switch(action){
            case "show":
                Show(request,response);
                        break;
            default:
               throw new AssertionError(); 
        }
        
    
    
    
    
    }

    private void Show(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException{
                response.setContentType("text/html;charset=UTF-8");
        
         int id = Integer.parseInt(request.getParameter("id"));     
        
        var p= prodottoDao.findById(id).orElseThrow();
        
        request.setAttribute("prodotto", p);
        request.getRequestDispatcher(pagShowProdotto).forward(request, response);
        
   }
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ProdotoShowServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ProdotoShowServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    
   

}
