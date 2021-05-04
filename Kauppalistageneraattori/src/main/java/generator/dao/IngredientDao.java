package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.util.List;

/**
 * Ainesosien tallennukseen liittyvien luokkien rajapinta.
 */

public interface IngredientDao {
    
    /**
     * Tallentaa ainesosan.
     * @param ingredient    Valittu ainesosa.
     * @return              true jos tallennus onnistuu, muuten false
     */
    
    boolean create(Ingredient ingredient);
    boolean remove(Ingredient ingredient);
    boolean removeByRecipe(Recipe recipe);
    List<Ingredient> findByRecipe(Recipe recipe);
    Ingredient findById(int id);
    List<Ingredient> findAll();
    
}
