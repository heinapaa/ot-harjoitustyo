package generator.dao.sql;

import java.sql.*;

/**
 * Tietokantakyselyitä suorittava luokka.
 * @author heinapaa
 */

public class SQLDao {

    private final String driver = "org.h2.Driver";
    private final String url;
    private final String username; 
    private final String password;   
    
    private Connection conn;
    
    /**
     * Konstruktori, joka tallentaa käytettävän tietokantatiedoston nimen ja tietokannan käyttäjätunnuksen ja salasanan
     * @param fileName  tietokantatiedoston nimi
     * @param username  tietokannan käyttäjätunnus
     * @param password  tietokannan salasana
     * @throws java.lang.ClassNotFoundException
     */
    
    public SQLDao(String fileName, String username, String password) throws ClassNotFoundException {
        if (fileName.startsWith("jdbc:h2")) {
            this.url = fileName;            
        } else {
            this.url = "jdbc:h2:./" + fileName;
        }

        this.username = username;
        this.password = password;
        Class.forName(driver);        
    } 
   
    public Connection connect() throws SQLException {
        this.conn = DriverManager.getConnection(url, username, password); 
        return conn;
    } 
    
    public void completePreparedConnection(PreparedStatement pstmt) throws SQLException {
        pstmt.executeUpdate();
        pstmt.close();
        this.conn.close();
    }
    
    public void endConnection(Statement stmt) throws SQLException {
        stmt.close();
        this.conn.close();
    } 
    
    public void endPreparedConnection(PreparedStatement pstmt, ResultSet rs) throws SQLException {
        rs.close();
        pstmt.close();   
        this.conn.close();
    }  
    
    public void closeConnection() throws SQLException {
        connect().createStatement().execute("DROP ALL OBJECTS");
    }    
}