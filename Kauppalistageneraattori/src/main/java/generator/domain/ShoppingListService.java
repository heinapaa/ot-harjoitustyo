package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ShoppingListService {
 
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    
    public ShoppingListService(RecipeDao recipeDao, IngredientDao ingredientDao) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
    }   
    
    public String createShoppingList(List<Recipe> recipes) {
        if (recipes.isEmpty() || recipes.size() == 0 || recipes == null) {
            return "";
        }
        List<Ingredient> longList = getIngredientsForAllRecipes(recipes);
        Map<String, Double> uniqueIngredients = sumIngredients(longList);
        return printShoppingList(uniqueIngredients);
    }
    
    public Map<String, Double> sumIngredients(List<Ingredient> longList) {
        Map<String, Double> nameList = new HashMap<>();
        for (Ingredient ingredient : longList) {
            if (isWeight(ingredient)) {
                if (!nameList.containsKey(ingredient.getName() + "_WEIGHT")) {
                    nameList.put(ingredient.getName() + "_WEIGHT", convertWeight(ingredient));
                } else {
                    Double oldWeight = nameList.get(ingredient.getName() + "_WEIGHT");
                    Double newWeight = oldWeight + convertWeight(ingredient);
                    nameList.put(ingredient.getName() + "_WEIGHT", newWeight);
                }
            } else if (isVolume(ingredient)) {
                if (!nameList.containsKey(ingredient.getName() + "_VOLUME")) {
                    nameList.put(ingredient.getName() + "_VOLUME", convertVolume(ingredient));
                } else {
                    Double oldVolume = nameList.get(ingredient.getName() + "_VOLUME");
                    Double newVolume = oldVolume + convertVolume(ingredient);
                    nameList.put(ingredient.getName() + "_VOLUME", newVolume);
                }                
            } else if (ingredient.getUnit().equals("kpl")) {
                if (!nameList.containsKey(ingredient.getName() + "_PCS")) {
                    nameList.put(ingredient.getName() + "_PCS", ingredient.getAmount());
                } else {
                    Double oldAmount = nameList.get(ingredient.getName() + "_PCS");
                    Double newAmount = oldAmount + ingredient.getAmount();
                    nameList.put(ingredient.getName() + "_PCS", newAmount);
                }                
            }
        }
        return nameList;
    }
    
    public double convertWeight(Ingredient ingredient) {
        double weight = 0;
        if (ingredient.getUnit().equals("g")) {
            weight = weight + ingredient.getAmount() / 1000;
        } else if (ingredient.getUnit().equals("kg")) {
            weight = weight + ingredient.getAmount();
        } 
        
        return weight;
    }
    
    public double convertVolume(Ingredient ingredient) {
        double weight = 0;
        if (ingredient.getUnit().equals("dl")) {
            weight = weight + ingredient.getAmount() / 10;
        } else if (ingredient.getUnit().equals("l")) {
            weight = weight + ingredient.getAmount();
        } 
        
        return weight;
    }    
    
    public String printShoppingList(Map<String, Double> uniqueIngredients) {
        String printString = "";
        List<String> lines = new ArrayList<>();
        uniqueIngredients.keySet().stream()
                .sorted()
                .forEach(s -> lines.add(s));
        Collections.sort(lines);
        for (String line : lines) {
            String[] s = line.split("_");
            if (s[1].equals("WEIGHT")) {
                printString = printString + s[0] + ", " + uniqueIngredients.get(line) + " kg" + "\n";                
            } else if (s[1].equals("VOLUME")) {
                printString = printString + s[0] + ", " + uniqueIngredients.get(line) + " l" + "\n";                       
            } else if (s[1].equals("PCS")) {
                printString = printString + s[0] + ", " + uniqueIngredients.get(line) + " kpl" + "\n";                       
            }
        }
        return printString;
    }
    
    public List<Ingredient> getIngredientsForAllRecipes(List<Recipe> recipes) {
        List<Ingredient> ingredientList = new LinkedList<>();
        
        for (Recipe recipe : recipes) {
            List<Ingredient> ingredients = ingredientDao.findByRecipe(recipe);   
            for (Ingredient ingredient : ingredients) {
                ingredientList.add(ingredient);
            }
        }
        
        return ingredientList;        
    }
    
    public boolean isVolume(Ingredient ingredient) {
        if (ingredient.getUnit().equals("dl") || ingredient.getUnit().equals("l")) {
            return true;
        }
        return false;
    }
    
    public boolean isWeight(Ingredient ingredient) {
        if (ingredient.getUnit().equals("g") || ingredient.getUnit().equals("kg")) {
            return true;
        }
        return false;
    }  
    
}
