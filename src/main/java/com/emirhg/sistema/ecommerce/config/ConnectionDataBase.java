package com.emirhg.sistema.ecommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionDataBase {

    private static final Properties props;

    static {
        try (InputStream input = ConnectionDataBase.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("File config.properties non trovato");
            }
            props = new Properties();
            props.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Errore al caricare config.properties: " + e.getMessage());
        }
    }
    
    

    private static final String DriverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String host = props.getProperty("DB_HOST","localhost");
    private static final String port = props.getProperty("DB_PORT","3306");
    private static final String db = props.getProperty("DB_NAME","tlacuache");
    private static final String user = props.getProperty("DB_USER","root");
    private static final String pass = props.getProperty("DB_PASSWORD","root");
    private static final String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&serverTimezone=UTC";

   
    private static BasicDataSource pool;

    public static BasicDataSource getInstance() throws SQLException {
        if (pool == null) {
            pool = new BasicDataSource();
            pool.setDriverClassName(DriverClassName);
            pool.setUrl(url);
            pool.setUsername(user);
            pool.setPassword(pass);
            pool.setInitialSize(3);
            pool.setMinIdle(3);
            pool.setMaxIdle(8);
            pool.setMaxTotal(8);
        }
        return pool;
    }

    //ottener una connection del pool
    public static Connection getConnection() throws SQLException {
        return getInstance().getConnection();
    }

    /*
    Questa class gestisce il pool di connessioni JDBC (Apache DBCP2)
    -Imposta URL, utente e password del database MySQL.
    -Implementa getConnection() per ottenere una Connection dal pool.
    -Il pool viene inizializzato quando si utilizza per prima volta
    
    NOTE IMPORTANTI:
    -Aggiornare URL e credenziali secondo l'ambiente locale.
    -Tenere il drive MySQL nel classpath (mysql-connector-j)
    -Chiuedere ResultSet/Statment/Connection (utilizzando try-with-resourses).
    
     */
}
