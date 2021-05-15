package generator.dao.sql;

import generator.dao.UserDao;
import generator.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Käyttäjiä tietokantaan tallentava luokka, joka toteuttaa UserDao-rajapinnan.
 * 
 */

public class SQLUserDao extends SQLDao implements UserDao {
    
    private final String createUserTable = "CREATE TABLE IF NOT EXISTS Users (name VARCHAR(255) UNIQUE not NULL)";  
    private final String insertUser = "INSERT INTO Users (name) VALUES(?)";  
    private final String selectUser = "SELECT * FROM Users WHERE name = ?";  
    private final String selectAllUsers = "SELECT * FROM Users";    
    
    /**
     * Konstruktori, joka luo yhteyden käyttäjät sisältävään tietokantaan
     * @param fileName tietokantatiedoston nimi
     * @param username tietokannan käyttäjänimi
     * @param password tietokannan salasana
     */
    public SQLUserDao(String fileName, String username, String password) throws SQLException, ClassNotFoundException {
        super(fileName, username, password);
        Statement stmt = super.connect().createStatement();           
        stmt.executeUpdate(createUserTable);
        super.endConnection(stmt);        
    }
    
    /**
     * Metodi tallentaa uuden käyttäjän tiedot tietokantaan
     * @param user  tallennettava käyttäjä
     * @return true jos käyttäjän tietojen tallentaminen onnistuu, muuten false
     */

    @Override
    public boolean create(User user) {
        try {
            PreparedStatement pstmt = super.connect().prepareStatement(insertUser);
            pstmt.setString(1, user.getUsername());
            super.completePreparedConnection(pstmt);            
            return true;               
        } catch (SQLException e) {
            return false;
        }  
    }
    
    /**
     * Metodi hakee ja palauttaa käyttäjän käyttäjänimen perusteella.
     * @param name käyttäjänimi, jonka mukaista käyttäjää haetaan
     * @return Haettava käyttäjä {@code User}-olion muodossa, jos käyttäjää ei löydy niin null
     */

    @Override
    public User findByUsername(String name) {
        try {
            User user = null;   
            PreparedStatement pstmt = super.connect().prepareStatement(selectUser);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();    
            while (rs.next()) {
                user = new User(rs.getString("name"));               
            }
            super.endPreparedConnection(pstmt, rs);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tallennetut käyttäjät.
     * @return Lista, joka sisältää kaikki tallennetut käyttäjät {@code User}-olioina
     */
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();           
        try {           
            PreparedStatement pstmt = super.connect().prepareStatement(selectAllUsers);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString("name")));
            }
            super.endPreparedConnection(pstmt, rs);
            return users;
        } catch (SQLException e) {
            return users;
        }

    }
    
}
