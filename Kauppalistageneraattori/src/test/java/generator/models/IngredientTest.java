package generator.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {
    
    @Test
    public void equalWhenSame() {      
        Ingredient i1 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertEquals(i1, i2);           
    } 
    
    @Test
    public void notEqualWhenNotRecipe() {
        Ingredient i1 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertNotEquals(i1, null);
        assertNotEquals(i1, new User("tester"));
    }
    
    @Test
    public void notEqualWhenDifferentRecipe() {
        User u1 = new User("u1");
        Recipe r1 = new Recipe("r1", 1, "kala", u1);
        Recipe r2 = new Recipe("r2", 6, "kasvis", u1);
        Ingredient i1 = new Ingredient("aines", 1, "kpl", r1);
        Ingredient i2 = new Ingredient("aines", 1, "kpl", r2);  
        assertNotEquals(i1, i2);        
    }    
    
    @Test
    public void notEqualWhenDifferentUnit() {
        Ingredient i1 = new Ingredient("aines", 1, "kg", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertNotEquals(i1, i2);            
    }  
    
    @Test
    public void notEqualWhenDifferentAmount() {
        Ingredient i1 = new Ingredient("aines", 1, "kg", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 10, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertNotEquals(i1, i2);            
    }     
    
    @Test
    public void notEqualWhenDifferentName() {
        Ingredient i1 = new Ingredient("aines", 1, "kg", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines2", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertNotEquals(i1, i2);            
    }  
    
    @Test
    public void correctToString() {
        Ingredient i1 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));        
        assertEquals("aines, 1.0 kpl", i1.toString());
    }
    
    @Test
    public void likeIngredientsCanBeSummed1() {
        Ingredient i1 = new Ingredient("aines", 1, "kg", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "g", new Recipe("r1", 1, "kala", new User("u1")));  
        assertTrue(i1.canBeSummed(i2));
    }
    
    @Test
    public void likeIngredientsCanBeSummed2() {
        Ingredient i1 = new Ingredient("aines", 1, "l", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "dl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertTrue(i1.canBeSummed(i2));
    }    
    
    @Test
    public void likeIngredientsCanBeSummed3() {
        Ingredient i1 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 10, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertTrue(i1.canBeSummed(i2));
    }    
    
    @Test
    public void unlikeIngredientsCantBeSummed() {
        Ingredient i1 = new Ingredient("aines", 1, "kg", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i3 = new Ingredient("aines", 1, "l", new Recipe("r1", 1, "kala", new User("u1")));        
        assertFalse(i1.canBeSummed(i2));
        assertFalse(i1.canBeSummed(i3));
        assertFalse(i2.canBeSummed(i3));        
    }    
}
