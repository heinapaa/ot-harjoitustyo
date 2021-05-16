/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heinapaa
 */
public class UserTest {
    
    private User u1;
    
    @Before
    public void setUp() {
        this.u1 = new User("tester");
    }
    
    @Test
    public void constructorSetsNameCorrectly() {
        assertEquals(u1.getUsername(), "tester");
    }
    
    @Test
    public void equalWhenSameName() {
        User toinenKayttaja = new User("tester");
        assertTrue(u1.equals(toinenKayttaja));
    }
    
    @Test
    public void notEqualWhenDifferentName() {
        User u2 = new User("tester2");
        assertFalse(u1.equals(u2));
    }
    
    @Test
    public void notEqualWhenDifferentType() {
        String s = "tester";
        assertFalse(u1.equals(s));
    }

}
