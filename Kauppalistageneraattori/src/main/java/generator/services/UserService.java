package generator.services;

import generator.models.User;
import generator.dao.UserDao;
import org.apache.commons.lang3.StringUtils;

/**
 * Käyttäjien käsittelyyn liittyvästä sovelluslogiikasta vastaava luokka.
 */

public class UserService {
    
    private final UserDao userDao;
    private final InputValidator validator;    
    private User loggedIn;
    
    /**
     * Konstruktori
     * @param userDao   UserDao-rajapinnan oteuttava olio, joka vastaa käyttäjien tallentamisesta
     * @param validator InputValidator-olio, joka vastaa syötteiden validoimisesta
     */
    
    public UserService(UserDao userDao, InputValidator validator) {
        this.userDao = userDao;
        this.validator = validator;
    }
    
    /**
     * Metodi asettaa käyttäjän sisäänkirjautuneeksi syötteenä annetun käyttäjänimen perusteella.
     * 
     * @param name  Käyttäjän syöttämä merkkijono
     * @see generator.services.InputValidator#isValidUserName(java.lang.String) 
     * @see generator.dao.UserDao#findByUsername(java.lang.String) 
     * @return      true jos käyttäjä on olemassa, false jos käyttäjää ei ole olemassa
     */
          
    public boolean login(String name) {
        if (name == null) {
            return false;
        }
        String nm = StringUtils.deleteWhitespace(name);
        if (!validator.isValidUserName(nm)) {
            return false;
        }
        User user = userDao.findByUsername(nm);
        if (user == null) {
            return false;
        }
        this.loggedIn = user;
        return true;         
    }
    
    /**
     * Metodi kirjaa ulos sisäänkirjautuneen käyttäjän.
     */
    public void logout() {
        this.loggedIn = null;
    }
    
    /**
     * Metodi luo uuden käyttäjän syötteenä annetun käyttäjänimen perusteella.
     * 
     * @param name  Käyttäjän syöttämä merkkijono
     * @see generator.services.InputValidator#isValidUserName(java.lang.String) 
     * @see generator.dao.UserDao#findByUsername(java.lang.String) 
     * @see generator.dao.UserDao#create(generator.models.User) 
     * @return      true jos uuden käyttäjän luominen onnistuu, muuten false
     */
    
    public boolean addNewUser(String name) {
        if (name == null) {
            return false;
        }
        String nm = StringUtils.deleteWhitespace(name); 
        if (!validator.isValidUserName(nm)) {
            return false;
        } else if (userDao.findByUsername(nm) != null) {
            return false;
        } 
        return userDao.create(new User(nm));
    }
    
    /**
     * Palauttaa sisäänkirjautuneen käyttäjän.
     * @return Sisäänkirjautunut käyttäjä, null jos käyttäjää ei ole asetettu
     */
    
    public User getLoggedIn() {
        return loggedIn;
    }
    
}
