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
    
    public FileRecipeDao(UserDao users) throws Exception {
        this.recipes = new ArrayList<>();
        this.file = "recipes.txt";
        
        File recipeList = new File(file);
        
        if (recipeList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(file))) {
                while (tiedostonLukija.hasNextLine()) {
                    String rivi = tiedostonLukija.nextLine();
                    String[] palat = rivi.split(",");
                    recipes.add(new Recipe(palat[0],Integer.valueOf(palat[1]), users.findByUsername(palat[2])));
                }
            }             
        }        
    }
    
    private void save() throws Exception {
        try (FileWriter kirjoittaja = new FileWriter(new File(file))) {
            for (Recipe recipe : recipes) {
                kirjoittaja.write(recipe.getName() + "," + recipe.getServing() + "," + recipe.getOwner().getUsername() + "\n");
            }
        }         
    }

    @Override
    public void create(Recipe recipe) throws Exception {
        recipes.add(recipe);
        save();
    }

    @Override
    public void remove(Recipe recipe) throws Exception {
        recipes.remove(recipe);
        save();
    }
    
    @Override
    public Recipe findByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name)) return recipe;
        }
        
        return null;
    }    

    @Override
    public List<Recipe> findAll() {
        return recipes;
    }
    
}
