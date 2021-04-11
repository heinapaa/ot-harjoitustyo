package generator.domain;

import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements UserDao {
    
    private String username;
    private Map<String, Recipe> recipes;
    
    public User(String username) {
        this.username = username;
        this.recipes = new HashMap<>();
    }
    
    @Override
    public Recipe getRecipeByName(String name) {
        return recipes.get(name);
    }

    @Override
    public void create(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getName(), recipe);
    }
    
    @Override
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe.getName());
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Recipe> getRecipes() {
        return recipes;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRecipes(Map<String, Recipe> recipes) {
        this.recipes = recipes;
    }
    
    
}
