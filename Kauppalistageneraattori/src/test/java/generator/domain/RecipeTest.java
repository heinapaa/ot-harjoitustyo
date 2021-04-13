package generator.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeTest {
    
    User kayttaja;
    Recipe resepti;
    
    @Before
    public void setUp() {
        this.kayttaja = new User("testaaja");
        this.resepti = new Recipe("TestiResepti", 2, kayttaja);
    }
    
    @Test
    public void konstruktoriAsettaaNimenOikein() {
        assertEquals(resepti.getName(), "TestiResepti");
    }
    
    @Test
    public void konstruktoriAsettaaAnnoskoonOikein() {
        assertEquals(resepti.getServing(), 2);
    }
    
}
