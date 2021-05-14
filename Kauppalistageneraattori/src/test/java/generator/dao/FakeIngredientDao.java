package generator.dao;

import generator.dao.IngredientDao;
import generator.dao.IngredientDao;
import generator.models.Ingredient;
import generator.models.Recipe;
import java.util.ArrayList;
import java.util.List;

public class FakeIngredientDao implements IngredientDao {
    
    List<Ingredient> ingredients;
    
    public FakeIngredientDao() {
        this.ingredients = new ArrayList<>();
    }

    @Override
    public boolean create(Ingredient ingredient) {
        ingredients.add(ingredient);
        return true;
    }

    @Override
    public boolean removeByRecipe(Recipe recipe) {
        List<Ingredient> deleteList = findByRecipe(recipe);
        for (Ingredient ingredient : deleteList) {
            ingredients.remove(ingredient);
        }
        return true;
    }

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        List<Ingredient> ingredientList = new ArrayList<>();
        
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().equals(recipe)) {
                ingredientList.add(ingredient);
            }
        }
        
        return ingredientList;
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredients;
    }

    @Override
    public boolean remove(Ingredient ingredient) {
        ingredients.remove(ingredient);
        return true;
    }

    @Override
    public Ingredient findByNameAndRecipe(String name, Recipe recipe) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(name) && ingredient.getRecipe().equals(recipe)) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    public List<Ingredient> findByRecipes(List<Recipe> recipes) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(findByRecipe(recipe));
        }
        return ingredients;
    }
    
}
