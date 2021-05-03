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
    
    public boolean createRecipe(String name, String portion, String type, User user) {
        String nm = name.strip();
        String pn = StringUtils.deleteWhitespace(portion);
        String tp = type.strip();
        if (!validator.isValidRecipeName(nm) || !validator.isValidRecipePortion(pn) || !validator.isValidRecipeType(tp)) {
            return false;
        }
        if (recipeExists(nm)) {
            return false;
        }
        Recipe newRecipe = new Recipe(nm, Integer.parseInt(pn), tp, user);
        try {
            recipeDao.create(newRecipe);   
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean removeRecipe(Recipe recipe) {
        if (!recipeExists(recipe.getName())) {
            return false;
        }
        try {
            ingredientDao.removeByRecipe(recipe);
            recipeDao.remove(recipe);            
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean updateRecipe(Recipe recipe, String newName, String newPortion, String newType) {
        String newnm = newName.strip();
        String newpn = StringUtils.deleteWhitespace(newPortion); 
        String newtp = newType.strip();
        if (!validator.isValidRecipeName(newnm) || !validator.isValidRecipePortion(newpn) || !validator.isValidRecipeType(newtp)) {
            return false;
        }
        if (!recipe.getName().equals(newnm) && recipeExists(newnm)) {
            return false;
        }
        try {
            recipeDao.update(newnm, Integer.parseInt(newpn), newtp, recipe);                   
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
        if (recipeDao.findByName(name) == null) {
            return false;
        }
        return true;
    }
}