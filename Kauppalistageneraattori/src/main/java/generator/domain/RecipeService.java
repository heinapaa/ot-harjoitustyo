package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;
    private User currentUser;
    
    public RecipeService(UserDao userDao, RecipeDao recipeDao, IngredientDao ingredientDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.ingredientDao = ingredientDao;
    }    
    
    public Recipe createRecipe(String name, int portion) throws Exception {
        Recipe newRecipe = new Recipe(name, portion, currentUser);
        recipeDao.create(newRecipe);
        return recipeDao.findByName(name);
    }    
    
    public void removeRecipe(String name) throws Exception {
        Recipe recipeToDelete = recipeDao.findByName(name);
        ingredientDao.removeByRecipe(recipeToDelete);
        recipeDao.remove(recipeToDelete);
    }      
    
    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getOwner().equals(currentUser)) returnRecipes.add(recipe);
        }
        
        return returnRecipes;
    }
    
    public void addIngredient(String recipeName, String ingredientName, String ingredientUnit, double ingredientAmount) throws Exception {
        Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnit);
        newIngredient.setRecipe(recipeDao.findByName(recipeName));
        ingredientDao.create(newIngredient);
    }
    
    public List<Ingredient> getIngredients(String recipeName) {
        Recipe recipe = recipeDao.findByName(recipeName);
        return ingredientDao.findByRecipe(recipe);
    }
    
    public User login(String nimi) throws Exception {
        
            if (userDao.findByUsername(nimi) == null) {
                userDao.create(new User(nimi));
            }
            this.currentUser = userDao.findByUsername(nimi);
            return currentUser;
            
    }
    
    public List<String> createShoppingList(List<String> recipes) {
        List<String> shoppingList = new ArrayList<>();
        
        for (String reseptinNimi : recipes) {
            Recipe recipe = recipeDao.findByName(reseptinNimi);
            List<Ingredient> ingredients = ingredientDao.findByRecipe(recipe);
            
            for (Ingredient ingredient : ingredients) shoppingList.add(ingredient.toString());
        }
        
        return shoppingList;
    }

    
}
