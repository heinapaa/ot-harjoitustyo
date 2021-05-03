package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 * Ainesosia tekstitiedostoon tallentava luokka.
 */

public class FileIngredientDao implements IngredientDao {
    
    private List<Ingredient> ingredients;
    private String file;
    private int latestId;
    
    public FileIngredientDao(String file, RecipeDao recipes) throws Exception {
        this.ingredients = new ArrayList<>();
        this.file = file;     
        this.latestId = 1;
        
        File ingredientList = new File(file);
        if (ingredientList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    String rivi = tiedostonLukija.nextLine();
                    String[] palat = rivi.split(";");    
                    int ingredientId = Integer.valueOf(palat[0]);
                    String ingredientName = palat[1];
                    Double ingredientAmount = Double.valueOf(palat[2]);
                    String ingredientUnit = palat[3];
                    int recipeId = Integer.valueOf(palat[4]);
                    Recipe recipe = recipes.findById(recipeId);
                    ingredients.add(new Ingredient(ingredientId, ingredientName, ingredientAmount, ingredientUnit, recipe));
                    if (ingredientId > latestId) {
                        latestId = ingredientId;
                    }
                }
            }             
        } else {
            ingredientList.createNewFile();
        }                          
    }   
    
    private boolean save() {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (Ingredient ingredient : ingredients) {
                int ingredientId = ingredient.getId();
                String ingredientName = ingredient.getName();
                Double ingredientAmount = ingredient.getAmount();
                String ingredientUnit = ingredient.getUnit();
                int recipeId = ingredient.getRecipe().getId();
                kirjoittaja.write(ingredientId + ";" + ingredientName + ";" + ingredientAmount + ";" + ingredientUnit + ";" + recipeId + "\n");
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }    
    
    private int generateId() {
        latestId++;
        return latestId - 1;
    }  
    
    /**
     * Tallentaa ainesosan
     * @param ingredient    tallennettava ainesosa Ingredient-oliona
     * @return true jos tallennus onnistuu, muuten false
     */

    @Override
    public boolean create(Ingredient ingredient) {
        Ingredient newIngredient = ingredient;
        newIngredient.setId(generateId());
        ingredients.add(newIngredient);
        return save();
    }
    
    /**
     * Poistaa ainesosan.
     * @param ingredient    poistettavaan ainesosaan viittaava Ingredient-olio
     * @return true jos poistaminen onnistuu, muuten false
     */
    
    @Override
    public boolean remove(Ingredient ingredient) {
        ingredients.remove(ingredient);
        return save();
    }
    
    /**
     * Poistaa kaikki tiettyyn reseptiin liittyvt ainesosat.
     * @param recipe    valittuun reseptiin viittaava Recipe-olio
     * @return true jos poistaminen onnistuu, muuten false
     */
    
    
    @Override
    public boolean removeByRecipe(Recipe recipe) {
        List<Ingredient> deleteList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().getId() == recipe.getId()) {
                deleteList.add(ingredient);
            }
        }
        for (Ingredient ingredient : deleteList) {
            ingredients.remove(ingredient);
        }
        return save();
    }   
    
    /**
     * Hakee kaikki tiettyyn reseptiin liityvät ainesosat ja palauttaa ne listana
     * @param recipe    haluttuun reseptiin viittaava Recipe-olio
     * @return          List-rakenne, joka sisältää halutut ainesosat Ingredient-olioina
     */

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().getId() == recipe.getId()) {
                ingredientList.add(ingredient);
            }
        }
        Collections.sort(ingredientList);
        return ingredientList;
    }    
    
    @Override
    public Ingredient findById(int id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        
        return null;
    }
    
    /**
     * Hakee kaikki reseptit ja palauttaa ne listana
     * @return List-rakenne, joka sisältää kaikki tallennetut ainesosat Ingredient-olioina 
     */

    @Override
    public List<Ingredient> findAll() {
        return ingredients;
    }  
}