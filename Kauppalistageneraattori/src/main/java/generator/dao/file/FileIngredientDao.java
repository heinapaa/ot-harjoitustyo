package generator.dao.file;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.models.Ingredient;
import generator.models.Recipe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ainesosia tekstitiedostoon tallentava luokka, joka laajentaa FileDao-luokkaa ja toteuttaa IngredientDao-rajapinnan.
 */

public class FileIngredientDao extends FileDao implements IngredientDao {
    
    private List<Ingredient> ingredients;
    
    /**
     * Konstruktori
     * @param fileName  Tiedoston nimi, johon ainesosat halutaan tallentaa
     * @param recipes   RecipeDao-rajapinnan toteuttava olio
     * @throws java.io.IOException
     */
    
    public FileIngredientDao(String fileName, RecipeDao recipes) throws IOException {
        super(fileName);
        this.ingredients = new ArrayList<>();    
        for (String line : super.lines) {
            String[] palat = line.split(";;");    
            String ingredientName = palat[0];
            Double ingredientAmount = Double.valueOf(palat[1]);
            String ingredientUnit = palat[2];
            int recipeId = Integer.valueOf(palat[3]);
            Recipe recipe = recipes.findById(recipeId);
            ingredients.add(new Ingredient(ingredientName, ingredientAmount, ingredientUnit, recipe));
        }                         
    }      

    /**
     * Metodi tallentaa ainesosan.
     * @param ingredient    tallennettava ainesosa Ingredient-oliona
     * @return true jos tallennus onnistuu, muuten false
     */

    @Override
    public boolean create(Ingredient ingredient) {
        ingredients.add(ingredient);
        return save();
    }
    
    /**
     * Metodi poistaa ainesosan.
     * @param ingredient    poistettavaan ainesosaan viittaava Ingredient-olio
     * @return true jos poistaminen onnistuu, muuten false
     */
    
    @Override
    public boolean remove(Ingredient ingredient) {
        ingredients.remove(ingredient);
        return save();
    }
    
    /**
     * Metodi poistaa kaikki tiettyyn reseptiin liittyvt ainesosat.
     * @param recipe    valittuun reseptiin viittaava Recipe-olio
     * @return true jos poistaminen onnistuu, muuten false
     */
    
    @Override
    public boolean removeByRecipe(Recipe recipe) {
        List<Ingredient> deleteList = findByRecipe(recipe);
        for (Ingredient ingredient : deleteList) {
            ingredients.remove(ingredient);
        }
        return save();
    }   
    
    /**
     * Metodi hakee kaikki tiettyyn reseptiin liityvät ainesosat ja palauttaa ne listana
     * @param recipe    haluttuun reseptiin viittaava Recipe-olio
     * @return          List-rakenne, joka sisältää halutut ainesosat Ingredient-olioina
     */

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().equals(recipe)) {
                ingredientList.add(ingredient);
            }
        }
        Collections.sort(ingredientList);
        return ingredientList;
    }    
    
    /**
     * Metodi hakee syötteenä annetun nimen perusteella tiettyyn reseptiin liittyvän ainesosan.
     * 
     * @param name      haetun ainesosan nimi
     * @param recipe    resepti, johon haettu ainesosa liittyy
     * @return haettu ainesosa jos se löytyy, muuten null
     */
    
    @Override
    public Ingredient findByNameAndRecipe(String name, Recipe recipe) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(name) && ingredient.getRecipe().equals(recipe)) {
                return ingredient;
            }
        }
        return null;
    }
    

    /**
     * Metodi hakee kaikki ainesosat ja palauttaa ne listana
     * @return List-rakenne, joka sisältää kaikki tallennetut ainesosat Ingredient-olioina 
     */

    @Override
    public List<Ingredient> findAll() {
        return ingredients;
    }  
        
    private boolean save() {
        super.lines = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            String ingredientName = ingredient.getName();
            Double ingredientAmount = ingredient.getAmount();
            String ingredientUnit = ingredient.getUnit().toString();
            int recipeId = ingredient.getRecipe().getId();
            super.lines.add(ingredientName + ";;" + ingredientAmount + ";;" + ingredientUnit + ";;" + recipeId + "\n");
        }
        return super.writeToFile();
    }     
}