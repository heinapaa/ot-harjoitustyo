package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

/**
 * Reseptien tallennukseen liittyvien luokkien rajapinta.
 */

public interface RecipeDao {
    
    boolean create(Recipe recipe);
    //Recipe findById(int id);    
    Recipe findByNameAndUser(String name, User user);
    List<Recipe> findByType(String type);
    List<Recipe> findByUser(User user);    
    List<Recipe> findAll();
    boolean update(String newName, int newPortion, String newType, Recipe recipe);
    boolean remove(Recipe recipe);    
    
}
