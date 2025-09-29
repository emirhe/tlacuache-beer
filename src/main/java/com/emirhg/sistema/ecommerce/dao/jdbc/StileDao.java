package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.mapper.StileMapper;
import com.emirhg.sistema.ecommerce.dao.repository.RepositoryCataloghi;

import com.emirhg.sistema.ecommerce.model.cataloghi.Stile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StileDao implements RepositoryCataloghi<Stile> {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final StileMapper mapper = new StileMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT id, nome, descrizione FROM stile  
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY id ASC";
    private static final String SELECT_BY_NOME = SELECT_BASE + " WHERE nome LIKE ? ORDER BY nome";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO stile (nome, descrizione)
                VALUES (?, ?)     
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
        UPDATE stile
                SET nome=?, descrizione=?
                WHERE id=?
        """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM stile WHERE id=?";

    // conto referenze
    private static final String COUNT_PRODOTTI_USING_STILE = "SELECT COUNT(*) FROM birra WHERE stile_id = ?";

    @Override
    public List findAll() {
        List<Stile> stili = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                stili.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stili;

    }

    @Override
    public List findByNome(String q) throws Exception {

        List<Stile> stili = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NOME)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stili.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stili;

    }

    @Override
    public Optional findById(int id) throws Exception {

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
    public Integer save(Stile s) throws Exception {

        // INSERT si id == 0, sino UPDATE
        if (s.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, s.getNome());
                ps.setString(2, s.getDescrizione());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        s.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT stile");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar stile", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, s.getNome());
                ps.setString(2, s.getDescrizione());
                ps.setInt(3, s.getId());

                ps.executeUpdate();
                return s.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar stile id=" + s.getId(), e);
            }
        }

    }

    @Override
    public int creare(Stile s) {
         int result = 0;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescrizione());
            

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di creazione stile", e);
        }

        return result;
    }

    @Override
    public int modificare(Stile s) {
         int result = 0;
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescrizione());          
            ps.setInt(3, s.getId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di modificazione id=" + s.getId(), e);
        }
        return result;
    }

    //check per delete   
    private boolean isFkViolation(SQLException e) {
        // MySQL: SQLState 23000 (violación integridad); 1451 = cannot delete/update parent row
        return "23000".equals(e.getSQLState()) && e.getErrorCode() == 1451;
    }

    private boolean isStileInUse(int stileId) throws SQLException {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(COUNT_PRODOTTI_USING_STILE)) {
            ps.setInt(1, stileId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        // 1) Chequeo preventivo (UX mejor)
        if (isStileInUse(id)) {
            throw new DomainConstraintException(
                    "Imposibile eliminar lo stile perche ci sono birre collegate."
            );
        }

        // 2) Intento de borrar (por si otro hilo/tx insertó algo en el medio)
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isFkViolation(e)) {
                throw new DomainConstraintException(
                        "Imposibile eliminar lo stile perche ci sono birre collegate.", e
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

}
