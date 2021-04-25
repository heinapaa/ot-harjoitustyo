package generator.domain;

import generator.dao.UserDao;
import org.apache.commons.lang3.StringUtils;

public class UserService {
    
    private UserDao userDao;
    private User loggedIn;
    private InputValidator validator;
    
    public UserService(UserDao userDao, InputValidator validator) {
        this.userDao = userDao;
        this.validator = validator;
    }
          
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
    
    public void logout() {
        this.loggedIn = null;
    }
    
    public boolean addNewUser(String name) {
        String nm = StringUtils.deleteWhitespace(name); 
        if (!validator.isValidUserName(nm)) {
            return false;
        } else if (userDao.isUser(nm)) {
            return false;
        }
        userDao.create(new User(nm));
        return true;
    }
    
    public User getLoggedIn() {
        return loggedIn;
    }
    
}
