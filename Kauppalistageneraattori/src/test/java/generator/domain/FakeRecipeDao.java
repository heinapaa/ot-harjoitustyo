package generator.domain;

import generator.dao.RecipeDao;
import generator.dao.RecipeDao;
import generator.models.Recipe;
import generator.models.User;
import java.util.ArrayList;
import java.util.List;

public class FakeRecipeDao implements RecipeDao {
    
    List<Recipe> recipes;
    int id;
    
    public FakeRecipeDao() {
        this.recipes = new ArrayList<>();
        this.id = 1;
    }

    
    @Override
    public boolean create(Recipe recipe) {
        recipe.setId(id);
        id++;
        recipes.add(recipe);
        return true;
    }

    @Override
    public Recipe findById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }
    
    @Override
    public Recipe findByNameAndUser(String name, User user) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name) && recipe.getOwner().equals(user)) {
                return recipe;
            }
        }
        return null;
    }
    
    @Override
    public List<Recipe> findByTypeAndUser(String type, User user) {
        List<Recipe> recipeList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getType().equals(type) && recipe.getOwner().equals(user)) {
                recipeList.add(recipe);
            }
        }
        return recipeList;
    }    

    @Override
    public List<Recipe> findByUser(User user) {
        List<Recipe> recipeList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getOwner().equals(user)) {
                recipeList.add(recipe);
            }
        }
        return recipeList;
    }

    @Override
    public List<Recipe> findAll() {
        return recipes;
    }

    @Override
    public boolean update(String newName, int newPortion, String newType, Recipe recipe) {
        Recipe recipeToUpdate = null;
        for (Recipe r : recipes) {
            if (r.equals(recipe)) {
                recipeToUpdate = r;
                break;
            }
        }
        recipes.remove(recipeToUpdate);        
        recipeToUpdate.setName(newName);
        recipeToUpdate.setPortion(newPortion);
        recipeToUpdate.setType(newType);
        recipes.add(recipeToUpdate);
        return true;
    }
    
    @Override
    public boolean remove(Recipe recipe) {
        recipes.remove(recipe);
        return true;
    }
    
}
