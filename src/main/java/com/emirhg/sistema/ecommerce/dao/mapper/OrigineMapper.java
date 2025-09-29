package com.emirhg.sistema.ecommerce.dao.mapper;

import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrigineMapper implements RowMapper<Origine> {

    @Override
    public Origine map(ResultSet rs) throws SQLException {

        Origine o = new Origine();
        o.setId(rs.getInt("id"));
        o.setNome(rs.getString("nome"));

        return o;

    }

}
