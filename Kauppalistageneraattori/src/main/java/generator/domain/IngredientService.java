package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Ainesosien käsittelyyn liittyvästä sovelluslogiikasta vastaava luokka.
 */

public class IngredientService {
    
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private InputValidator validator;    
    
    public IngredientService(RecipeDao recipeDao, IngredientDao ingredientDao, InputValidator validator) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }    
    
    /**
     * Metodi luo syötteiden perusteella uuden ainesosan.
     * 
     * @param recipe    Resepti, johon uusi ainesosa liitetään
     * @param ingredientName    Käyttäjän antama nimi
     * @param ingredientUnit    Käyttäjän antama yksikkö 
     * @param ingredientAmount  Käyttäjän antama määrä
     * 
     * @see generator.domain.InputValidator#isValidIngredientName(java.lang.String) 
     * @see generator.domain.InputValidator#isValidIngredientUnit(java.lang.String)
     * @see generator.domain.InputValidator#isValidIngredientAmount(java.lang.String)
     * 
     * @return true jos ainesosan luonti onnistuu, false jos ainesosan luonti epäonnistuu
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
        Ingredient newIngredient = new Ingredient(nm, Double.parseDouble(amount), unit);
        newIngredient.setRecipe(recipe);
        ingredientDao.create(newIngredient);
        return true;
    }    
    
    /**
     * Metodi poistaa valittuun reseptiin liittyvän valitun ainesosan.
     * 
     * @param recipe    Resepti, johon liittyvä ainesosa halutaan poistaa
     * @param ingredient    Poistettavan ainesosa
     * 
     * @return true jos ainesosan poistaminen onnistuu, false jos ainesosan poistaminen epäonnistuu
     */
    
    public boolean removeIngredient(Recipe recipe, Ingredient ingredient) {
        if (!ingredientExists(recipe, ingredient.getName())) {
            return false;
        }
        List<Ingredient> ingredientList = ingredientDao.findByRecipe(recipe);
        for (Ingredient listIngredient : ingredientList) {
            if (listIngredient.equals(ingredient)) {
                ingredientDao.remove(ingredient);
                return true;
            }
        }
        return false;
    }     
    
    /**
     * Metodi tarkistaa syötteenä annetun reseptin ja ainesosan nimen perusteella, onko tietty ainesosa olemassa.
     * 
     * @param recipe    Resepti, johon ainesosa liittyy
     * @param ingredientName    Ainesosan nimi
     * 
     * @return true jos ainesosa on olemassa, false jos ainesosaa ei ole olemassa
     */
    
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
    
    /**
     * Metodi palauttaa ainesosan syötteenä annetun nimen perusteella.
     * 
     * @param recipe    Resepti, johon ainesosa liittyy
     * @param ingredientName    Ainesosan nimi 
     * 
     * @return ainesosa mikäli kyseiseen reseptiin liittyy kyseinen ainesosa, muuten null
     */
    /*
    public Ingredient getIngredient(Recipe recipe, String ingredientName) {
        String nm = ingredientName.strip();
        if (!validator.isValidIngredientName(nm)) {
            return null;
        }
        if (!ingredientExists(recipe, nm)) {
            return null;
        }
        List<Ingredient> ingredientsInRecipe = ingredientDao.findByRecipe(recipe);
        for (Ingredient ingredient : ingredientsInRecipe) {
            if (nm.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }   */
    
    /**
     * Metodi palauttaa kaikki tiettyyn reseptiin liittyvät ainesosat
     * 
     * @param recipe    Resepti, johon liittyvät ainesosat halutaan
     * 
     * @return  Lista reseptiin liittyvistä ainesosista, null jos ainesosia ei ole
     */
    
    public List<Ingredient> getIngredients(Recipe recipe) {
        return ingredientDao.findByRecipe(recipe);
    }    
    
}
