package com.emirhg.sistema.ecommerce.controllers.birre;

import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.OrigineDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.ProduttoreDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.StileDao;
import com.emirhg.sistema.ecommerce.model.Birra;
import com.emirhg.sistema.ecommerce.model.Prodotto;
import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import com.emirhg.sistema.ecommerce.model.cataloghi.Produttore;
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

@WebServlet(name = "BirreCreateServlet", urlPatterns = {"/BirreCreateServlet"})
public class BirreCreateServlet extends HttpServlet {

    private BirraDao birraDao = new BirraDao();
    private StileDao stileDao = new StileDao();
    private OrigineDao origineDao = new OrigineDao();
    private ProduttoreDao produttoreDao = new ProduttoreDao();

    private static final String FORM_JSP = "/views/birre/form.jsp";

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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        request.setAttribute("birra", new Birra()); // id=0 => INSERT
        loadCatalogs(request);
        request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }

    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
  
     Integer id = parseIntSafe(request.getParameter("id"));
    if (id == null || id <= 0) {
      request.getSession().setAttribute("error", "ID non valido");
      response.sendRedirect(request.getContextPath() + "/BirreListServlet");
      return;
    }

    var opt = birraDao.findById(id);
    if (opt.isEmpty()) {
      request.getSession().setAttribute("error", "Birra id " + id + " non trovato");
      response.sendRedirect(request.getContextPath() + "/BirraListServlet");
      return;
    }

    request.setAttribute("birra", opt.get());
    loadCatalogs(request);
    request.getRequestDispatcher(FORM_JSP).forward(request, response);
    
    }
    
    

    
    private void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
 
    // 1) Parse seguro
    Integer id        = parseIntSafe(request.getParameter("id"));
    String nome = request.getParameter("nome");
    Double gradazione = parseDoubleSafe(request.getParameter("gradazione"));
    Integer stileId = parseIntSafe(request.getParameter("stileId"));
    Integer origineId = parseIntSafe(request.getParameter("origineId"));
    boolean senzaGlutine = parseCheckbox(request.getParameter("senzaGlutine"));
    boolean biologico = parseCheckbox(request.getParameter("biologico"));
    Integer produttoreId = parseIntSafe(request.getParameter("produttoreId"));
        
    boolean isInsert = (id == null || id == 0);

    // 2) Validación
    List<String> errors = new ArrayList<>();
    if (stileId == null)    errors.add("Seleziona un stile");
    if (origineId == null)  errors.add("Seleziona un origine");
    if (nome == null)        errors.add("Nome obbligatorio");    

    if (!isInsert) {
      // si es update, confirma existencia
      if (id == null || birraDao.findById(id).isEmpty()) {
        errors.add("La birra da aggiornare non esiste");
      }
    }  
    

    Birra bean = buildBirra(id, nome, gradazione, stileId, origineId, senzaGlutine, biologico, produttoreId);
    
    if (!errors.isEmpty()) {
      request.setAttribute("errors", errors);
      request.setAttribute("prodotto", bean);
      loadCatalogs(request);
      request.getRequestDispatcher(FORM_JSP).forward(request, response);
      return;
    }
    
    // 3) Persistencia (usa tus metodos separati o uno solo save)
    int result;
      if (isInsert) {
          result = birraDao.creare(bean);  // debe devolver 1 y setear ID si usas generated keys
      } else {
          result = birraDao.modificare(bean);
      } // p.ej. SKU duplicado

    if (result > 0) {
      request.getSession().setAttribute("success", "Birra salvata correttamente");
      response.sendRedirect(request.getContextPath() + "/BirreListServlet?action=listar&ok=1");
    } else {
      request.getSession().setAttribute("error", "Salvataggio non riuscito");
      request.setAttribute("birra", bean);
      loadCatalogs(request);
      request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }    
    
        
    
    }

    
    // ------ helpers ------
    private void loadCatalogs(HttpServletRequest request) throws Exception {
        request.setAttribute("stili", stileDao.findAll());
        request.setAttribute("origini", origineDao.findAll());
        request.setAttribute("produttori", produttoreDao.findAll());
    }

    private static Birra buildBirra(Integer id, String nome, Double gradazione, Integer stileId,
            Integer origineId, boolean senzaGlutine, boolean biologico, Integer produttoreId) {
        Birra b = new Birra();
        b.setId(id == null ? 0 : id);
        b.setNome(nome);
        b.setGradazione(gradazione == null ? 0.0 : gradazione);
        b.setSenzaGlutine(senzaGlutine);
        b.setBiologico(biologico);

        Stile s = new Stile();
        s.setId(stileId == null ? 0 : stileId);
        b.setStile(s);
        Origine o = new Origine();
        o.setId(origineId == null ? 0 : origineId);
        b.setOrigine(o);
        Produttore pr = new Produttore();
        pr.setId(produttoreId == null ? 0 : produttoreId);
        b.setProduttore(pr);
        return b;
    }

    private static Integer parseIntSafe(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseDoubleSafe(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        s = s.trim().replace(',', '.');
        try {
            return Double.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean parseCheckbox(String v) {
        if (v == null) {
            return false;
        }
        v = v.trim().toLowerCase();
        return v.equals("1") || v.equals("on") || v.equals("true") || v.equals("yes") || v.equals("y");
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
            Logger.getLogger(BirreCreateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(BirreCreateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
