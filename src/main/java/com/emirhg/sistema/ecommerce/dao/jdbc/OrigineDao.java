package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.mapper.OrigineMapper;
import com.emirhg.sistema.ecommerce.dao.repository.RepositoryCataloghi;
import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrigineDao implements RepositoryCataloghi<Origine> {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final OrigineMapper mapper = new OrigineMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT id, nome FROM origine  
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY id ASC";
    private static final String SELECT_BY_NOME = SELECT_BASE + " WHERE nome LIKE ? ORDER BY nome";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO origine (nome)
                VALUES (?)     
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
        UPDATE origine
                SET nome=?
                WHERE id=?
        """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM origine WHERE id=?";

    // conto referenze
    private static final String COUNT_PRODOTTI_USING_ORIGINE = "SELECT COUNT(*) FROM birra WHERE origine_id = ?";

    @Override
    public List<Origine> findAll() {
        List<Origine> origini = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                origini.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return origini;

    }

    @Override
    public List<Origine> findByNome(String q) throws Exception {
        List<Origine> origini = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NOME)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    origini.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return origini;

    }

    @Override
    public Optional<Origine> findById(int id) throws Exception {

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
    public Integer save(Origine o) throws Exception {

        // INSERT si id == 0, sino UPDATE
        if (o.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, o.getNome());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        o.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT origine");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar origine", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, o.getNome());
                ps.setInt(2, o.getId());

                ps.executeUpdate();
                return o.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar origine id=" + o.getId(), e);
            }
        }

    }

    @Override
    public int creare(Origine o) {

        int result = 0;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

            ps.setString(1, o.getNome());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di creazione origine", e);
        }

        return result;

    }

    @Override
    public int modificare(Origine o) {

        int result = 0;
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, o.getNome());
            ps.setInt(2, o.getId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di modificazione id=" + o.getId(), e);
        }
        return result;

    }

    //check per delete   
    private boolean isFkViolation(SQLException e) {
        // MySQL: SQLState 23000 (violación integridad); 1451 = cannot delete/update parent row
        return "23000".equals(e.getSQLState()) && e.getErrorCode() == 1451;
    }

    private boolean isOrigineInUse(int origineId) throws SQLException {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(COUNT_PRODOTTI_USING_ORIGINE)) {
            ps.setInt(1, origineId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        // 1) Chequeo preventivo (UX mejor)
        if (isOrigineInUse(id)) {
            throw new DomainConstraintException(
                    "Imposibile eliminar l'origine perche ci sono birre collegate."
            );
        }

        // 2) Intento de borrar (por si otro hilo/tx insertó algo en el medio)
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isFkViolation(e)) {
                throw new DomainConstraintException(
                        "Imposibile eliminar l'origine perche ci sono birre collegate.", e
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
