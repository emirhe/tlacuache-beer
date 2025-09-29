package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.mapper.ProduttoreMapper;
import com.emirhg.sistema.ecommerce.dao.repository.RepositoryCataloghi;
import com.emirhg.sistema.ecommerce.model.cataloghi.Produttore;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduttoreDao implements RepositoryCataloghi<Produttore> {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final ProduttoreMapper mapper = new ProduttoreMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT id, nome, indirizzo, website FROM produttore
    
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY id ASC";
    private static final String SELECT_BY_NOME = SELECT_BASE + " WHERE nome LIKE ? ORDER BY nome";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO produttore (nome, indirizzo, website)
                VALUES (?, ?, ?)     
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
        UPDATE produttore
                SET nome=?, indirizzo=?, website=?
                WHERE id=?
        """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM produttore WHERE id=?";

    // conto referenze
    private static final String COUNT_PRODOTTI_USING_PRODUTTORE = "SELECT COUNT(*) FROM birra WHERE produttore_id = ?";

    @Override
    public List findAll() {

        List<Produttore> produttori = new ArrayList<>();

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                produttori.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produttori;

    }

    @Override
    public List findByNome(String q) throws Exception {

        List<Produttore> produttori = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NOME)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produttori.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produttori;

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
    public Integer save(Produttore pr) throws Exception {

        // INSERT si id == 0, sino UPDATE
        if (pr.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, pr.getNome());
                ps.setString(2, pr.getIndirizzo());
                ps.setString(3, pr.getWebsite());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        pr.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT produttore");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar produttore", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, pr.getNome());
                ps.setString(2, pr.getIndirizzo());
                ps.setString(3, pr.getWebsite());
                ps.setInt(4, pr.getId());

                ps.executeUpdate();
                return pr.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar produttore id=" + pr.getId(), e);
            }
        }

    }

    @Override
    public int creare(Produttore p) {

        int result = 0;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getIndirizzo());
            ps.setString(3, p.getWebsite());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di creazione produttore", e);
        }

        return result;

    }

    @Override
    public int modificare(Produttore p) {

        int result = 0;
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getIndirizzo());
            ps.setString(3, p.getWebsite());
            ps.setInt(4, p.getId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di modificazione id=" + p.getId(), e);
        }
        return result;

    }

    //check per delete   
    private boolean isFkViolation(SQLException e) {
        // MySQL: SQLState 23000 (violación integridad); 1451 = cannot delete/update parent row
        return "23000".equals(e.getSQLState()) && e.getErrorCode() == 1451;
    }

    private boolean isProduttoreInUse(int produttoreId) throws SQLException {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(COUNT_PRODOTTI_USING_PRODUTTORE)) {
            ps.setInt(1, produttoreId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        // 1) Chequeo preventivo (UX mejor)
        if (isProduttoreInUse(id)) {
            throw new DomainConstraintException(
                    "Imposibile eliminar il produttore perche ci sono birre collegate."
            );
        }

        // 2) Intento de borrar (por si otro hilo/tx insertó algo en el medio)
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isFkViolation(e)) {
                throw new DomainConstraintException(
                        "Imposibile eliminar il produttore perche ci sono birre collegate.", e
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
