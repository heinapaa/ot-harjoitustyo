package generator.dao.sql;

import generator.dao.UserDao;
import generator.models.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Käyttäjiä tietokantaan tallentava luokka, joka toteuttaa UserDao-rajapinnan.
 * 
 */

public class SQLUserDao implements UserDao {
    
    private final SQLUserConnection connection;
    
    /**
     * Konstruktori, joka luo yhteyden käyttäjät sisältävään tietokantatauluun.
     * @param connection {@code SQLConnection}-olio, jota käytetään tietokantapyyntöjen toteuttamiseen
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLUserConnection#createUserTable() 
     */
    
    public SQLUserDao(SQLUserConnection connection) throws SQLException, ClassNotFoundException {
        this.connection = connection;
        connection.createUserTable();
    }
    
    /**
     * Metodi tallentaa uuden käyttäjän tiedot tietokantaan
     * @param user  tallennettava käyttäjä
     * @return true jos käyttäjän tietojen tallentaminen onnistuu, muuten false
     * @see generator.dao.sql.connection.SQLUserConnection#insertUser(java.lang.String) 
     */

    @Override
    public boolean create(User user) {
        try {
            return connection.insertUser(user.getUsername());    
        } catch (SQLException e) {
            return false;
        }  
    }
    
    /**
     * Metodi hakee ja palauttaa käyttäjän käyttäjänimen perusteella.
     * @param name käyttäjänimi, jonka mukaista käyttäjää haetaan
     * @return Haettava käyttäjä {@code User}-olion muodossa, jos käyttäjää ei löydy niin null
     * @see generator.dao.sql.connection.SQLUserConnection#selectOneUser(java.lang.String) 
     */

    @Override
    public User findByUsername(String name) {
        try {
            return connection.selectOneUser(name);
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tallennetut käyttäjät.
     * @return Lista, joka sisältää kaikki tallennetut käyttäjät {@code User}-olioina
     * @see generator.dao.sql.connection.SQLUserConnection#selectAllUsers() 
     */
    
    @Override
    public List<User> findAll() {
        try {
            return connection.selectAllUsers();
        } catch (SQLException e) {
            return new ArrayList<>();
        }

    }
    
}
