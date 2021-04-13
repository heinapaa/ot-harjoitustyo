package generator.domain;

import generator.dao.RecipeDao;
import java.util.*;

public class Recipe {
    
    private String name;
    private int serving;
    private User owner;
    
    public Recipe(String name, int serving, User user) {
        this.name = name;
        this.serving = serving;
        this.owner = user;
    }   

    public String getName() {
        return name;
    }

    public int getServing() {
        return serving;
    }
    
    public User getOwner() {
        return this.owner;
    }
     
}
