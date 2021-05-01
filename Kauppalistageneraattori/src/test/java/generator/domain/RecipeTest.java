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
        this.resepti = new Recipe("TestiResepti", 2, null, kayttaja);
    }
    
    @Test
    public void konstruktoriAsettaaNimenOikein() {
        assertEquals(resepti.getName(), "TestiResepti");
    }
    
    @Test
    public void konstruktoriAsettaaAnnoskoonOikein() {
        assertEquals(resepti.getPortion(), 2);
    }
    
    @Test
    public void eiEqualKunEriNimi() {
        Recipe toinenResepti = new Recipe("ToinenResepti", 2, "kasvis", kayttaja);
        assertFalse(resepti.equals(toinenResepti));
    }
    
    @Test
    public void eiEqualKunEriOmistaja() {
        User toinenKayttaja = new User("toinen");
        Recipe toinenResepti = new Recipe("TestiResepti", 2, "kasvis", toinenKayttaja);
        assertFalse(resepti.equals(toinenResepti));    
    }
    
    @Test
    public void eiEqualKunEriId() {
        resepti.setId(1);
        Recipe toinenResepti = new Recipe(2, "TestiResepti", 2, "kasvis", kayttaja);
        assertFalse(resepti.equals(toinenResepti));
    }
    
}
