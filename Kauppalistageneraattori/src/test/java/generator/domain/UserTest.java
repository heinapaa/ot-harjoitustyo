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
    
    User kayttaja;
    
    @Before
    public void setUp() {
        this.kayttaja = new User("testaaja");
    }
    
    @Test
    public void konstruktoriAsettaaNimenOikein() {
        assertEquals(kayttaja.getUsername(), "testaaja");
    }
        
    
}
