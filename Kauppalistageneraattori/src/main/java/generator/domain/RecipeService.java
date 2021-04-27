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
        if (!validator.isValidRecipePortion(pn)) {
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
    
    public boolean updateRecipe(String oldName, String newName, String newPortion) {
        String oldnm = oldName.strip();
        if (!recipeExists(oldnm)) {
            return false;
        }
        String newnm = newName.strip();
        String pn = StringUtils.deleteWhitespace(newPortion); 
        if (!validator.isValidRecipeName(newName) || !validator.isValidRecipePortion(pn)) {
            return false;
        }
        if (!oldnm.equals(newnm) && recipeExists(newnm)) {
            return false;
        }
        try {
            recipeDao.update(newnm, Integer.parseInt(pn), getRecipe(oldnm));                   
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
        if (!validator.isValidRecipeName(name)) {
            return false;
        }        
        if (recipeDao.findByName(name) == null) {
            return false;
        }
        return true;
    }
}