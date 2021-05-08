package generator.dao.sql;

import generator.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author heinapaa
 */
public class SQLUserConnection extends SQLConnection {
    
    public SQLUserConnection(String fileName, String username, String password) {
        super(fileName, username, password);             
    }
    
    /**
     * Metodi luo taulun käyttäjien tietojen tallennusta varten (kenttänä käyttäjänimi), jos sellaista ei ole jo olemassa.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endConnection(java.sql.Connection, java.sql.Statement)
     */
    
    public void createUserTable() throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE IF NOT EXISTS Users (name VARCHAR(255) UNIQUE not NULL)";  
        
        System.out.println("Luodaan käyttäjätaulukkoa...");
        Connection conn = super.connect();
        Statement stmt = conn.createStatement();           
        stmt.executeUpdate(sql);
        System.out.println("Käyttäjätaulukko luotu!\n");
        
        super.endConnection(conn, stmt);
    }
    
    
    /**
     * Metodi lisää syötteenä annetut käyttäjän tiedot tietokantaan.
     * @param username  tallennettavan käyttäjän käyttäjänimi
     * @return true jos käyttäjän tallennus onnistuu, muuten false
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean insertUser(String username) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Users (name) VALUES(?)";    
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        super.completePreparedConnection(conn, pstmt);            
        return true;
    }     
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden käyttäjän tiedot käyttäjänimen perusteella.
     * @param username  haettavan käyttäjän käyttäjänimi
     * @return haettua käyttäjää vastaava {@code User} jos käyttäjä löytyy, mu
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public User selectOneUser(String username) throws SQLException, ClassNotFoundException {
        User user = null;   
        String sql = "SELECT * FROM Users WHERE name = ?";         
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();    
        while (rs.next()) {
            user = new User(rs.getString("name"));               
        }
        
        super.endPreparedConnection(conn, pstmt, rs);
        return user;
    }      
    
    /**
     * Metodi hakee ja palauttaa kaikki tietokantaan tallennetut käyttäjät.
     * @return lista, joka sisältää tallennetut käyttäjät {@code User}-olioina
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<User> selectAllUsers() throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();  
        String sql = "SELECT * FROM Users";         

        Connection conn = super.connect();  
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getString("name")));
        }
        super.endPreparedConnection(conn, pstmt, rs);
        
        return users;
    }      
    
}