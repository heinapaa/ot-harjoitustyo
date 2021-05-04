package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reseptejä tekstitiedostoon tallentava luokka, joka toteuttaa RecipeDao-rajapinnan.
 */

public class FileRecipeDao implements RecipeDao {
    
    private List<Recipe> recipes;
    private String file;
    int latestId;
    
    public FileRecipeDao(String file, UserDao users) throws Exception {
        this.recipes = new ArrayList<>();  
        this.file = file;
        this.latestId = 1;
        
        File recipeList = new File(file);
        
        if (recipeList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    String rivi = tiedostonLukija.nextLine();
                    String[] palat = rivi.split(";");
                    recipes.add(new Recipe(Integer.valueOf(palat[0]), palat[1], Integer.valueOf(palat[2]), palat[3], users.findByUsername(palat[4])));
                    if (Integer.valueOf(palat[0]) > latestId) {
                        latestId = Integer.valueOf(palat[0]);
                    }
                }
            }            
        } else {
            recipeList.createNewFile();
        }        
    }

    @Override
    public boolean create(Recipe recipe) {
        Recipe newRecipe = recipe;
        newRecipe.setId(generateId());
        recipes.add(recipe);
        return save();
    }
    
    @Override
    public Recipe findById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }     
        return null;
    }    
    
    @Override
    public Recipe findByNameAndUser(String name, User user) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name) && recipe.getOwner().equals(user)) {
                return recipe;
            }
        }  
        return null;
    }    
    
    @Override
    public List<Recipe> findByType(String type) {
        List<Recipe> typeRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getType().equals(type)) {
                typeRecipes.add(recipe);
            }
        }
        return typeRecipes;
    }    

    @Override
    public List<Recipe> findByUser(User user) {
        List<Recipe> userRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getOwner().getUsername().equals(user.getUsername())) {
                userRecipes.add(recipe);
            }
        }
        return userRecipes;
    }

    @Override
    public List<Recipe> findAll() {
        return recipes;
    }  
    
    @Override
    public boolean update(String newName, int newPortion, String newType, Recipe recipe) {
        Recipe recipeToUpdate = recipe;
        recipes.remove(recipe);
        recipeToUpdate.setName(newName);
        recipeToUpdate.setPortion(newPortion);
        recipeToUpdate.setType(newType);
        recipes.add(recipeToUpdate);
        return save();
    }    

    @Override
    public boolean remove(Recipe recipe) {
        recipes.remove(recipe);
        return save();
    }
    
    private int generateId() {
        latestId++;
        return latestId;
    } 
    
    private boolean save() {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (Recipe recipe : recipes) {
                kirjoittaja.write(recipe.getId() + ";" + recipe.getName() + ";" + recipe.getPortion() + ";" + recipe.getType() + ";" + recipe.getOwner().getUsername() + "\n");
            }
        } catch (Exception e) {
            return false;
        }     
        return true;
    }      
}
