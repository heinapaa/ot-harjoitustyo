package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShoppingListServiceTest {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;
    private ShoppingListService sls;    
    
    
    @Before
    public void setUp() {   
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();    
        this.sls = new ShoppingListService(userDao, recipeDao, ingredientDao);
        
        Recipe r1 = new Recipe(1, "r1", 1, null);
        recipeDao.create(r1);
        Recipe r2 = new Recipe(2, "r2", 10, null);
        recipeDao.create(r2);
        
        Ingredient w1 = new Ingredient(1, "w", 100, "kg", r1);
        ingredientDao.create(w1);
        Ingredient w2 = new Ingredient(2, "w", 100, "g", r2);
        ingredientDao.create(w2);
        Ingredient v1 = new Ingredient(3, "v", 100, "l", r1);
        ingredientDao.create(v1);
        Ingredient v2 = new Ingredient(4, "v", 100, "dl", r2); 
        ingredientDao.create(v2);
        Ingredient a1 = new Ingredient(5, "a", 100, "kpl", r1);
        ingredientDao.create(a1);
        Ingredient a2 = new Ingredient(6, "a", 100, "kpl", r2);     
        ingredientDao.create(a2);
    }
    
    @Test
    public void weightUnitsRecognised() {
        assertTrue(sls.isWeight(ingredientDao.findById(1)));   
        assertTrue(sls.isWeight(ingredientDao.findById(2)));       
    }
    
    @Test
    public void onlyWeightUnitsRecognisedAsWeights() {
        assertFalse(sls.isWeight(ingredientDao.findById(3)));    
        assertFalse(sls.isWeight(ingredientDao.findById(4))); 
        assertFalse(sls.isWeight(ingredientDao.findById(5)));      
    }
    
    @Test
    public void volumeUnitsRecognised() {
        assertTrue(sls.isVolume(ingredientDao.findById(3)));   
        assertTrue(sls.isVolume(ingredientDao.findById(4))); 
    }
    
    @Test
    public void onlyVolumeUnitsRecognisedAsVolumes() {
        assertFalse(sls.isVolume(ingredientDao.findById(1)));    
        assertFalse(sls.isVolume(ingredientDao.findById(2))); 
        assertFalse(sls.isVolume(ingredientDao.findById(5))); 
    }    

    @Test
    public void kgConvertedCorrectly() {
        assertEquals(100, sls.convertWeight(ingredientDao.findById(1)),0);
    }
    
    @Test
    public void gConvertedCorrectly() {
        assertEquals(0.1, sls.convertWeight(ingredientDao.findById(2)),0);
    }
    
    @Test
    public void lConvertedCorrectly() {
        assertEquals(100, sls.convertVolume(ingredientDao.findById(3)),0);
    }
    
    @Test
    public void dlConvertedCorrectly() {
        assertEquals(10, sls.convertVolume(ingredientDao.findById(4)), 0);
    }
    
    @Test
    public void weightsSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(1));
        testList.add(ingredientDao.findById(2));
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(100.1, summedList.get("w_WEIGHT"), 0);
    }
    
    @Test
    public void volumesSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(3));
        testList.add(ingredientDao.findById(4));
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(110, summedList.get("v_VOLUME"), 0);
    }    
    
    @Test
    public void amountsSummedCorrectly() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(5));
        testList.add(ingredientDao.findById(6));
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(200, summedList.get("a_PCS"), 0);
    }
    
    @Test
    public void differentUnitsNotSummed() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(1));
        testList.add(new Ingredient(7, "w", 100, "kpl", null));
        testList.add(new Ingredient(8, "w", 100, "l", null));        
        Map<String, Double> summedList = sls.sumIngredients(testList);
        assertEquals(100, summedList.get("w_WEIGHT"), 0);      
        assertEquals(100, summedList.get("w_VOLUME"), 0);
        assertEquals(100, summedList.get("w_PCS"), 0);        
    }
    
    @Test
    public void listPrintedCorrectly1() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(1));
        testList.add(new Ingredient(7, "w", 100, "kpl", null));
        testList.add(new Ingredient(8, "w", 100, "l", null));        
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        
        differentUnitsNotSummed();
        String s = "Ostoslista:\n***********\n" +
                "w, 100.0 kpl\n" +
                "w, 100.0 l\n" +
                "w, 100.0 kg\n";
        
        assertEquals(s, sls.printShoppingList(summedList));
    }
    
    @Test
    public void listPrintedCorrectly2() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(1));
        testList.add(new Ingredient(7, "b", 100, "kg", null));
        testList.add(new Ingredient(8, "c", 100, "kg", null));        
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        
        differentUnitsNotSummed();
        String s = "Ostoslista:\n***********\n" +
                "b, 100.0 kg\n" +
                "c, 100.0 kg\n" +
                "w, 100.0 kg\n";
        
        assertEquals(s, sls.printShoppingList(summedList));
    }   
    
    @Test
    public void listPrintedCorrectly3() {
        List<Ingredient> testList = new ArrayList<>();
        testList.add(ingredientDao.findById(1));
        testList.add(ingredientDao.findById(2));        
        testList.add(ingredientDao.findById(3));
        testList.add(ingredientDao.findById(4));  
        testList.add(ingredientDao.findById(5));
        testList.add(ingredientDao.findById(6));          
        Map<String, Double> summedList = sls.sumIngredients(testList);        
        
        differentUnitsNotSummed();
        String s = "Ostoslista:\n***********\n" +
                "a, 200.0 kpl\n" +
                "v, 110.0 l\n" +
                "w, 100.1 kg\n";
        
        assertEquals(s, sls.printShoppingList(summedList));
    }      
}
