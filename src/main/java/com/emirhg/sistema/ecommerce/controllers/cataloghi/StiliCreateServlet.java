package com.emirhg.sistema.ecommerce.controllers.cataloghi;

import com.emirhg.sistema.ecommerce.dao.jdbc.StileDao;
import com.emirhg.sistema.ecommerce.model.cataloghi.Stile;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "StiliCreateServlet", urlPatterns = {"/StiliCreateServlet"})
public class StiliCreateServlet extends HttpServlet {

    private StileDao stileDao = new StileDao();
    private static final String FORM_JSP = "/views/cataloghi/stili-form.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null || action.isBlank()) {
            action = "nuovo";
        }

        switch (action) {
            case "nuovo" ->
                showNewForm(request, response);
            case "modifica" ->
                showEditForm(request, response);
            case "salvare" ->
                save(request, response);
            default -> {
                request.getSession().setAttribute("error", "Azione non valida");
                response.sendRedirect(request.getContextPath() + "/BirreListServlet");
            }
        }

    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        request.setAttribute("stile", new Stile());
        request.getRequestDispatcher(FORM_JSP).forward(request, response);
        
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        
        Integer id = parseIntSafe(request.getParameter("id"));
        if (id == null || id <= 0) {
            request.getSession().setAttribute("error", "ID non valido");
            response.sendRedirect(request.getContextPath() + "/FormatiListServlet");
            return;
        }
        
        var opt = stileDao.findById(id);
        if (opt.isEmpty()) {
            request.getSession().setAttribute("error", "Stile id " + id + " non trovato");
            response.sendRedirect(request.getContextPath() + "/StiliListServlet");
            return;
        }
        
        request.setAttribute("stile", opt.get());
        request.getRequestDispatcher(FORM_JSP).forward(request, response);
        
        
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        
         // 1) Parse seguro
    Integer id        = parseIntSafe(request.getParameter("id"));
    String nome = request.getParameter("nome");
    String descrizione = request.getParameter("descrizione");
    
    boolean isInsert = (id == null || id == 0);
    
    // 2) Validación
    List<String> errors = new ArrayList<>();
    if (nome == null)    errors.add("Nome obbligatorio ");      
   

    if (!isInsert) {
      // si es update, confirma existencia
      if (id == null || stileDao.findById(id).isEmpty()) {
        errors.add("L'origine da aggiornare non esiste");
      }
    } 
    
    Stile bean = buildStile(id, nome, descrizione);
    
    if (!errors.isEmpty()) {
      request.setAttribute("errors", errors);
      request.setAttribute("stile", bean);      
      request.getRequestDispatcher(FORM_JSP).forward(request, response);
      return;
    }
    
    // 3) Persistencia (usa tus metodos separati o uno solo save)
    int result;
      if (isInsert) {
          result = stileDao.creare(bean);  // debe devolver 1 y setear ID si usas generated keys
      } else {
          result = stileDao.modificare(bean);
      } // p.ej. SKU duplicado

    if (result > 0) {
      request.getSession().setAttribute("success", "Stile salvato correttamente");
      response.sendRedirect(request.getContextPath() + "/StiliListServlet?action=listar&ok=1");
    } else {
      request.getSession().setAttribute("error", "Salvataggio non riuscito");
      request.setAttribute("stile", bean);      
      request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }    
    
        
    }
    
    
    

    
    // helpers
    
    private static Stile buildStile(Integer id, String nome, String descrizione) {
        Stile s = new Stile();
        s.setId(id == null ? 0 : id);
        s.setNome(nome);        
        s.setDescrizione(descrizione);        
        return s;
    }
    
    
    private static Integer parseIntSafe(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String trimOrNull(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(StiliCreateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(StiliCreateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
