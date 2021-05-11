package generator.services;

import generator.services.ShoppingListService;
import generator.models.Recipe;
import generator.models.Ingredient;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShoppingListServiceTest {

    private ShoppingListService sls; 
    private IngredientDao ingredientDao;
    
    @Before
    public void setUp() { 
        this.ingredientDao = new FakeIngredientDao();
        this.sls = new ShoppingListService(ingredientDao);
    }
 
    @Test
    public void kgConvertedCorrectly() {
        assertEquals(100, sls.convertWeight(new Ingredient("w", 100, "kg", null)), 0);
    }
    
    @Test
    public void gConvertedCorrectly() {
        assertEquals(0.1, sls.convertWeight(new Ingredient("w", 100, "g", null)),0);
    }
    
    @Test
    public void lConvertedCorrectly() {
        assertEquals(100, sls.convertVolume(new Ingredient("v", 100, "l", null)),0);
    }
    
    @Test
    public void dlConvertedCorrectly() {
        assertEquals(10, sls.convertVolume(new Ingredient("v", 100, "dl", null)), 0);
    }
    
    @Test
    public void weightsSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("w", 100, "kg", null);
        Ingredient ingredient2 = new Ingredient("w", 100, "g", null);  
        testList.add(ingredient1);        
        testList.add(ingredient2); 
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(100.1, summedList.get("w_WEIGHT"), 0);
    }
    
    @Test
    public void volumesSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("v", 100, "l", null);
        Ingredient ingredient2 = new Ingredient("v", 100, "dl", null);  
        testList.add(ingredient1);        
        testList.add(ingredient2); 
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(110, summedList.get("v_VOLUME"), 0);
    }    
    
    @Test
    public void amountsSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("a", 100, "kpl", null);
        Ingredient ingredient2 = new Ingredient("a", 100, "kpl", null);  
        testList.add(ingredient1);        
        testList.add(ingredient2); 
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(200, summedList.get("a_PCS"), 0);
    }
    
    @Test
    public void differentUnitsNotSummed() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("w", 100, "kg", null);
        Ingredient ingredient2 = new Ingredient("w", 100, "l", null);  
        Ingredient ingredient3 = new Ingredient("w", 100, "kpl", null);  
        testList.add(ingredient1);        
        testList.add(ingredient2); 
        testList.add(ingredient3);         
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(100, summedList.get("w_WEIGHT"), 0);      
        assertEquals(100, summedList.get("w_VOLUME"), 0);
        assertEquals(100, summedList.get("w_PCS"), 0);        
    }
    
    /*
    @Test
    public void ingredientsCollectedCorrectly() {
        RecipeDao recipeDao = new FakeRecipeDao();
        Recipe r1 = new Recipe(1, "r1", 1, "kasvis", null);
        Recipe r2 = new Recipe(2, "r2", 2, "kala", null);
        Recipe r3 = new Recipe(3, "r3", 3, "makea", null);
        recipeDao.create(r1);
        recipeDao.create(r2);
        recipeDao.create(r3);
        Ingredient ingredient1 = new Ingredient("a", 100, "kpl", recipeDao.findById(1));
        Ingredient ingredient2 = new Ingredient("v", 100, "l", recipeDao.findById(2));  
        Ingredient ingredient3 = new Ingredient("w", 100, "kg", recipeDao.findById(3)); 
        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);
        ingredientDao.create(ingredient3);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipeDao.findById(1));
        recipeList.add(recipeDao.findById(2));
        recipeList.add(recipeDao.findById(3));
        List<Ingredient> testList = sls.getIngredientsForAllRecipes(recipeList);
        assertEquals(3, testList.size());
    }
    */
    
    @Test
    public void listPrintedCorrectly1() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("w", 100, "kpl", null);
        Ingredient ingredient2 = new Ingredient("w", 100, "l", null);  
        Ingredient ingredient3 = new Ingredient("w", 100, "kg", null);         
        testList.add(ingredient1);
        testList.add(ingredient2);  
        testList.add(ingredient3);            
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        String s = "w, 100.0 kpl\n" +
                "w, 100.0 l\n" +
                "w, 100.0 kg\n";
        assertEquals(s, sls.printShoppingList(summedList));
    }
    
    @Test
    public void listPrintedCorrectly2() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("a", 100, "kg", null);
        Ingredient ingredient2 = new Ingredient("b", 100, "kg", null);  
        Ingredient ingredient3 = new Ingredient("c", 100, "kg", null);         
        testList.add(ingredient1);
        testList.add(ingredient2);  
        testList.add(ingredient3);     
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        
        differentUnitsNotSummed();
        String s = "a, 100.0 kg\n" +
                "b, 100.0 kg\n" +
                "c, 100.0 kg\n";
        
        assertEquals(s, sls.printShoppingList(summedList));
    }   
    
    @Test
    public void listPrintedCorrectly3() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("w", 100, "kg", null);
        Ingredient ingredient2 = new Ingredient("w", 100, "g", null);  
        Ingredient ingredient3 = new Ingredient("v", 100, "l", null);   
        Ingredient ingredient4 = new Ingredient("v", 100, "dl", null);
        Ingredient ingredient5 = new Ingredient("a", 100, "kpl", null);  
        Ingredient ingredient6 = new Ingredient("a", 100, "kpl", null);         
        testList.add(ingredient1);
        testList.add(ingredient2);  
        testList.add(ingredient3);    
        testList.add(ingredient4);
        testList.add(ingredient5);  
        testList.add(ingredient6);          
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        String s = "a, 200.0 kpl\n" +
                "v, 110.0 l\n" +
                "w, 100.1 kg\n";
        assertEquals(s, sls.printShoppingList(summedList));
    }    
    
    /*
    @Test
    public void listCreatedCorrectly1() {
        RecipeDao recipeDao = new FakeRecipeDao();
        Recipe r1 = new Recipe(1, "r1", 1, "kasvis", null);
        Recipe r2 = new Recipe(2, "r2", 2, "kala", null);
        Recipe r3 = new Recipe(3, "r3", 3, "makea", null);
        recipeDao.create(r1);
        recipeDao.create(r2);
        recipeDao.create(r3);
        Ingredient ingredient1 = new Ingredient("a", 100, "kpl", recipeDao.findById(1));
        Ingredient ingredient2 = new Ingredient("v", 100, "l", recipeDao.findById(2));  
        Ingredient ingredient3 = new Ingredient("w", 100, "kg", recipeDao.findById(3)); 
        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);
        ingredientDao.create(ingredient3);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipeDao.findById(1));
        recipeList.add(recipeDao.findById(2));
        recipeList.add(recipeDao.findById(3));
        String shoppingList = sls.createShoppingList(recipeList);   
        String s = "a, 100.0 kpl\n" +
                "v, 100.0 l\n" +
                "w, 100.0 kg\n";
        assertEquals(s, shoppingList);
    }  
    */
    
    @Test
    public void listCreatedCorrectly2() {
        String shoppingList = sls.createShoppingList(null);   
        assertEquals("", shoppingList);
    }    
    
    @Test
    public void listCreatedCorrectly3() {
        List<Recipe> recipeList = new ArrayList<>();        
        String shoppingList = sls.createShoppingList(recipeList);   
        assertEquals("", shoppingList);
    }     
}
