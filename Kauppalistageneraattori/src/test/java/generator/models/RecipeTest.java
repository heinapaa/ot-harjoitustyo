package generator.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeTest {
    
    User u1;
    Recipe r1;
    
    @Before
    public void setUp() {
        this.u1 = new User("tester");
        this.r1 = new Recipe(1, "recipe1", 2, "kasvis", u1);
    }
    
    @Test
    public void constructorSetsNameCorrectly() {
        assertEquals(r1.getName(), "recipe1");
    }
    
    @Test
    public void constructorSetsPortionCorrectly() {
        assertEquals(r1.getPortion(), 2);
    }
    
    @Test
    public void notEqualWhenDifferentName() {
        Recipe r2 = new Recipe(1, "recipe2", 2, "kasvis", u1);
        assertFalse(r1.equals(r2));
    }
    
    @Test
    public void notEqualWhenDifferentUser() {
        User toinenKayttaja = new User("toinen");
        Recipe r2 = new Recipe(1, "recipe1", 2, "kasvis", toinenKayttaja);
        assertFalse(r1.equals(r2));    
    }
    
    @Test
    public void notEqualWhenDifferentType() {
        Recipe r2 = new Recipe(1, "recipe1", 2, "makea", u1);
        assertFalse(r1.equals(r2));           
    }
    
    @Test
    public void notEqualWhenDifferentId() {
        r1.setId(1);
        Recipe r2 = new Recipe(2, "recipe1", 2, "kasvis", u1);
        assertFalse(r1.equals(r2));
    }  
    
    @Test
    public void equalWhenEqual() {
        Recipe r2 = new Recipe(1, "recipe1", 2, "kasvis", u1);
        assertEquals(r1, r2);
    }
    
    @Test
    public void recipesAreComparedCorrectly() {
        Recipe r2 = new Recipe(2, "a", 1, "makea", u1);
        Recipe r3 = new Recipe(3, "y", 10, "kasvis", u1);
        Recipe r4 = new Recipe(4, "recipe1", 2, "kasvis", u1);
        assertTrue(r2.compareTo(r1)<0);
        assertTrue(r3.compareTo(r1)>0);
        assertTrue(r4.compareTo(r1)==0);
    }
    
}
