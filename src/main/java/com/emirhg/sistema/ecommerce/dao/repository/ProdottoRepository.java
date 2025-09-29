
package com.emirhg.sistema.ecommerce.dao.repository;

import com.emirhg.sistema.ecommerce.model.Prodotto;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface ProdottoRepository extends Repository<Prodotto,Integer> {
    
    Optional<Prodotto> findBySku(String sku) throws Exception;
    
    List<Prodotto> findAllActive() throws Exception;
    
    boolean decrementaStock(int id, int qty) throws Exception;
    
    boolean deactivateById(int id) throws Exception;
    
    boolean reactivateById(int id) throws Exception;
    
   
    
    
}
