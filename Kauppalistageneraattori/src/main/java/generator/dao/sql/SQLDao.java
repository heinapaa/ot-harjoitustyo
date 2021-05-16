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
     * @throws java.lang.ClassNotFoundException virhe tietokanta-ajurin tunnistamisessa
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
    
    /**
     * Avaa yhteyden tietokantaan kyselyä varten
     * @return Connection-olio
     * @throws SQLException tietokantayhteyden muodostaminen epäonnistuu
     */
   
    public Connection connect() throws SQLException {
        this.conn = DriverManager.getConnection(url, username, password); 
        return conn;
    }
    
    /**
     * Suorittaa parametrillisen kyselyn joka tekee muutoksia tietokantaan, ja sulkee kyselyn ja tietokantayhteyden.
     * @param pstmt parametrillinen tietokantakysely
     * @throws SQLException tietokantakyselyn suorittaminen epäonnistuu
     */
    
    public void completePreparedConnection(PreparedStatement pstmt) throws SQLException {
        pstmt.executeUpdate();
        pstmt.close();
        this.conn.close();
    }
    
    /**
     * Suorittaa parametrittoman tietokantakyselyn, ja sulkee kyselyn ja tietokantayhteyden.
     * @param stmt parametriton tietokantakysely
     * @throws SQLException tietokantakyselyn suorittaminen epäonnistuu
     */
    
    public void endConnection(Statement stmt) throws SQLException {
        stmt.close();
        this.conn.close();
    } 
    
    /**
     * Sulkee parametrillisen tietokantakyselyn, sen avulla saadut tulokset sekä yhteyden.
     * @param pstmt parametrillinen tietokantakysely
     * @param rs    tietokantakyselyn avulla saadut tulokset
     * @throws SQLException tietokantakyselyn suorittaminen epäonnistuu
     */
    
    public void endPreparedConnection(PreparedStatement pstmt, ResultSet rs) throws SQLException {
        rs.close();
        pstmt.close();   
        this.conn.close();
    }  
    
    /**
     * Tyhjentää tietokannan
     * @throws SQLException tietokantakyselyn suorittaminen epäonnistuu
     */
    
    public void closeConnection() throws SQLException {
        connect().createStatement().execute("DROP ALL OBJECTS");
    }    
}
