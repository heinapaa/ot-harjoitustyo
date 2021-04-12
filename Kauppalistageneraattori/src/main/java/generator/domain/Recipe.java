package generator.domain;

import generator.dao.RecipeDao;
import java.util.*;

public class Recipe {
    
    private String name;
    private int serving;
    private List<Ingredient> ingredients;
    
    public Recipe(String name, int serving) {
        this.name = name;
        this.serving = serving;
        this.ingredients = new ArrayList<>();
    }   
    
    public void addIngredient(String name, double amount, String unit) {
        ingredients.add(new Ingredient(name, amount, unit));
    }

    public String getName() {
        return name;
    }

    public int getServing() {
        return serving;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
     
}
