package generator.dao;

/**
 * Käyttäjien tallennukseen liittyvä rajapinta.
 */

import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public interface UserDao {
    
    boolean create(User user);    
    User findByUsername(String name);
    List<User> findAll();
    boolean isUser(String name);

}
