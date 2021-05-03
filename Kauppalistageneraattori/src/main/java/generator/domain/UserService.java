package generator.domain;

import generator.dao.UserDao;
import org.apache.commons.lang3.StringUtils;

/**
 * Käyttäjien käsittelyyn liittyvästä sovelluslogiikasta vastaava luokka.
 */

public class UserService {
    
    private final UserDao userDao;
    private final InputValidator validator;    
    private User loggedIn;
    
    public UserService(UserDao userDao, InputValidator validator) {
        this.userDao = userDao;
        this.validator = validator;
    }
    
    /**
     * Metodi asettaa käyttäjän sisäänkirjautuneeksi syötteenä annetun käyttäjänimen perusteella.
     * 
     * @param name  Käyttäjän syöttämä merkkijono
     * 
     * @see         generator.domain.InputValidator#isValidUserName(java.lang.String) 
     * @see         generator.dao.UserDao#isUser(java.lang.String)
     * @see         generator.dao.UserDao#findByUsername(java.lang.String) 
     * 
     * @return      true jos käyttäjä on olemassa, false jos käyttäjää ei ole olemassa
     */
          
    public boolean login(String name) {
        String nm = StringUtils.deleteWhitespace(name);
        if (!validator.isValidUserName(nm)) {
            return false;
        } else if (!userDao.isUser(nm)) {
            return false;
        }
        this.loggedIn = userDao.findByUsername(nm);
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
     * 
     * @return      true jos uuden käyttäjän luominen onnistuu, muuten false
     */
    
    public boolean addNewUser(String name) {
        String nm = StringUtils.deleteWhitespace(name); 
        if (!validator.isValidUserName(nm)) {
            return false;
        } else if (userDao.isUser(nm)) {
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
