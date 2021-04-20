package generator.domain;

import generator.dao.RecipeDao;
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
    public void create(Recipe recipe) {
        recipe.setId(id);
        id++;
        recipes.add(recipe);
    }

    @Override
    public void remove(Recipe recipe) {
        recipes.remove(recipe);
    }

    @Override
    public Recipe findByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }
        return null;
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
    
}
