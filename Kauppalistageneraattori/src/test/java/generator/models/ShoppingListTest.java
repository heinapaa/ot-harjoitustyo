package generator.models;

import generator.dao.FakeIngredientDao;
import generator.dao.IngredientDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ShoppingListTest {
    
    private ShoppingList sls; 
    private IngredientDao ingredientDao;
    
    @Before
    public void setUp() { 
        this.ingredientDao = new FakeIngredientDao();
        this.sls = new ShoppingList();
    }

    @Test
    public void listPrintedCorrectly1() {
        List<Ingredient> testList = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("w", 100, "kpl", null);
        Ingredient ingredient2 = new Ingredient("w", 100, "l", null);  
        Ingredient ingredient3 = new Ingredient("w", 100, "kg", null);         
        testList.add(ingredient1);
        testList.add(ingredient2);  
        testList.add(ingredient3);            
        sls.addToList(testList);
        String s = "w, 100.0 kg\n" +
                "w, 100.0 kpl\n" +
                "w, 100.0 l\n";
        assertEquals(s, sls.toString());
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
        sls.addToList(testList);
        String s = "a, 100.0 kg\n" +
                "b, 100.0 kg\n" +
                "c, 100.0 kg\n";
        assertEquals(s, sls.toString());
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
        sls.addToList(testList);       
        String s = "a, 200.0 kpl\n" +
                "v, 110.0 l\n" +
                "w, 100.1 kg\n";
        assertEquals(s, sls.toString());
    }    
    
}
