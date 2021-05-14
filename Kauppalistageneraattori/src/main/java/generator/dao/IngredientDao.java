package generator.dao;

import generator.models.Ingredient;
import generator.models.Recipe;
import java.util.List;

/**
 * Ainesosien tallennukseen liittyvien luokkien rajapinta.
 */

public interface IngredientDao {
    
    /**
     * Metodi tallentaa ainesosan.
     * @param ingredient    Tallennettava ainesosa
     * @return              true jos tallennus onnistuu, muuten false
     */
    
    boolean create(Ingredient ingredient);
    
    /**
     * Metodi poistaa ainesosan
     * @param ingredient    Poistettava ainesosa
     * @return  true jos poistaminen onnistuu, muuten false
     */
    
    boolean remove(Ingredient ingredient);
    
    /**
     * Metodi poistaa kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipe    Resepti, johon liittyvät ainesosan halutaan poistaa
     * @return true jos ainesosien poistaminen onnistuu, muuten false
     */
    
    boolean removeByRecipe(Recipe recipe);
    
    /**
     * Metodi hakee syötteenä annetun nimen perusteella tiettyyn reseptiin liittyvän ainesosan.
     * 
     * @param name      haetun ainesosan nimi
     * @param recipe    resepti, johon haettu ainesosa liittyy
     * @return haettu ainesosa jos se löytyy, muuten null
     */
    
    Ingredient findByNameAndRecipe(String name, Recipe recipe);
    
    /**
     * Meodi palauttaa kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipe    Resepti, johon liittyvät ainesosan halutaan poistaa
     * @return List-rakenne, joka sisältää kaikki valittuun reseptiin liittyvät ainesosat
     */
    
    List<Ingredient> findByRecipe(Recipe recipe);
    
    /**
     * Metodi palauttaa kaikki tallennetut ainesosat.
     * @return List-rakenne, joka sisältää kaikki tallennetut ainesosat.
     */
    
    List<Ingredient> findAll();
    
    /**
     * Metodi palauttaa kaikki listattuihin resepteihin liittyvät ainesosat
     * @param recipes   List-rakenne, joka sisältää ne reseptit joihin liittyvät ainesosat halutaan
     * @return List-rakenne, joka sisältää kaikki valittuihin resepteihin liittyvät ainesosat
     */
    
    List<Ingredient> findByRecipes(List<Recipe> recipes);
}
