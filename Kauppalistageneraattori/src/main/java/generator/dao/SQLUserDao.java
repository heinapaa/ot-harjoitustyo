package generator.dao;

import generator.domain.User;
import java.util.List;

public class SQLUserDao implements UserDao{
    
    SQLConnection connection;
    
    public SQLUserDao(SQLConnection conn) {
        this.connection = conn;
        connection.createUserTable();
    }

    @Override
    public boolean create(User user) {
        return connection.insertUser(user.getUsername());
    }

    @Override
    public User findByUsername(String name) {
        User user = connection.selectOneUser(name);
        return user;
    }

    @Override
    public List<User> findAll() {
        return connection.selectAllUsers();
    }
    
}
