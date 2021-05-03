package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Syötteiden validoinnista vastaava luokka.
 */


public class InputValidator {

    private final UserDao userDao;
    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private List<String> acceptableTypes;
    
    public InputValidator(UserDao userDao, RecipeDao recipeDao, IngredientDao ingredientDao, List<String> acceptableTypes) {
        this.userDao = userDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.acceptableTypes = acceptableTypes;
    }
    
    /**
     * Metodi testaa täyttääkö syöte käyttäjänimelle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */
    
    public boolean isValidUserName(String input) {
        if (!passesStringTest(input)) {
            return false;
        } else if (StringUtils.containsWhitespace(input)) {
            return false;
        } else if (input.length() < 3) {
            return false;
        }        
        return true;
    }
    
    /**
     * Metodi testaa täyttääkö syöte reseptin nimelle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte 
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidRecipeName(String input) {
        return passesStringTest(input);
    }   
    
    /**
     * Metodi testaa täyttääkö syöte reseptin annoskoolle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidRecipePortion(String input) {   
        if (!passesStringTest(input)) {
            return false;
        }
        try {
            int serving = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    /**
     * Metodi testaa täyttääkö syöte reseptin tyypille asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidRecipeType(String input) {
        if (!passesStringTest(input)) {
            return false;
        } else {
            for (String type : acceptableTypes) {
                if (type.equals(input)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Metodi testaa täyttääkö syöte ainesosan nimelle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidIngredientName(String input) {       
        return passesStringTest(input);
    }    
    
    /**
     * Metodi testaa täyttääkö syöte ainesosan määrälle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidIngredientAmount(String input) {  
        if (!passesStringTest(input)) {
            return false;
        }
        try {
            double serving = Double.parseDouble(input);
        } catch (NumberFormatException nfe) {
            return false;
        }            
        return true;    
    }
    
    /**
     * Metodi testaa täyttääkö syöte ainesosan yksikölle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    public boolean isValidIngredientUnit(String input) {
        if (!passesStringTest(input)) {
            return false;
        } else if (input.equals("kpl") || input.equals("g") || input.equals("kg") || input.equals("dl") || input.equals("l")) {
            return true;
        }
        return false;
    }
    
    /**
     * Metodi testaa täyttääkö syöte yleiset String-muotoa olevalle syötteelle asetetut vaatimukset.
     * @param input     Käyttäjän antama syöte
     * @return          true jos syöte täyttää vaatimukset, false muuten
     */    
    
    private boolean passesStringTest(String input) {
        if (input == null) {
                    return false;
        } else if (input.isBlank()
                || StringUtils.trimToEmpty(input).isEmpty()
                || StringUtils.startsWith(input, " ")
                || StringUtils.endsWith(input, " ")
                || StringUtils.containsIgnoreCase(input, ";;")) {
            return false;
        } 
        return true;
    }
    
}
