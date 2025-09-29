package com.emirhg.sistema.ecommerce.dao.jdbc;

import com.emirhg.sistema.ecommerce.config.ConnectionDataBase;
import com.emirhg.sistema.ecommerce.controllers.exceptions.DomainConstraintException;
import com.emirhg.sistema.ecommerce.dao.mapper.FormatoMapper;
import com.emirhg.sistema.ecommerce.dao.repository.RepositoryCataloghi;
import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormatoDao implements RepositoryCataloghi<Formato> {

    /*metodo private per ottenere una Connection dal pool centralizzato.
    Viene utilizzato su tutti i metodi di accesso ai dati.
     */
    private Connection getConnection() throws SQLException {
        return ConnectionDataBase.getConnection();
    }

    private final FormatoMapper mapper = new FormatoMapper();

    //Querys    
    private static final String SELECT_BASE = """
    SELECT id, codice FROM formato  
    """;

    private static final String SELECT_ALL = SELECT_BASE + " ORDER BY id ASC";
    private static final String SELECT_BY_CODICE = SELECT_BASE + " WHERE codice LIKE ? ORDER BY codice";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";

    // INSERT con tutti i campi - se genere_id è null, si gestisce a monte
    private static final String INSERT_SQL = """
        INSERT INTO formato (codice)
                VALUES (?)     
        """;

    // UPDATE completo - richiede libro_id, posizionato nell'ultimia posizione
    private static final String UPDATE_SQL = """
        UPDATE formato
                SET codice=?
                WHERE id=?
        """;

    // DELETE per id
    private static final String DELETE_SQL = "DELETE FROM formato WHERE id=?";

    // conto referenze
    private static final String COUNT_PRODOTTI_USING_FORMATO = "SELECT COUNT(*) FROM prodotto WHERE formato_id = ?";

    @Override
    public List<Formato> findAll() {

        List<Formato> formati = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                formati.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formati;

    }

    @Override
    public List<Formato> findByNome(String q) throws Exception {

        List<Formato> formati = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODICE)) {

            stmt.setString(1, like(q));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formati.add(mapper.map(rs));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formati;

    }

    @Override
    public Optional<Formato> findById(int id) throws Exception {

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
    public Integer save(Formato f) throws Exception {

        // INSERT si id == 0, sino UPDATE
        if (f.getId() == 0) {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, f.getCodice());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        f.setId(id);
                        return id;
                    }
                }
                throw new SQLException("No se generó ID en INSERT formato");
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar formato", e);
            }
        } else {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, f.getCodice());
                ps.setInt(2, f.getId());

                ps.executeUpdate();
                return f.getId();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar formato id=" + f.getId(), e);
            }
        }

    }

    @Override
    public int creare(Formato f) {

        int result = 0;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {

            ps.setString(1, f.getCodice());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di creazione formato", e);
        }

        return result;

    }

    @Override
    public int modificare(Formato f) {
        int result = 0;
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, f.getCodice());
            ps.setInt(2, f.getId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore di modificazione id=" + f.getId(), e);
        }
        return result;

    }

    //check per delete   
    private boolean isFkViolation(SQLException e) {
        // MySQL: SQLState 23000 (violación integridad); 1451 = cannot delete/update parent row
        return "23000".equals(e.getSQLState()) && e.getErrorCode() == 1451;
    }

    private boolean isFormatoInUse(int formatoId) throws SQLException {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(COUNT_PRODOTTI_USING_FORMATO)) {
            ps.setInt(1, formatoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        // 1) Chequeo preventivo (UX mejor)
        if (isFormatoInUse(id)) {
            throw new DomainConstraintException(
                    "Imposibile eliminar il formato perche ci sono prodotti collegati."
            );
        }

        // 2) Intento de borrar (por si otro hilo/tx insertó algo en el medio)
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isFkViolation(e)) {
                throw new DomainConstraintException(
                        "Imposibile eliminar il formato perche ci sono prodotti collegati.", e
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
