package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class IngredientService {
    
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private InputValidator validator;    
    
    public IngredientService(RecipeDao recipeDao, IngredientDao ingredientDao, InputValidator validator) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }     
    
    public boolean addIngredient(String recipeName, String ingredientName, String ingredientUnit, String ingredientAmount) {
        String rnm = recipeName.strip();
        String inm = ingredientName.strip();
        String unit = StringUtils.deleteWhitespace(ingredientUnit);
        String amount = StringUtils.deleteWhitespace(ingredientAmount);     
        if (!validator.isValidRecipeName(rnm) || !validator.isValidIngredientUnit(unit) || !validator.isValidIngredientAmount(amount)) {
            return false;
        }
        Recipe recipe = recipeDao.findByName(rnm);
        if (ingredientExists(recipe, inm)) {
            return false;
        }
        Ingredient newIngredient = new Ingredient(inm, Double.parseDouble(amount), unit);
        newIngredient.setRecipe(recipeDao.findByName(rnm));
        ingredientDao.create(newIngredient);
        return true;
    }    
    
    public boolean removeIngredient(String recipeName, String ingredientName) {
        String rnm = recipeName.strip();
        String inm = ingredientName.strip();
        if (!validator.isValidRecipeName(rnm) || !validator.isValidIngredientName(inm)) {
            return false;
        }
        if (!ingredientExists(recipeDao.findByName(rnm), inm)) {
            return false;
        }
        List<Ingredient> ingredientList = ingredientDao.findByRecipe(recipeDao.findByName(rnm));
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getName().equals(inm)) {
                ingredientDao.remove(ingredient);
                return true;
            }
        }
        return false;
    }     
    
    public boolean ingredientExists(Recipe recipe, String ingredientName) {
        String inm = ingredientName.strip();
        if (recipe == null) {
            return false;
        }        
        List<Ingredient> existingIngredients = ingredientDao.findByRecipe(recipe);
        if (existingIngredients.isEmpty()) {
            return false;
        }
        for (Ingredient ingredient : existingIngredients) {
            if (ingredient.getName().equals(inm)) {
                return true;
            }
        }
        return false;
    }    
    
    public Ingredient getIngredient(String recipeName, String ingredientName) {
        String rnm = recipeName.strip();
        String inm = ingredientName.strip();
        if (!validator.isValidRecipeName(rnm) || !validator.isValidIngredientName(inm)) {
            return null;
        }
        Recipe recipe = recipeDao.findByName(rnm);
        if (!ingredientExists(recipe, inm)) {
            return null;
        }
        List<Ingredient> ingredientsInRecipe = ingredientDao.findByRecipe(recipe);
        for (Ingredient ingredient : ingredientsInRecipe) {
            if (inm.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }   
    
    public List<Ingredient> getIngredients(String recipeName) {
        String rnm = recipeName.strip();
        if (!validator.isValidRecipeName(rnm)) {
            return null;
        }
        if (recipeDao.findByName(rnm) == null) {
            return null;
        }
        Recipe recipe = recipeDao.findByName(rnm);
        return ingredientDao.findByRecipe(recipe);
    }    
    
}
