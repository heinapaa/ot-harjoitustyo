package generator.domain;

import generator.dao.IngredientDao;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Kauppalistojen luomiseen liittyvästä sovelluslogiikasta vastaava luokka.
 */


public class ShoppingListService {
 
    private final IngredientDao ingredientDao;
    
    public ShoppingListService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    } 
    
    /**
     * Palauttaa Recipe-luokan olioita sisältävän List-rakenteen perusteella String-muotoisen kauppalistan, jossa reseptien sisältämät ainesosat on mahdollisuuksien mukaan summattu yhteen.
     * 
     * @param recipes   List-luokan olio, joka sisältää kauppalistalle valitut reseptit Recipe-luokan olioina
     * 
     * @see             #getIngredientsForAllRecipes(java.util.List) 
     * @see             #sumIngredients(java.util.List) 
     * @see             #printShoppingList(java.util.Map) 
     * 
     * @return String-muotoinen kauppalista, jossa jokainen ainesosa määrineen on omalla rivillään.
     */
    
    public String createShoppingList(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            return "";
        }
        List<Ingredient> longList = getIngredientsForAllRecipes(recipes);
        Map<String, Double> uniqueIngredients = sumIngredients(longList);
        return printShoppingList(uniqueIngredients);
    }
    
    public Map<String, Double> sumIngredients(List<Ingredient> longList) {
        Map<String, Double> nameList = new HashMap<>();
        for (Ingredient ingredient : longList) {
            if (ingredient.getUnit().isWeight()) {
                if (!nameList.containsKey(ingredient.getName() + "_WEIGHT")) {
                    nameList.put(ingredient.getName() + "_WEIGHT", convertWeight(ingredient));
                } else {
                    Double oldWeight = nameList.get(ingredient.getName() + "_WEIGHT");
                    Double newWeight = oldWeight + convertWeight(ingredient);
                    nameList.put(ingredient.getName() + "_WEIGHT", newWeight);
                }
            } else if (ingredient.getUnit().isVolume()) {
                if (!nameList.containsKey(ingredient.getName() + "_VOLUME")) {
                    nameList.put(ingredient.getName() + "_VOLUME", convertVolume(ingredient));
                } else {
                    Double oldVolume = nameList.get(ingredient.getName() + "_VOLUME");
                    Double newVolume = oldVolume + convertVolume(ingredient);
                    nameList.put(ingredient.getName() + "_VOLUME", newVolume);
                }                
            } else if (ingredient.getUnit().isAmount()) {
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
        if (ingredient.getUnit() == Unit.G) {
            weight = weight + ingredient.getAmount() / 1000;
        } else if (ingredient.getUnit() == Unit.KG) {
            weight = weight + ingredient.getAmount();
        } 
        
        return weight;
    }
    
    public double convertVolume(Ingredient ingredient) {
        double weight = 0;
        if (ingredient.getUnit() == Unit.DL) {
            weight = weight + ingredient.getAmount() / 10;
        } else if (ingredient.getUnit() == Unit.L) {
            weight = weight + ingredient.getAmount();
        } 
        
        return weight;
    }    
    
    public String printShoppingList(Map<String, Double> uniqueIngredients) {
        StringBuilder shoppingList = new StringBuilder();
        List<String> lines = new ArrayList<>();
        uniqueIngredients.keySet().stream()
                .sorted()
                .forEach(s -> lines.add(s));
        Collections.sort(lines);   
        
        for (String line : lines) {
            String[] s = line.split("_");
            if (s[1].equals("WEIGHT")) {
                shoppingList.append(s[0] + ", " + uniqueIngredients.get(line) + " kg" + "\n");                
            } else if (s[1].equals("VOLUME")) {
                shoppingList.append(s[0] + ", " + uniqueIngredients.get(line) + " l" + "\n");                       
            } else if (s[1].equals("PCS")) {
                shoppingList.append(s[0] + ", " + uniqueIngredients.get(line) + " kpl" + "\n");                       
            }
        }
        
        return shoppingList.toString();
    }
    
    public List<Ingredient> getIngredientsForAllRecipes(List<Recipe> recipes) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<Ingredient> ingredients = ingredientDao.findByRecipe(recipe);   
            for (Ingredient ingredient : ingredients) {
                ingredientList.add(ingredient);
            }
        }
        return ingredientList;        
    }
    
    public boolean saveToFile(String shoppingList, File file) {
        return true;
    }
}
