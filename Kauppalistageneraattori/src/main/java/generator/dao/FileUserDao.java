package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUserDao implements UserDao {
    
    private List<User> users;
    private String file;
    
    public FileUserDao() throws Exception {
        this.users = new ArrayList<>();
        this.file = "users.txt";    
        
        File userList = new File(file);
        
        if (userList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get("users.txt"))) {
                while (tiedostonLukija.hasNextLine()) {
                    users.add(new User(tiedostonLukija.nextLine()));
                }
            }             
        }                          
    }
    
    private void save() {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (User user : users) {
                kirjoittaja.write(user.getUsername() + "\n");
            }
        } catch (Exception e) {
            
        }       
    }

    @Override
    public void create(User user) {
        users.add(user);
        save();
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
