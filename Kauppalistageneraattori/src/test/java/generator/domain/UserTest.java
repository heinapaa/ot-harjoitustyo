/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.domain;

import generator.models.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heinapaa
 */
public class UserTest {
    
    private User kayttaja;
    
    @Before
    public void setUp() {
        this.kayttaja = new User("testaaja");
    }
    
    @Test
    public void konstruktoriAsettaaNimenOikein() {
        assertEquals(kayttaja.getUsername(), "testaaja");
    }
    
    @Test
    public void equalKunSamaNimi() {
        User toinenKayttaja = new User("testaaja");
        assertTrue(kayttaja.equals(toinenKayttaja));
    }
    
    @Test
    public void eiEqualKunEriNimi() {
        User toinenKayttaja = new User("toinenTestaaja");
        assertFalse(kayttaja.equals(toinenKayttaja));
    }

}
