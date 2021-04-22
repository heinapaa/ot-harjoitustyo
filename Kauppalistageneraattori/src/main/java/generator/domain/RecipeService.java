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
    
    public boolean createRecipe(String name, int portion) {
        Recipe newRecipe = new Recipe(name, portion, currentUser);
        try {
            recipeDao.create(newRecipe);   
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean removeRecipe(String name) {
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
    
    public boolean addIngredient(String recipeName, String ingredientName, String ingredientUnit, double ingredientAmount) {
        Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnit);
        newIngredient.setRecipe(recipeDao.findByName(recipeName));
        ingredientDao.create(newIngredient);
        return true;
    }
    
    public boolean removeIngredient(String recipeName, String ingredientName) {
        List<Ingredient> ingredientList = ingredientDao.findByRecipe(recipeDao.findByName(recipeName));
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getName().equals(ingredientName)) {
                ingredientDao.remove(ingredient);
                return true;
            }
        }
        return false;
    }
    
    public List<Ingredient> getIngredients(String recipeName) {
        Recipe recipe = recipeDao.findByName(recipeName);
        return ingredientDao.findByRecipe(recipe);
    }
    
    public Recipe getRecipe(String recipeName) {
        return recipeDao.findByName(recipeName);
    }
    
    public Ingredient getIngredient(String recipeName, String ingredientName) {
        Recipe recipe = recipeDao.findByName(recipeName);
        List<Ingredient> ingredientsInRecipe = ingredientDao.findByRecipe(recipe);
        for (Ingredient ingredient : ingredientsInRecipe) {
            if (ingredientName.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }
    
    public boolean login(String nimi) {
        if (userDao.findByUsername(nimi) == null) {
            userDao.create(new User(nimi));
        }
        this.currentUser = userDao.findByUsername(nimi);
        return true;         
    }
      
    public boolean recipeExists(String name) {

        if (recipeDao.findByName(name) == null) {
            return false;
        }

        return true;
    }
}