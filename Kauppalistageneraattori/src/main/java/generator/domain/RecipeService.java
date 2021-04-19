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
    
    public boolean createRecipe(String name, int portion) throws Exception {
        Recipe newRecipe = new Recipe(name, portion, currentUser);
        try {
            recipeDao.create(newRecipe);   
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean removeRecipe(String name) throws Exception {
        try {
            Recipe recipeToDelete = recipeDao.findByName(name);
            if (recipeToDelete == null) {
                return false;
            }
            ingredientDao.removeByRecipe(recipeToDelete);
            recipeDao.remove(recipeToDelete);            
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getOwner().equals(currentUser)) {
                returnRecipes.add(recipe);
            }
        }
        
        return returnRecipes;
    }
    
    public boolean addIngredient(String recipeName, String ingredientName, String ingredientUnit, double ingredientAmount) throws Exception {
        Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnit);
        try {
            newIngredient.setRecipe(recipeDao.findByName(recipeName));
            ingredientDao.create(newIngredient);            
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public List<Ingredient> getIngredients(String recipeName) {
        Recipe recipe = recipeDao.findByName(recipeName);
        return ingredientDao.findByRecipe(recipe);
    }
    
    public boolean login(String nimi) throws Exception {
        if (userDao.findByUsername(nimi) == null) {
            userDao.create(new User(nimi));
        }
        this.currentUser = userDao.findByUsername(nimi);
        return true;         
    }
    
    public List<String> createShoppingList(List<String> recipes) {
        List<String> shoppingList = new ArrayList<>();
        
        for (String reseptinNimi : recipes) {
            Recipe recipe = recipeDao.findByName(reseptinNimi);
            List<Ingredient> ingredients = ingredientDao.findByRecipe(recipe);
            
            for (Ingredient ingredient : ingredients) {
                shoppingList.add(ingredient.toString());
            }
        }
        
        return shoppingList;
    }
    
    public boolean recipeExists(String name) throws Exception {
        try {
            if (recipeDao.findByName(name) == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}