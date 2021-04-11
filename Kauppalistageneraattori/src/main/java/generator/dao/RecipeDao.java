package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;

public interface RecipeDao {
    
    void create(Recipe recipe) throws Exception;
    void addIngredient(String name, double amount, String unit);
    
}
