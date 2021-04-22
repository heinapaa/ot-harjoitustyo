package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.util.List;

public interface IngredientDao {
    
    void create(Ingredient ingredient);
    void remove(Ingredient ingredient);
    void removeByRecipe(Recipe recipe);
    List<Ingredient> findByRecipe(Recipe recipe);
    Ingredient findById(int id);
    List<Ingredient> findAll();
    
}
