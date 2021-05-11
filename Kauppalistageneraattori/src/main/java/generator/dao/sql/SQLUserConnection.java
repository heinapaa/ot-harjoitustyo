package generator.dao.sql;

import generator.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Käyttäjiin liittyvistä tietokantakyselyistä vastaava luokka, joka laajentaa {@Code SQLConnection}-luokkaa
 *
 */
public class SQLUserConnection extends SQLConnection {
    
    private final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS Users (name VARCHAR(255) UNIQUE not NULL)";
    private final String INSERT_USER = "INSERT INTO Users (name) VALUES(?)";  
    private final String SELECT_USER = "SELECT * FROM Users WHERE name = ?";  
    private final String SELECT_ALL_USERS = "SELECT * FROM Users";
    
    public SQLUserConnection(String fileName, String username, String password) throws ClassNotFoundException {
        super(fileName, username, password);             
    }
    
    /**
     * Metodi luo taulun käyttäjien tietojen tallennusta varten (kenttänä käyttäjänimi), jos sellaista ei ole jo olemassa.
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endConnection(java.sql.Connection, java.sql.Statement)
     */
    
    public void createUserTable() throws SQLException {
        System.out.println("Yhdistetään käyttäjätaulukkoa...");
        Statement stmt = super.connect().createStatement();           
        stmt.executeUpdate(CREATE_USER_TABLE);
        System.out.println("Käyttäjätaulukko yhdistetty!\n");
        super.endConnection(stmt);
    }
    
    
    /**
     * Metodi lisää syötteenä annetut käyttäjän tiedot tietokantaan.
     * @param username  tallennettavan käyttäjän käyttäjänimi
     * @return true jos käyttäjän tallennus onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean insertUser(String username) throws SQLException {
        PreparedStatement pstmt = super.connect().prepareStatement(INSERT_USER);
        pstmt.setString(1, username);
        super.completePreparedConnection(pstmt);            
        return true;
    }     
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden käyttäjän tiedot käyttäjänimen perusteella.
     * @param username  haettavan käyttäjän käyttäjänimi
     * @return haettua käyttäjää vastaava {@code User} jos käyttäjä löytyy, mu
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public User selectOneUser(String username) throws SQLException {
        User user = null;   
        PreparedStatement pstmt = super.connect().prepareStatement(SELECT_USER);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();    
        while (rs.next()) {
            user = new User(rs.getString("name"));               
        }
        super.endPreparedConnection(pstmt, rs);
        return user;
    }      
    
    /**
     * Metodi hakee ja palauttaa kaikki tietokantaan tallennetut käyttäjät.
     * @return lista, joka sisältää tallennetut käyttäjät {@code User}-olioina
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();  
        PreparedStatement pstmt = super.connect().prepareStatement(SELECT_ALL_USERS);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getString("name")));
        }
        super.endPreparedConnection(pstmt, rs);
        
        return users;
    }      
    
}