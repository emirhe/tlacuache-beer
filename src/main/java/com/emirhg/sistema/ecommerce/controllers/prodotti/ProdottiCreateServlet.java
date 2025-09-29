package com.emirhg.sistema.ecommerce.controllers.prodotti;

import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.FormatoDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import com.emirhg.sistema.ecommerce.model.Birra;
import com.emirhg.sistema.ecommerce.model.Prodotto;
import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProdottiCreateServlet", urlPatterns = {"/ProdottiCreateServlet"})
public class ProdottiCreateServlet extends HttpServlet {

  private final ProdottoDao prodottoDao = new ProdottoDao();
  private final BirraDao birraDao = new BirraDao();
  private final FormatoDao formatoDao = new FormatoDao();

  private static final String FORM_JSP = "/views/prodotti/form.jsp";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      processRequest(request, response);
    } catch (Exception e) {
      // No nos comemos la excepción: la propagamos para ver el error en el navegador/log
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      processRequest(request, response);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
      
      String action = request.getParameter("action");
        if (action == null || action.isBlank()) action = "nuovo";   
    
   

    switch (action) {
      case "nuovo" -> showNewForm(request, response);
      case "modifica" -> showEditForm(request, response);
      case "salvare" -> save(request, response);
      default -> {
        request.getSession().setAttribute("error", "Azione non valida");
        response.sendRedirect(request.getContextPath() + "/ProdottiListServlet");
      }
    }
  }

  // ------ VISTAS ------

  private void showNewForm(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    request.setAttribute("prodotto", new Prodotto()); // id=0 => INSERT
    loadCatalogs(request);
    request.getRequestDispatcher(FORM_JSP).forward(request, response);
  }

  private void showEditForm(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Integer id = parseIntSafe(request.getParameter("id"));
    if (id == null || id <= 0) {
      request.getSession().setAttribute("error", "ID non valido");
      response.sendRedirect(request.getContextPath() + "/ProdottiListServlet");
      return;
    }

    var opt = prodottoDao.findById(id);
    if (opt.isEmpty()) {
      request.getSession().setAttribute("error", "Prodotto id " + id + " non trovato");
      response.sendRedirect(request.getContextPath() + "/ProdottiListServlet");
      return;
    }

    request.setAttribute("prodotto", opt.get());
    loadCatalogs(request);
    request.getRequestDispatcher(FORM_JSP).forward(request, response);
  }

  // ------ GUARDAR ------

  private void save(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    // 1) Parse seguro
    Integer id        = parseIntSafe(request.getParameter("id"));
    Integer birraId   = parseIntSafe(request.getParameter("birraId"));
    String  sku       = trimOrNull(request.getParameter("sku"));
    Integer formatoId = parseIntSafe(request.getParameter("formatoId"));
    Integer volume    = parseIntSafe(request.getParameter("volumeMl"));
    Integer packSize  = parseIntSafe(request.getParameter("packSize"));
    Double  prezzo    = parseDoubleSafe(request.getParameter("prezzo"));
    Integer stock     = parseIntSafe(request.getParameter("stock"));
    boolean active    = parseCheckbox(request.getParameter("active")); // "1"/"on"/"true" => true
    String  urlImg    = trimOrNull(request.getParameter("urlImg"));

    boolean isInsert = (id == null || id == 0);

    System.out.println("[ProdottiCreateServlet] SALVARE isInsert=" + isInsert
        + " id=" + id + " sku=" + sku + " birraId=" + birraId
        + " formatoId=" + formatoId + " prezzo=" + prezzo);

    // 2) Validación
    List<String> errors = new ArrayList<>();
    if (birraId == null)    errors.add("Seleziona una birra");
    if (formatoId == null)  errors.add("Seleziona un formato");
    if (sku == null)        errors.add("SKU obbligatorio");
    if (volume == null)     errors.add("Volume obbligatorio");
    if (packSize == null)   errors.add("PackSize obbligatorio");
    if (prezzo == null)     errors.add("Prezzo obbligatorio");

    if (!isInsert) {
      // si es update, confirma existencia
      if (id == null || prodottoDao.findById(id).isEmpty()) {
        errors.add("Il prodotto da aggiornare non esiste");
      }
    }

    Prodotto bean = buildProdotto(id, birraId, sku, formatoId, volume, packSize, prezzo, stock, active, urlImg);

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
          result = prodottoDao.creare(bean);  // debe devolver 1 y setear ID si usas generated keys
      } else {
          result = prodottoDao.modificare(bean);
      } // p.ej. SKU duplicado

    if (result > 0) {
      request.getSession().setAttribute("success", "Prodotto salvato correttamente");
      response.sendRedirect(request.getContextPath() + "/ProdottiListServlet?action=listar&ok=1");
    } else {
      request.getSession().setAttribute("error", "Salvataggio non riuscito");
      request.setAttribute("prodotto", bean);
      loadCatalogs(request);
      request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }
  }

  // ------ helpers ------

  private void loadCatalogs(HttpServletRequest request) throws Exception {
    request.setAttribute("birre", birraDao.findAll());
    request.setAttribute("formati", formatoDao.findAll());
  }

  private static Prodotto buildProdotto(Integer id, Integer birraId, String sku, Integer formatoId,
                                        Integer volume, Integer packSize, Double prezzo, Integer stock,
                                        boolean active, String urlImg) {
    Prodotto p = new Prodotto();
    p.setId(id == null ? 0 : id);
    p.setSku(sku);
    p.setVolumeMl(volume == null ? 0 : volume);
    p.setPackSize(packSize == null ? 0 : packSize);
    p.setPrezzo(prezzo == null ? 0.0 : prezzo);
    p.setStock(stock == null ? 0 : stock);
    p.setActive(active);
    p.setUrlImg(urlImg);

    Birra b = new Birra(); b.setId(birraId == null ? 0 : birraId); p.setBirra(b);
    Formato f = new Formato(); f.setId(formatoId == null ? 0 : formatoId); p.setFormato(f);
    return p;
  }

  private static Integer parseIntSafe(String s) {
    try {
      return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static Double parseDoubleSafe(String s) {
    if (s == null || s.isBlank()) return null;
    s = s.trim().replace(',', '.');
    try { return Double.valueOf(s); } catch (Exception e) { return null; }
  }

  private static boolean parseCheckbox(String v) {
    if (v == null) return false;
    v = v.trim().toLowerCase();
    return v.equals("1") || v.equals("on") || v.equals("true") || v.equals("yes") || v.equals("y");
  }

  
  private static String trimOrNull(String s) {
    if (s == null) return null;
    s = s.trim();
    return s.isEmpty() ? null : s;
  }
}
