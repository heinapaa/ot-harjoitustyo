package generator.dao;

import generator.models.User;
import java.util.List;

/**
 * Käyttäjien tallennukseen liittyvien luokkien rajapinta.
 */

public interface UserDao {
    
    /**
     * Tallentaa käyttäjän.
     * @param user  Tallennettava käyttäjä
     * @return  true jos tallennus onnistuu, muuten false
     */    
    
    boolean create(User user);    
    
    /**
     * Hakee käyttäjän käyttäjänimen perusteella.
     * @param name  syötteenä annettu käyttäjänimi
     * @return käyttäjä jos nimeä vastaava olio löytyy, muuten null
     */
    
    User findByUsername(String name);
    
    /**
     * Hakee kaikki rekisteröidyt käyttäjät.
     * @return List-rakenne, joka sisältää kaikki rekisteröityneet käyttäjät
     */ 
    
    List<User> findAll();

}
