package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;

public class RecipeService {
    
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private UserDao userDao;
    private User currentUser;
    
    public RecipeService(RecipeDao recipeDao, IngredientDao ingredientDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.userDao = userDao;
    }    
    
    // Siirretään tänne:
    
    // addRecipe
    
    // removeRecipe
    
    // addIngredient
    
}
