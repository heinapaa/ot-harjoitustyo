package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileIngredientDao implements IngredientDao {
    
    private List<Ingredient> ingredients;
    private String file;
    private int latestId;
    
    public FileIngredientDao(RecipeDao recipes) throws Exception {
        this.ingredients = new ArrayList<>();
        this.file = "ingredients.txt";    
        this.latestId = 1;
        
        File ingredientList = new File(file);
        
        if (ingredientList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    String rivi = tiedostonLukija.nextLine();
                    String[] palat = rivi.split(",");    
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
    
    private void save() {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (Ingredient ingredient : ingredients) {
                int ingredientId = ingredient.getId();
                String ingredientName = ingredient.getName();
                Double ingredientAmount = ingredient.getAmount();
                String ingredientUnit = ingredient.getUnit();
                int recipeId = ingredient.getRecipe().getId();
                kirjoittaja.write(ingredientId + "," + ingredientName + "," + ingredientAmount + "," + ingredientUnit + "," + recipeId + "\n");
            }
        } catch (Exception e) {
            
        }
    }    
    
    private int generateId() {
        latestId++;
        return latestId - 1;
    }    

    @Override
    public void create(Ingredient ingredient) {
        Ingredient newIngredient = ingredient;
        newIngredient.setId(generateId());
        ingredients.add(newIngredient);
        save();
    }
    
    @Override
    public void remove(Ingredient ingredient) {
        ingredients.remove(ingredient);
        save();
    }
    
    
    @Override
    public void removeByRecipe(Recipe recipe) {
        List<Ingredient> deleteList = new ArrayList<>();
        
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipe().getId() == recipe.getId()) {
                deleteList.add(ingredient);
            }
        }
        
        for (Ingredient ingredient : deleteList) {
            ingredients.remove(ingredient);
        }
        
        save();
    }   

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

    @Override
    public List<Ingredient> findAll() {
        return ingredients;
    }
    
}
