package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public interface RecipeDao {
    
    void create(Recipe recipe) throws Exception;
    void remove(Recipe recipe) throws Exception;
    Recipe findByName(String name);
    Recipe findById(int id);
    List<Recipe> findByUser(User user);    
    List<Recipe> findAll();
    
}
