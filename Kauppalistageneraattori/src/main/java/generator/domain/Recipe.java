package generator.domain;

import generator.dao.RecipeDao;
import java.util.*;

public class Recipe implements RecipeDao {
    
    private String name;
    private int serving;
    private List<Ingredient> ingredients;
    
    public Recipe(String name, int serving) {
        this.name = name;
        this.serving = serving;
        this.ingredients = new ArrayList<>();
    }   
    
    @Override
    public void addIngredient(String name, double amount, String unit) {
        ingredients.add(new Ingredient(name, amount, unit));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    @Override
    public void create(Recipe recipe) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    } 
     
}
