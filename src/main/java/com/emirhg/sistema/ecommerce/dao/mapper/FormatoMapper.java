
package com.emirhg.sistema.ecommerce.dao.mapper;

import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FormatoMapper implements RowMapper<Formato> {

    @Override
    public Formato map(ResultSet rs) throws SQLException {
        
        Formato f = new Formato();
        f.setId(rs.getInt("id"));
        f.setCodice(rs.getString("codice"));
        
        return f;       
        
        
        
    }
    
}
