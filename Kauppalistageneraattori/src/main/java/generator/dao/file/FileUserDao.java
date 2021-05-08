package generator.dao.file;

import generator.dao.UserDao;
import generator.models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Käyttäjiä tekstitiedostoon tallentava luokka, joka laajentaa FileDao-luokkaa ja toteuttaa UserDao-rajapinnan.
 */

public class FileUserDao extends FileDao implements UserDao {
    
    private List<User> users;
    
    /**
     * Konstruktori
     * @param fileName  Tiedoston nimi, johon käyttäjät halutaan tallentaa
     * @throws java.io.IOException
     */
    
    public FileUserDao(String fileName) throws IOException {
        super(fileName);       
        this.users = new ArrayList<>();   
        
        for (String line : super.lines) {
            users.add(new User(line));
        }                          
    }
    
    /**
     * Metodi tallentaa käyttäjän.
     * @param user  Tallennettava käyttäjä
     * @return  true jos tallennus onnistuu, muuten false
     */

    @Override
    public boolean create(User user) {
        users.add(user);
        return save();
    }
    
    /**
     * Metodi hakee käyttäjän käyttäjänimen perusteella.
     * @param name  syötteenä annettu käyttäjänimi
     * @return käyttäjä jos nimeä vastaava olio löytyy, muuten null
     */

    @Override
    public User findByUsername(String name) {
        if (users.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }
    
    
    /**
     * Metodi hakee kaikki rekisteröidyt käyttäjät.
     * @return List-rakenne, joka sisältää kaikki rekisteröityneet käyttäjät
     */    

    @Override
    public List<User> findAll() {
        return users;
    }    
       
    private boolean save() {
        super.lines = new ArrayList<>();
        for (User user : users) {
            lines.add(user.getUsername() + "\n");
        }
        return super.writeToFile();
    }

}