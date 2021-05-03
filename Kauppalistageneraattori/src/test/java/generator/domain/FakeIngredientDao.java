package generator.domain;

import generator.dao.IngredientDao;
import java.util.ArrayList;
import java.util.List;

public class FakeIngredientDao implements IngredientDao {
    
    List<Ingredient> ingredients;
    int id;
    
    public FakeIngredientDao() {
        this.ingredients = new ArrayList<>();
        this.id = 1;
    }

    @Override
    public boolean create(Ingredient ingredient) {
        ingredient.setId(id);
        id++;
        ingredients.add(ingredient);
        return true;
    }

    @Override
    public boolean removeByRecipe(Recipe recipe) {
        List<Ingredient> deleteList = new ArrayList<>();
        
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().equals(recipe)) {
                deleteList.add(ingredient);
            }
        }
        
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
    public Ingredient findById(int id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        return null;
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
    
}
