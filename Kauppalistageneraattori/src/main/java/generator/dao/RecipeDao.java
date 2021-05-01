package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public interface RecipeDao {
    
    boolean create(Recipe recipe);
    Recipe findById(int id);    
    Recipe findByName(String name);
    List<Recipe> findByType(String type);
    List<Recipe> findByUser(User user);    
    List<Recipe> findAll();
    boolean update(String newName, int newPortion, String newType, Recipe recipe);
    boolean remove(Recipe recipe);    
    
}
