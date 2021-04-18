/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heinapaa
 */
public class UserTest {
    
    @Test
    public void konstruktoriAsettaaNimenOikein() {
        User kayttaja = new User("testaaja");
        assertEquals(kayttaja.getUsername(), "testaaja");
    }
    
    @Test
    public void equalKunSamaNimi() {
        User ensimmainen = new User("testaaja");
        User toinen = new User("testaaja");
        assertTrue(ensimmainen.equals(toinen));
    }
    
    @Test
    public void eiEqualKunEriNimi() {
        User ensimmainen = new User("testaaja");
        User toinen = new User("toinenTestaaja");
        assertFalse(ensimmainen.equals(toinen));
    }
        
    
}
