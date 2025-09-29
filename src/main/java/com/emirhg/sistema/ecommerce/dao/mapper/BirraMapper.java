
package com.emirhg.sistema.ecommerce.dao.mapper;

import com.emirhg.sistema.ecommerce.model.Birra;
import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import com.emirhg.sistema.ecommerce.model.cataloghi.Produttore;
import com.emirhg.sistema.ecommerce.model.cataloghi.Stile;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BirraMapper implements RowMapper<Birra> {

    @Override
    public Birra map(ResultSet rs) throws SQLException {
        
        // Cataloghi
        Stile stile = new Stile();
        stile.setId(rs.getInt("s_id"));
        stile.setNome(rs.getString("s_nome"));
        stile.setDescrizione(rs.getString("s_descrizione"));
        
        Origine origine = new Origine();
        origine.setId(rs.getInt("o_id"));
        origine.setNome(rs.getString("o_nome"));
        
        Produttore produttore = new Produttore();
        produttore.setId(rs.getInt("pr_id"));
        produttore.setNome(rs.getString("pr_nome"));
        
        //Birra utilizzando le istanze di sopra
        Birra b = new Birra();
        b.setId(rs.getInt("b_id"));
        b.setNome(rs.getString("b_nome"));
        
        double grad = rs.getDouble("b_gradazione");
        if (rs.wasNull()) grad = 0.0;
        b.setGradazione(grad);
        
        b.setSenzaGlutine(rs.getBoolean("b_senza_glutine"));
        b.setBiologico(rs.getBoolean("b_biologico"));
        
        b.setStile(stile);
        b.setOrigine(origine);
        b.setProduttore(produttore);
        
        return b;
        
    }
    
}
