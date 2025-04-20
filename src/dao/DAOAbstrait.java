package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite pour les DAO
 * @param <T> Le type d'entité
 */
public abstract class DAOAbstrait<T> implements DAO<T> {

    protected static final String URL = "jdbc:mysql://localhost:8889/parc_attractions";
    protected static final String USER = "root";
    protected static final String PASSWORD = "root";

    /**
     * Obtenir une connexion à la base de données
     * @return La connexion
     * @throws SQLException En cas d'erreur de connexion
     */
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC non trouvé", e);
        }
    }

    /**
     * Ferme les ressources JDBC
     */
    protected void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Le reste du code reste inchangé
}