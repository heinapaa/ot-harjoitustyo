package generator.services;

import generator.models.Recipe;
import generator.models.Ingredient;
import generator.dao.IngredientDao;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Ainesosien käsittelyyn liittyvästä sovelluslogiikasta vastaava luokka.
 */

public class IngredientService {
    
    private final IngredientDao ingredientDao;
    private final InputValidator validator;   
    
    /**
     * Konstruktori
     * @param ingredientDao     IngredientDao-rajapinnan toteuttava olio, joka vastaa ainesosien tallentamisesta
     * @param validator         InputValidator-olio, joka validoi syötteitä
     */

    public IngredientService(IngredientDao ingredientDao, InputValidator validator) {
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }    
    
    /**
     * Metodi luo annettujen syötteiden perusteella uuden ainesosan.
     * 
     * @param recipe            Resepti, johon uusi ainesosa liitetään
     * @param ingredientName    Syötteenä annettu nimi
     * @param ingredientUnit    Syötteenä annettu yksikkö 
     * @param ingredientAmount  Syötteenä annettu määrä
     * @return                  true jos ainesosan luonti onnistuu, false jos ainesosan luonti epäonnistuu
     */
    
    public boolean addIngredient(Recipe recipe, String ingredientName, String ingredientUnit, String ingredientAmount) throws NumberFormatException {
        String nm = ingredientName.strip();
        String unit = StringUtils.deleteWhitespace(ingredientUnit);
        String amount = StringUtils.deleteWhitespace(ingredientAmount);     
        if (!validator.isValidIngredientName(nm) || !validator.isValidIngredientUnit(unit) || !validator.isValidIngredientAmount(amount)) {
            return false;
        }
        if (ingredientExists(recipe, nm)) {
            return false;
        }
        Ingredient newIngredient = new Ingredient(nm, Double.parseDouble(amount), unit, recipe);
        return ingredientDao.create(newIngredient);
    }    
    
    /**
     * Metodi poistaa valitun ainesosan.
     * 
     * @param recipe        Resepti, johon poistettava ainesosa liittyy
     * @param ingredient    Poistettavan ainesosa
     * @return              true jos ainesosan poistaminen onnistuu, false jos ainesosan poistaminen epäonnistuu
     */
    
    public boolean removeIngredient(Recipe recipe, Ingredient ingredient) {
        if (!ingredientExists(recipe, ingredient.getName())) {
            return false;
        }
        List<Ingredient> ingredientList = ingredientDao.findByRecipe(recipe);
        for (Ingredient listIngredient : ingredientList) {
            if (listIngredient.equals(ingredient)) {
                return ingredientDao.remove(ingredient);
            }
        }
        return false;
    }     
    
    /**
     * Metodi tarkistaa valitun reseptin ja syötteenä annetun ainesosan nimen perusteella, liittyykö kyseiseen reseptiin sen niminen ainesosa.
     * 
     * @param recipe            Resepti, johon ainesosa liittyy
     * @param ingredientName    Syötteenä annettu ainesosan nimi
     * @return                  true jos ainesosa on olemassa, false jos ainesosaa ei ole olemassa
     */
    
    public boolean ingredientExists(Recipe recipe, String ingredientName) {
        if (recipe == null) {
            return false;
        }           
        List<Ingredient> existingIngredients = ingredientDao.findByRecipe(recipe);
        if (existingIngredients.isEmpty()) {
            return false;
        }        
        for (Ingredient ingredient : existingIngredients) {
            if (ingredient.nameEquals(ingredientName)) {
                return true;
            }
        }
        return false;
    }    
    
    /**
     * Metodi hakee kaikki tiettyyn reseptiin liittyvät ainesosat
     * 
     * @param recipe    Resepti, johon ainesosat liittyvät
     * @return          List-rakenne joka sisältää reseptiin liittyvät ainesosat Ingredient-luokan olioina
     */
    
    public List<Ingredient> getIngredients(Recipe recipe) {
        return ingredientDao.findByRecipe(recipe);
    }
    
    /**
     * Metodi hakee kaikki tiettyihin resepteihin liittyvät ainesosat
     * @param recipes   List-rakenne, joka sisältää ne reseptit joiden ainesosat halutaan 
     * @return          List-rakenne joka sisältää resepteihin liittyvät ainesosat Ingredient-luokan olioina
     */    

    public List<Ingredient> getIngredients(List<Recipe> recipes) {
        return ingredientDao.findByRecipes(recipes);
    }
    
}
