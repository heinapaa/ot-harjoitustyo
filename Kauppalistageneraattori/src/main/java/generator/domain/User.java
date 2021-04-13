package generator.domain;

import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    
    private String username;
    
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
