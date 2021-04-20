package generator.domain;

import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;

public class FakeUserDao implements UserDao {
    
    List<User> users;
    
    public FakeUserDao() {
        this.users = new ArrayList<>();
    }

    @Override
    public void create(User user) {
        users.add(user);
    }

    @Override
    public User findByUsername(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return users;
    }
    
}
