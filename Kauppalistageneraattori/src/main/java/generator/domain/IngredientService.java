package generator.domain;

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
     * 
     * @see                     generator.domain.InputValidator#isValidIngredientName(java.lang.String) 
     * @see                     generator.domain.InputValidator#isValidIngredientUnit(java.lang.String)
     * @see                     generator.domain.InputValidator#isValidIngredientAmount(java.lang.String)
     * @see                     #ingredientExists(generator.domain.Recipe, java.lang.String) 
     * @see                     generator.dao.IngredientDao#create(generator.domain.Ingredient) 
     * 
     * @return                  true jos ainesosan luonti onnistuu, false jos ainesosan luonti epäonnistuu
     */
    
    public boolean addIngredient(Recipe recipe, String ingredientName, String ingredientUnit, String ingredientAmount) {
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
     * 
     * @see                 #ingredientExists(generator.domain.Recipe, java.lang.String) 
     * @see                 generator.dao.IngredientDao#findByRecipe(generator.domain.Recipe) 
     * @see                 generator.dao.IngredientDao#remove(generator.domain.Ingredient) 
     * 
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
     * 
     * @see                     generator.dao.IngredientDao#findByRecipe(generator.domain.Recipe) 
     * 
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
     * 
     * @see             generator.dao.IngredientDao#findByRecipe(generator.domain.Recipe) 
     * 
     * @return          List-rakenne joka sisältää reseptiin liittyvät ainesosat Ingredient-luokan olioina, null jos ainesosia ei ole
     */
    
    public List<Ingredient> getIngredients(Recipe recipe) {
        return ingredientDao.findByRecipe(recipe);
    }    
    
}
