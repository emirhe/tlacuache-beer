package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.dao.mapper.ProdottoMapper;
import com.emirhg.sistema.ecommerce.dao.mapper.RowMapper;
import com.emirhg.sistema.ecommerce.dao.repository.ProdottoRepository;
import com.emirhg.sistema.ecommerce.model.Prodotto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdottoDao implements ProdottoRepository {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final RowMapper<Prodotto> mapper = new ProdottoMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT
      p.id          AS p_id,
      p.sku         AS p_sku,
      p.volume_ml   AS p_volume_ml,
      p.pack_size   AS p_pack_size,
      p.prezzo      AS p_prezzo,
      p.is_active   AS p_active,
      p.stock       AS p_stock,
      p.url_img     AS p_url_img,
    
      b.id          AS b_id,
      b.nome        AS b_nome,
      b.gradazione  AS b_gradazione,
      b.senza_glutine AS b_senza_glutine,
      b.biologico   AS b_biologico,
    
      s.id          AS s_id,
      s.nome        AS s_nome,
      s.descrizione AS s_descrizione,
    
      o.id          AS o_id,
      o.nome        AS o_nome,
    
      pr.id         AS pr_id,
      pr.nome       AS pr_nome,
    
      f.id          AS f_id,
      f.codice      AS f_codice
    FROM prodotto p
    JOIN birra b        ON b.id = p.birra_id
    JOIN stile s        ON s.id = b.stile_id
    JOIN origine o      ON o.id = b.origine_id
    JOIN produttore pr  ON pr.id = b.produttore_id
    JOIN formato f      ON f.id = p.formato_id
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY p.id ASC";
    private static final String SELECT_BY_SKU = SELECT_BASE + " WHERE p.sku = ?";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE p.id = ?";
    private static final String SELECT_BY_ACTIVE = SELECT_BASE + " WHERE p.is_active = 1 ORDER BY p.id";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO prodotto (birra_id, sku, formato_id, volume_ml, pack_size, prezzo, is_active, stock, url_img)
                VALUES (?, ?, ?, ?, ?, ?, ?,?,?)        
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
         UPDATE prodotto
                SET birra_id=?, sku=?, formato_id=?, volume_ml=?, pack_size=?,
                  prezzo=?, is_active=?, stock=?, url_img=?
                WHERE id=?
        """;

    //UPDATE stock
    private static final String UPDATE_STOCK_SQL = """
                                                   UPDATE prodotto
                                                   SET stock = stock - ? WHERE id=? AND stock >=?
                                                   """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM prodotto WHERE id=?";

    // DESATTIVARE
    private static final String DEACTIVATE_SQL = "UPDATE prodotto SET is_active = 0 WHERE id=? AND is_active = 1";
    // REATTIVARE
    private static final String REACTIVATE_SQL = "UPDATE prodotto SET is_active = 1 WHERE id=? AND is_active = 0";

    // Read
    @Override
    public List<Prodotto> findAll() {
        List<Prodotto> prodotti = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Prodotto prodotto = mapper.map(rs);
                prodotti.add(prodotto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;

    }

    @Override
    public Optional<Prodotto> findBySku(String sku) throws SQLException {

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_SKU)) {

            stmt.setString(1, sku);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapper.map(rs));

            }

        }

    }

    @Override
    public Optional<Prodotto> findById(int id) throws SQLException {

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapper.map(rs));

            }

        }

    }

    @Override
    public List<Prodotto> findAllActive() throws Exception {

        List<Prodotto> prodotti = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_BY_ACTIVE)) {

            while (rs.next()) {
                Prodotto prodotto = mapper.map(rs);
                prodotti.add(prodotto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;

    }

    // CREATE / UPDATE
    @Override
    public Integer save(Prodotto p) {

        // INSERT si id == 0, sino UPDATE
        if (p.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, p.getBirra().getId());
                ps.setString(2, p.getSku());
                ps.setInt(3, p.getFormato().getId());
                ps.setInt(4, p.getVolumeMl());
                ps.setInt(5, p.getPackSize());
                ps.setDouble(6, p.getPrezzo());
                ps.setBoolean(7, p.isActive());
                ps.setInt(8, p.getStock());
                ps.setString(9, p.getUrlImg());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        p.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT prodotto");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar prodotto", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setInt(1, p.getBirra().getId());
                ps.setString(2, p.getSku());
                ps.setInt(3, p.getFormato().getId());
                ps.setInt(4, p.getVolumeMl());
                ps.setInt(5, p.getPackSize());
                ps.setDouble(6, p.getPrezzo());
                ps.setBoolean(7, p.isActive());
                ps.setInt(8, p.getStock());
                ps.setString(9, p.getUrlImg());
                ps.setInt(10, p.getId());

                ps.executeUpdate();
                return p.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar prodotto id=" + p.getId(), e);
            }
        }

    }

    // DELETE
    @Override
    public boolean deleteById(int id) throws SQLException {

        try (PreparedStatement stmt = getConnection().prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1; //true se è stata cancellata la fila
        }

    }

    @Override
    public boolean decrementaStock(int id, int qty) throws Exception {

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_STOCK_SQL)) {

            ps.setInt(1, qty); // resta
            ps.setInt(2, id);  // id
            ps.setInt(3, qty); // check stock >= qty 
            return ps.executeUpdate() == 1;
        }

    }

    //DESATTIVAZIONE
    @Override
    public boolean deactivateById(int id) throws Exception {

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(DEACTIVATE_SQL)) {

            ps.setInt(1, id);

            return ps.executeUpdate() == 1;
        }

    }

    @Override
    public boolean reactivateById(int id) throws Exception {

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(REACTIVATE_SQL)) {

            ps.setInt(1, id);

            return ps.executeUpdate() == 1;
        }

    }

    @Override
    public int creare(Prodotto p)  {

        int result = 0;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, p.getBirra().getId());
            ps.setString(2, p.getSku());
            ps.setInt(3, p.getFormato().getId());
            ps.setInt(4, p.getVolumeMl());
            ps.setInt(5, p.getPackSize());
            ps.setDouble(6, p.getPrezzo());
            ps.setBoolean(7, p.isActive());
            ps.setInt(8, p.getStock());
            ps.setString(9, p.getUrlImg());

            result = ps.executeUpdate();           
            if (result == 0) {
                throw new SQLException("Non si ha inserito nessun registro");
            }
            
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar prodotto", e);
        }
        
        return result;

    }

    @Override
    public int modificare(Prodotto p) {
        int result=0;
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setInt(1, p.getBirra().getId());
                ps.setString(2, p.getSku());
                ps.setInt(3, p.getFormato().getId());
                ps.setInt(4, p.getVolumeMl());
                ps.setInt(5, p.getPackSize());
                ps.setDouble(6, p.getPrezzo());
                ps.setBoolean(7, p.isActive());
                ps.setInt(8, p.getStock());
                ps.setString(9, p.getUrlImg());
                ps.setInt(10, p.getId());

                result = ps.executeUpdate();
                
                if (result == 0) {
                throw new SQLException("Non si ha inserito nessun registro");
            }
                
              
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar prodotto id=" + p.getId(), e);
            }
        
        return result;
        
            }

}
