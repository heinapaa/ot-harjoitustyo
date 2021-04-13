package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.util.List;

public interface RecipeDao {
    
    void create(Recipe recipe) throws Exception;
    void remove(Recipe recipe) throws Exception;
    Recipe findByName(String name);    
    List<Recipe> findAll();
    
}
