package generator.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {
    
    @Test
    public void eiEqualKunEriId() {
        Ingredient ainesosa1 = new Ingredient(1, null, 1, null, null);
        Ingredient ainesosa2 = new Ingredient(2, null, 1, null, null);  
        assertFalse(ainesosa1.equals(ainesosa2));
    }
    
    @Test
    public void eiEqualKunEriResepti() {
        Recipe resepti1 = new Recipe("Resepti1", 2, null);
        Recipe resepti2 = new Recipe("Resepti2", 6, null);
        Ingredient ainesosa1 = new Ingredient(1, null, 1, null, resepti1);
        Ingredient ainesosa2 = new Ingredient(2, null, 1, null, resepti2);  
        assertFalse(ainesosa1.equals(ainesosa2));        
    }    
    
}
