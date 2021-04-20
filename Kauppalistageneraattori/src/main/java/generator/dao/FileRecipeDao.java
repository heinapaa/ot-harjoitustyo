package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileRecipeDao implements RecipeDao {
    
    private List<Recipe> recipes;
    private String file;
    int latestId;
    
    public FileRecipeDao(UserDao users) throws Exception {
        this.recipes = new ArrayList<>();
        this.file = "recipes.txt";
        latestId = 1;
        
        File recipeList = new File(file);
        
        if (recipeList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    String rivi = tiedostonLukija.nextLine();
                    String[] palat = rivi.split(",");
                    recipes.add(new Recipe(Integer.valueOf(palat[0]), palat[1], Integer.valueOf(palat[2]), users.findByUsername(palat[3])));
                    if (Integer.valueOf(palat[0]) > latestId) {
                        latestId = Integer.valueOf(palat[0]);
                    }
                }
            }            
        }        
    }
    
    private void save(){
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (Recipe recipe : recipes) {
                kirjoittaja.write(recipe.getId() + "," + recipe.getName() + "," + recipe.getServing() + "," + recipe.getOwner().getUsername() + "\n");
            }
        } catch (Exception e) {
            
        }        
    }
    
    private int generateId() {
        latestId++;
        return latestId;
    }

    @Override
    public void create(Recipe recipe) {
        Recipe newRecipe = recipe;
        newRecipe.setId(generateId());
        recipes.add(recipe);
        save();
    }

    @Override
    public void remove(Recipe recipe) {
        recipes.remove(recipe);
        save();
    }
    
    @Override
    public Recipe findByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }  
        return null;
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
    
}
