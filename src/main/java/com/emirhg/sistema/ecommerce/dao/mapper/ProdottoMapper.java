
package com.emirhg.sistema.ecommerce.dao.mapper;


import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
import com.emirhg.sistema.ecommerce.model.Prodotto;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProdottoMapper implements RowMapper<Prodotto>{
    private final BirraMapper birraMapper = new BirraMapper();
   
    @Override
    public Prodotto map(ResultSet rs) throws SQLException {
        
         


        // Entità Prodotto
        Prodotto p = new Prodotto();
        p.setId(rs.getInt("p_id"));        
        p.setSku(rs.getString("p_sku"));        
        p.setVolumeMl(rs.getInt("p_volume_ml"));
        p.setPackSize(rs.getInt("p_pack_size")); // si es short en tu POJO, usa getShort
        p.setPrezzo(rs.getDouble("p_prezzo"));
        p.setActive(rs.getBoolean("p_active"));
        p.setStock(rs.getInt("p_stock"));
        p.setUrlImg(rs.getString("p_url_img"));
        
        
         // Mappa Birra con su mapper
        p.setBirra(birraMapper.map(rs)); 
        

        // Entità Cataloghi / relazionati       

        Formato f = new Formato();
        f.setId(rs.getInt("f_id"));
        f.setCodice(rs.getString("f_codice")); 
        p.setFormato(f);
        
        return p;
        
        
    }
    
}
