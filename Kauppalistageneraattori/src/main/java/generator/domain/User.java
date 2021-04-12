package generator.domain;

import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    
    private String username;
    private Map<String, Recipe> recipes;
    
    public User(String username) {
        this.username = username;
        this.recipes = new HashMap<>();
    }
    
    public Recipe getRecipeByName(String name) {
        return recipes.get(name);
    }

    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getName(), recipe);
    }
    
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe.getName());
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Recipe> getRecipes() {
        return recipes;
    }
    
}
