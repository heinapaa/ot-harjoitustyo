package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private User currentUser;
    
    public RecipeService(UserDao userDao, RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
    }    
    
    public void createRecipe(String name, int portion) throws Exception {
        Recipe newRecipe = new Recipe(name, portion, currentUser);
        recipeDao.create(newRecipe);
    }    
    
    public void removeRecipe(String name) throws Exception {
        recipeDao.remove(recipeDao.findByName(name));
    }      
    
    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getOwner().equals(currentUser)) returnRecipes.add(recipe);
        }
        
        return returnRecipes;
    }
    
    public User login(String nimi) throws Exception {
        
            if (userDao.findByUsername(nimi) == null) userDao.create(new User(nimi));
            this.currentUser = userDao.findByUsername(nimi);
            return currentUser;
            
    }

    
}
