package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import org.apache.commons.lang3.StringUtils;

public class InputValidator {

    private UserDao userDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    
    public InputValidator(UserDao userDao, RecipeDao recipeDao, IngredientDao ingredientDao) {
        this.userDao = userDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
    }
    
    public boolean isValidUserName(String input) {
        if (input == null) {
            return false;
        } else if (input.isBlank()
                || input.isEmpty()
                || StringUtils.containsWhitespace(input)
                || StringUtils.containsIgnoreCase(input, ";;")
                || input.length() < 3) {
            return false;
        }        
        return true;
    }
    
    public boolean isValidRecipeName(String input) {
        if (input == null) {
            return false;
        } else if (input.isBlank()
                || input.isEmpty()
                || StringUtils.trimToEmpty(input).isEmpty()
                || StringUtils.startsWith(input, " ")
                || StringUtils.endsWith(input, " ")
                || StringUtils.containsIgnoreCase(input, ";;")) {
            return false;
        }
        return true;
    }   
    
    public boolean isValidRecipePortion(String input) {   
        if (input == null) {
            return false;
        } else if (input.isBlank() || input.isEmpty() || StringUtils.containsWhitespace(input)) {
            return false;
        } else {
            try {
                int serving = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;            
        }
    }
    
    public boolean isValidRecipeType(String input) {
        if (input == null) {
            return false;
        } else if (input.equals("kana") || input.equals("kala") || input.equals("kasvis") || input.equals("liha") || input.equals("makea")) {
            return true;
        }
        return false;
    }
    
    public boolean isValidIngredientName(String input) { 
        if (input == null) {
            return false;
        } else if (input.isBlank()
                || input.isEmpty()
                || StringUtils.trimToEmpty(input).isEmpty()
                || StringUtils.startsWith(input, " ")
                || StringUtils.endsWith(input, " ")
                || StringUtils.containsIgnoreCase(input, ";;")) {
            return false;
        }        
        return true;
    }    
    
    public boolean isValidIngredientAmount(String input) {  
        if (input == null) {
            return false;
        } else if (input.isBlank() || input.isEmpty() || StringUtils.containsWhitespace(input)) {
            return false;
        } else {
            try {
                double serving = Double.parseDouble(input);
            } catch (NumberFormatException nfe) {
                return false;
            }            
        }
        return true;    
    }
    
    public boolean isValidIngredientUnit(String input) {
        if (input == null) {
            return false;
        } else if (input.equals("kpl") || input.equals("g") || input.equals("kg") || input.equals("dl") || input.equals("l")) {
            return true;
        }
        return false;
    }
    
}
