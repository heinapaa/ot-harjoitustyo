package generator.dao;

import generator.domain.User;
import java.util.List;

/**
 * Käyttäjien tallennukseen liittyvien luokkien rajapinta.
 */

public interface UserDao {
    
    boolean create(User user);    
    User findByUsername(String name);
    List<User> findAll();

}
