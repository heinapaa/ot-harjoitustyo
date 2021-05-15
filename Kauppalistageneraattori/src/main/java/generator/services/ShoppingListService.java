package generator.services;

import generator.models.Recipe;
import generator.models.Ingredient;
import generator.dao.IngredientDao;
import generator.models.ShoppingList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Kauppalistojen luomiseen liittyvästä sovelluslogiikasta vastaava luokka.
 */


public class ShoppingListService {
 
    private final IngredientDao ingredientDao;
    
    /**
     * Konstruktori
     * @param ingredientDao IngredientDao-rajapinnan toteuttava olio, joka vastaa ainesosien tallentamisesta
     */
    public ShoppingListService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    } 
    
    /**
     * Palauttaa Recipe-luokan olioita sisältävän List-rakenteen perusteella String-muotoisen kauppalistan, jossa reseptien sisältämät ainesosat on mahdollisuuksien mukaan summattu yhteen.
     * 
     * @param recipes   List-luokan olio, joka sisältää kauppalistalle valitut reseptit Recipe-luokan olioina
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
    
    /**
     * Tallentaa ostoslistan tekstitiedostoon
     * @param shoppingList  tallennettava ostoslista String-oliona
     * @param file tiedosto, johon ostoslista halutaan tallentaa
     * @return
     */
    public boolean saveToFile(String shoppingList, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(shoppingList);
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    } 
       
}
