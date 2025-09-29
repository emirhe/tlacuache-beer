
package com.emirhg.sistema.ecommerce.dao.repository;

import com.emirhg.sistema.ecommerce.model.Birra;
import java.util.List;

public interface BirraRepository extends Repository<Birra,Integer> {
    
    List<Birra> findByNomeLike(String q) throws Exception;
    List<Birra> listByStile(int stileId) throws Exception;
    List<Birra> listByStileNome(String stileNome) throws Exception;
    
}
