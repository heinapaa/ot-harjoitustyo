package generator.dao;

import generator.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Käyttäjiä tekstitiedostoon tallentava luokka, joka laajentaa TextFileSaver-luokkaa ja toteuttaa UserDao-rajapinnan.
 */

public class FileUserDao extends FileDao implements UserDao {
    
    private List<User> users;
    
    /**
     * Konstruktori
     * @param file  Tiedoston nimi, johon käyttäjät halutaan tallentaa
     */
    
    public FileUserDao(String file) {
        super(file);       
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
     * Metodi palauttaa kaikki käyttäjät.
     * @return List-rakenne, joka sisältää kaikki rekisteröityneet käyttäjät. 
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
