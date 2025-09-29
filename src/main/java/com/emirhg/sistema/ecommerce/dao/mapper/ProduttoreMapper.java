package com.emirhg.sistema.ecommerce.dao.mapper;

import com.emirhg.sistema.ecommerce.model.cataloghi.Produttore;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduttoreMapper implements RowMapper<Produttore> {

    @Override
    public Produttore map(ResultSet rs) throws SQLException {

        Produttore pr = new Produttore();
        pr.setId(rs.getInt("id"));
        pr.setNome(rs.getString("nome"));
        pr.setIndirizzo(rs.getString("indirizzo"));
        pr.setWebsite(rs.getString("website"));

        return pr;

    }

}
