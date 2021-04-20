package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public interface RecipeDao {
    
    void create(Recipe recipe);
    void remove(Recipe recipe);
    Recipe findByName(String name);
    Recipe findById(int id);
    List<Recipe> findByUser(User user);    
    List<Recipe> findAll();
    
}
