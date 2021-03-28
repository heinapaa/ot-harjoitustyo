package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void uudenKortinSaldoOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void lataaminenToimii() {
        kortti.lataaRahaa(1);
        assertEquals(11, kortti.saldo());
    }
    
    @Test
    public void saldoVaheneeKunRahaRiittaa() {
        kortti.otaRahaa(1);
        assertEquals(9, kortti.saldo());
    }
    
    @Test
    public void saldoEiVaheneKunRahaEiRiita() {
        kortti.otaRahaa(100);
        assertEquals(10, kortti.saldo());
    }
    

    @Test
    public void trueJosRahatRiittavat() {
        assertTrue(kortti.otaRahaa(1));
    }
    
    @Test
    public void falseJosRahatEivatRiita() {
        assertTrue(!kortti.otaRahaa(100));
    }
    
    @Test
    public void tulostusToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
