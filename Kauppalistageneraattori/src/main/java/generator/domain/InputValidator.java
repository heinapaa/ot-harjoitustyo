package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.List;
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
        if (input.isBlank()
                || input.isEmpty()
                || StringUtils.containsWhitespace(input)
                || StringUtils.endsWith(input, " ")
                || StringUtils.contains(input, ";")) {
            return false;
        }        
        String nm = StringUtils.deleteWhitespace(input);
        if (input.length() < 3) {
            return false;
        }
        return true;
    }
    
    public boolean isValidRecipeName(String input) {
        if (input.isBlank()
                || input.isEmpty()
                || StringUtils.trimToEmpty(input).isEmpty()
                || StringUtils.startsWith(input, " ")
                || StringUtils.endsWith(input, " ")
                || StringUtils.contains(input, ";")) {
            return false;
        }
        return true;
    }   
    
    public boolean isValidRecipePortion(String input) {   
        if (input.isBlank() || input.isEmpty() || StringUtils.containsWhitespace(input)) {
            return false;
        } 
        try {
            int serving = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public boolean isValidIngredientName(String input) {  
        if (input.isBlank()
                || input.isEmpty()
                || StringUtils.trimToEmpty(input).isEmpty()
                || StringUtils.startsWith(input, " ")
                || StringUtils.endsWith(input, " ")
                || StringUtils.contains(input, ";")) {
            return false;
        }        
        if (input.isBlank() || input.isEmpty()) {
            return false;
        }
        return true;
    }    
    
    public boolean isValidIngredientAmount(String input) {       
        if (input.isBlank() || input.isEmpty() || StringUtils.containsWhitespace(input)) {
            return false;
        } 
        try {
            double serving = Double.parseDouble(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;    
    }
    
    public boolean isValidIngredientUnit(String input) {
        if (input.equals("kpl") || input.equals("g") || input.equals("kg") || input.equals("dl") || input.equals("l")) {
            return true;
        }
        return false;
    }
    
}
