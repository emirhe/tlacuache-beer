package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.controllers.exceptions.ReferentialIntegrityException;
import com.emirhg.sistema.ecommerce.dao.mapper.BirraMapper;
import com.emirhg.sistema.ecommerce.dao.repository.BirraRepository;
import com.emirhg.sistema.ecommerce.model.Birra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

public class BirraDao implements BirraRepository {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final BirraMapper mapper = new BirraMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT
      b.id              AS b_id,
      b.nome            AS b_nome,
      b.gradazione      AS b_gradazione,
      b.senza_glutine   AS b_senza_glutine,
      b.biologico       AS b_biologico,

      s.id              AS s_id,
      s.nome            AS s_nome,
      s.descrizione     AS s_descrizione,

      o.id              AS o_id,
      o.nome            AS o_nome,

      pr.id             AS pr_id,
      pr.nome           AS pr_nome
    FROM birra b
    JOIN stile s        ON s.id = b.stile_id
    JOIN origine o      ON o.id = b.origine_id
    JOIN produttore pr  ON pr.id = b.produttore_id
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY b.id ASC";
    private static final String SELECT_BY_NOME = SELECT_BASE + " WHERE b.nome LIKE ? ORDER BY b.nome";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE b.id = ?";
    private static final String SELECT_BY_STILE_ID = SELECT_BASE + " WHERE s.id LIKE ? ORDER BY b.nome";
    private static final String SELECT_BY_STILE_NAME = SELECT_BASE + " WHERE s.nome LIKE ? ORDER BY b.nome";
    private static final String SELECT_BY_ORIGINE = SELECT_BASE + " WHERE o.nome LIKE ? ORDER BY b.nome";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO birra (nome, stile_id, produttore_id, gradazione, senza_glutine, biologico, origine_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)     
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
        UPDATE birra
                SET nome=?, stile_id=?, produttore_id=?, gradazione=?, senza_glutine=?,
                  biologico=?, origine_id=?
                WHERE id=?
        """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM birra WHERE id=?";
    
    // conto referenze
    private static final String COUNT_PRODOTTI_USING_BIRRA = "SELECT COUNT(*) FROM prodotto WHERE birra_id = ?";
    

//READ
    @Override
    public Optional<Birra> findById(int id) throws Exception {

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
    public List<Birra> findByNomeLike(String q) {

        List<Birra> birre = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NOME)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    birre.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birre;

    }

    @Override
    public List<Birra> listByStile(int stileId) {
        List<Birra> birre = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STILE_ID)) {

            stmt.setInt(1, stileId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    birre.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birre;

    }

    @Override
    public List<Birra> listByStileNome(String q) throws Exception {
        List<Birra> birre = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STILE_NAME)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    birre.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birre;

    }

    @Override
    public List<Birra> findAll() {
        List<Birra> birre = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Birra birra = mapper.map(rs);
                birre.add(birra);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birre;

    }

    //CREATE / UPDATE / DELETE
    @Override
    public Integer save(Birra b) {

        // INSERT si id == 0, sino UPDATE
        if (b.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, b.getNome());
                ps.setInt(2, b.getStile().getId());
                ps.setInt(3, b.getProduttore().getId());
                ps.setDouble(4, b.getGradazione());
                ps.setBoolean(5, b.isSenzaGlutine());
                ps.setBoolean(6, b.isBiologico());
                ps.setInt(7, b.getOrigine().getId());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        b.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT birra");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar birra", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, b.getNome());
                ps.setInt(2, b.getStile().getId());
                ps.setInt(3, b.getProduttore().getId());
                ps.setDouble(4, b.getGradazione());
                ps.setBoolean(5, b.isSenzaGlutine());
                ps.setBoolean(6, b.isBiologico());
                ps.setInt(7, b.getOrigine().getId());
                ps.setInt(8, b.getId());

                ps.executeUpdate();
                return b.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar birra id=" + b.getId(), e);
            }
        }

    }

    
   private boolean isFkViolation(SQLException e) {
        // MySQL: SQLState 23000 (violación integridad); 1451 = cannot delete/update parent row
        return "23000".equals(e.getSQLState()) && e.getErrorCode() == 1451;
    }

    private boolean isBirraInUse(int birraId) throws SQLException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(COUNT_PRODOTTI_USING_BIRRA)) {
            ps.setInt(1, birraId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    
    @Override
    public boolean deleteById(int id) throws Exception {

        // 1) Chequeo preventivo (UX mejor)
        if (isBirraInUse(id)) {
            throw new DomainConstraintException(
                "Imposibile eliminar la birra perche ci sono prodotti collegati."
            );
        }

        // 2) Intento de borrar (por si otro hilo/tx insertó algo en el medio)
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isFkViolation(e)) {
                throw new DomainConstraintException(
                    "Imposibile eliminar la birra perche ci sono prodotti collegati.", e
                );
            }
            throw e; // otro error SQL real
        }
    }

    

    /*
    Questo metodo costruisce il pattern per la ricerca LIKE:
    che aiuta alla ricerca per cercare se "contiene"    
     */
    private static String like(String q) {
        if (q == null) {
            return "%"; //qualsiasi secuenza de caratteri
        }

        q = q.trim(); //cancella gli spazi
        if (q.isEmpty()) {
            return "%";
        }

        return "%" + q + "%";
    }

    @Override
    public int creare(Birra b) {
        
        int result=0;
        
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

                ps.setString(1, b.getNome());
                ps.setInt(2, b.getStile().getId());
                ps.setInt(3, b.getProduttore().getId());
                ps.setDouble(4, b.getGradazione());
                ps.setBoolean(5, b.isSenzaGlutine());
                ps.setBoolean(6, b.isBiologico());
                ps.setInt(7, b.getOrigine().getId());

                result = ps.executeUpdate();
                
            } catch (SQLException e) {
                throw new RuntimeException("Errore di creazione birra", e);
            }
        
        return result;
        
            }

    @Override
    public int modificare(Birra b) throws Exception {
        int result = 0;
         try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, b.getNome());
                ps.setInt(2, b.getStile().getId());
                ps.setInt(3, b.getProduttore().getId());
                ps.setDouble(4, b.getGradazione());
                ps.setBoolean(5, b.isSenzaGlutine());
                ps.setBoolean(6, b.isBiologico());
                ps.setInt(7, b.getOrigine().getId());
                ps.setInt(8, b.getId());

                result= ps.executeUpdate();
                
            } catch (SQLException e) {
                throw new RuntimeException("Error di modificazione id=" + b.getId(), e);
            }
        return result;
        
           }

}
