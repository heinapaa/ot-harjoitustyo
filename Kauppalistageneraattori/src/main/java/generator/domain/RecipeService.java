package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class RecipeService {
    
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private InputValidator validator;    
    
    public RecipeService(RecipeDao recipeDao, IngredientDao ingredientDao, InputValidator validator) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }    
    
    public boolean createRecipe(String name, String portion, User user) {
        String nm = name.strip();
        String pn = StringUtils.deleteWhitespace(portion);
        if (!validator.isValidRecipeName(nm) || !validator.isValidRecipePortion(pn)) {
            return false;
        }
        if (recipeExists(nm)) {
            return false;
        }
        Recipe newRecipe = new Recipe(nm, Integer.parseInt(pn), user);
        try {
            recipeDao.create(newRecipe);   
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean removeRecipe(String name) {
        String nm = name.strip();
        if (!validator.isValidRecipeName(nm)) {
            return false;
        }
        if (!recipeExists(nm)) {
            return false;
        }
        try {
            Recipe recipeToDelete = recipeDao.findByName(nm);
            ingredientDao.removeByRecipe(recipeToDelete);
            recipeDao.remove(recipeToDelete);            
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean updateRecipe(String name, String portion) {
        String nm = name.strip();
        String pn = StringUtils.deleteWhitespace(portion);        
        if (!validator.isValidRecipeName(nm)) {
            return false;
        }
        if (!recipeExists(nm)) {
            return false;
        }
        try {
            Recipe recipeToUpdate = recipeDao.findByName(nm);
            recipeToUpdate.setName(nm);
            recipeToUpdate.setPortion(Integer.parseInt(pn));
            recipeDao.create(recipeToUpdate);
            List<Ingredient> ingredientsToAdd = ingredientDao.findByRecipe(recipeDao.findByName(nm));
            for (Ingredient ingredient : ingredientsToAdd) {
                Ingredient newIngredient = ingredient;
                newIngredient.setRecipe(recipeToUpdate);
                ingredientDao.remove(ingredient);
                ingredientDao.create(newIngredient);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public List<Recipe> getAllRecipes(User user) {
        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getOwner().equals(user)) {
                returnRecipes.add(recipe);
            }
        }
        return returnRecipes;
    } 
    
    public Recipe getRecipe(String name) {
        String nm = name.strip();
        return recipeDao.findByName(nm);
    }
      
    public boolean recipeExists(String name) {
        String nm = name.strip();
        if (recipeDao.findByName(nm) == null) {
            return false;
        }
        return true;
    }
}