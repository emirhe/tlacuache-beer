package com.emirhg.sistema.ecommerce.app;

import com.emirhg.sistema.ecommerce.dao.jdbc.BirraDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.FormatoDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao;
import com.emirhg.sistema.ecommerce.dao.jdbc.StileDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            var list = new ProdottoDao().findAll(); // o findAllActive()
            sce.getServletContext().setAttribute("prodotti", list);
            sce.getServletContext().log("AppInitializer OK → prodotti=" + list.size());
            
            //precaricare catologhi
            var formati = new FormatoDao().findAll();
            sce.getServletContext().setAttribute("formati", formati);
            
            var stili = new StileDao().findAll();
            sce.getServletContext().setAttribute("stili", stili);
            
            
        } catch (Throwable t) { // ← captura TODO
            sce.getServletContext().setAttribute("prodotti", java.util.List.of());
            sce.getServletContext().setAttribute("formati", java.util.List.of());
            sce.getServletContext().setAttribute("stili", java.util.List.of());            
            sce.getServletContext().log("AppInitializer ERROR", t); // NO relances
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
