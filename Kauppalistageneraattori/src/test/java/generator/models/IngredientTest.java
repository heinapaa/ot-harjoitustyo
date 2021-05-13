package generator.models;

import generator.models.User;
import generator.models.Recipe;
import generator.models.Ingredient;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {
    
    @Test
    public void equalWhenSame() {      
        Ingredient i1 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));
        Ingredient i2 = new Ingredient("aines", 1, "kpl", new Recipe("r1", 1, "kala", new User("u1")));  
        assertTrue(i1.equals(i2));           
    } 
    
    @Test
    public void notEqualWhenSameButDifferentRecipe() {
        User u1 = new User("u1");
        Recipe r1 = new Recipe("r1", 1, "kala", u1);
        Recipe r2 = new Recipe("r2", 6, "kasvis", u1);
        Ingredient i1 = new Ingredient("aines", 1, "kpl", r1);
        Ingredient i2 = new Ingredient("aines", 1, "kpl", r2);  
        assertFalse(i1.equals(i2));        
    }    
    
}
