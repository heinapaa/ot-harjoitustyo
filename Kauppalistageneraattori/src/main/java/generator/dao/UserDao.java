package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;

public interface UserDao {
    
    void create(User user) throws Exception;  
    void addRecipe(Recipe recipe);
    void removeRecipe(Recipe recipe);
    Recipe getRecipeByName(String name);

}
