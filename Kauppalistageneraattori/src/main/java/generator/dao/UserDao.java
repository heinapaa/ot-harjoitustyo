package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;

public interface UserDao {
    
    void create(User user) throws Exception;
    Recipe getRecipeByName(String name);
    void addRecipe(Recipe recipe);
    void removeRecipe(Recipe recipe);    

}
