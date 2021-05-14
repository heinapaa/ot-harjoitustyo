package generator.services;

import generator.models.Recipe;
import generator.models.Ingredient;
import generator.dao.IngredientDao;
import generator.models.ShoppingList;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

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
        ShoppingList shoppingList = new ShoppingList();
        List<Ingredient> longList = ingredientDao.findByRecipes(recipes);
        shoppingList.addToList(longList);
        return shoppingList.toString();
    }
    
    public boolean saveToFile(String shoppingList, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(shoppingList);
            writer.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    } 
       
}
