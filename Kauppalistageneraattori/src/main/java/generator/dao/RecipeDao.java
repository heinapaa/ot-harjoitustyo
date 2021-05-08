package generator.dao;

import generator.models.Recipe;
import generator.models.User;
import java.util.List;

/**
 * Reseptien tallennukseen liittyvien luokkien rajapinta.
 */

public interface RecipeDao {
    
    boolean create(Recipe recipe);
    Recipe findById(int id);    
    Recipe findByNameAndUser(String name, User user);
    List<Recipe> findByTypeAndUser(String type, User user);
    List<Recipe> findByUser(User user);    
    List<Recipe> findAll();
    boolean update(String newName, int newPortion, String newType, Recipe recipe);
    boolean remove(Recipe recipe);    
    
}
