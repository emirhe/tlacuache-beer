
package com.emirhg.sistema.ecommerce.dao.repository;

import java.util.List;
import java.util.Optional;


public interface Repository <T, ID> {
    
    List<T> findAll() throws Exception;
    
    Optional<T> findById(int id) throws Exception;
    
     int creare(T t) throws Exception;
    
    int modificare(T t) throws Exception;
    
    
    // crea o aggiorna. Ritorna il ID
    ID save(T t) throws Exception;
    
    // true si 1 fila viene affetatta
    boolean deleteById(int id) throws Exception;
    
    
    
}
