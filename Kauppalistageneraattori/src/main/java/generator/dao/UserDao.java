package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public interface UserDao {
    
    void create(User user);    
    User findByUsername(String name);
    List<User> findAll();

}
