package generator.dao;

import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Käyttäjiä tekstitiedostoon tallentava luokka, joka toteuttaa UserDao-rajapinnan.
 */

public class FileUserDao implements UserDao {
    
    private List<User> users;
    private String file;
    
    public FileUserDao(String file) throws Exception {
        this.file = file;         
        this.users = new ArrayList<>();   
        
        File userList = new File(file);
        if (userList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    users.add(new User(tiedostonLukija.nextLine()));
                }
            }             
        } else {
            userList.createNewFile();
        }                          
    }
    
    private boolean save() {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (User user : users) {
                kirjoittaja.write(user.getUsername() + "\n");
            }
        } catch (Exception e) {
            return false;
        }  
        return true;
    }

    @Override
    public boolean create(User user) {
        users.add(user);
        return save();
    }

    @Override
    public User findByUsername(String name) {
        if (users.isEmpty()) {
            return null;
        }
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
    
    @Override
    public boolean isUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
