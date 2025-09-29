package com.emirhg.sistema.ecommerce.dao.mapper;

import com.emirhg.sistema.ecommerce.model.cataloghi.Stile;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StileMapper implements RowMapper<Stile> {

    @Override
    public Stile map(ResultSet rs) throws SQLException {

        Stile s = new Stile();
        s.setId(rs.getInt("id"));
        s.setNome(rs.getString("nome"));
        s.setDescrizione(rs.getString("descrizione"));

        return s;

    }

}
