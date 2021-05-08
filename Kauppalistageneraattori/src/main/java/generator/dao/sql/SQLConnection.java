package generator.dao.sql;

import java.sql.*;

/**
 * Tietokantakyselyitä suorittava luokka.
 * @author heinapaa
 */

public class SQLConnection {

    private final String driver = "org.h2.Driver";
    private final String url;
    private String username = "sa"; 
    private String password = "";   
    
    /**
     * Konstruktori, joka tallentaa käytettävän tietokantatiedoston nimen ja tietokannan käyttäjätunnuksen ja salasanan
     * @param fileName  tietokantatiedoston nimi
     * @param username  tietokannan käyttäjätunnus
     * @param password  tietokannan salasana
     */
    
    public SQLConnection(String fileName, String username, String password) {
        this.url = "jdbc:h2:./" + fileName;
        this.username = username;
        this.password = password;
    } 
   
    public Connection connect() throws SQLException, ClassNotFoundException {
        Connection conn = null; 
        Class.forName(driver); 
        conn = DriverManager.getConnection(url, username, password); 
        return conn;
    } 
    
    public void completePreparedConnection(Connection conn, PreparedStatement pstmt) throws SQLException {
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
    
    public void endConnection(Connection conn, Statement stmt) throws SQLException {
        stmt.close();
        conn.close();
    } 
    
    public void endPreparedConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        rs.close();
        pstmt.close();        
        conn.close();
    }    
}
