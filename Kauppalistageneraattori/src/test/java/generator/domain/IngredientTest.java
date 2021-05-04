package generator.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {
    
    @Test
    public void notEqualWhenSameButDifferentId() {
        User u1 = new User("u1");
        User u2 = new User("u2");
        Recipe r1 = new Recipe("r1", 1, "kala", u1);
        Ingredient i1 = new Ingredient(1, "aines", 1, "kpl", r1);
        Ingredient i2 = new Ingredient(2, "aines", 1, "kpl", r1);  
        assertFalse(i1.equals(i2));
    }
    
    @Test
    public void notEqualWhenSameButDifferentRecipe() {
        User u1 = new User("u1");
        User u2 = new User("u2");        
        Recipe r1 = new Recipe("r1", 1, "kala", u1);
        Recipe r2 = new Recipe("r2", 6, "kasvis", u2);
        Ingredient i1 = new Ingredient(1, "aines", 1, "kpl", r1);
        Ingredient i2 = new Ingredient(1, "aines", 1, "kpl", r2);  
        assertFalse(i1.equals(i2));        
    }    
    
}
