package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate paate;
    
    @Before
    public void setUp() {
        paate = new Kassapaate();
    }
    
    @Test
    public void syoEdullisestiRahaRiittaaVaihtoraha() {
        assertEquals(100, paate.syoEdullisesti(340));
    }
    
    @Test
    public void syoEdullisestiRahaEiRiitaVaihtoraha() {
        assertEquals(1, paate.syoEdullisesti(1));
    }
    
    @Test
    public void syoEdullisestiRahaRiittaaKassaKasvaa() {
        paate.syoEdullisesti(340);
        assertEquals(100240,paate.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiRahaEiRiittaKassaEiKasva() {
        paate.syoEdullisesti(1);
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiRahaRiittaaVaihtoraha() {
        assertEquals(100, paate.syoMaukkaasti(500));
    }
    
    @Test
    public void syoMaukkaastiRahaEiRiitaVaihtoraha() {
        assertEquals(1, paate.syoMaukkaasti(1));
    }
    
    @Test
    public void syoMaukkaastiRahaRiittaaKassaKasvaa() {
        paate.syoMaukkaasti(500);
        assertEquals(100400,paate.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiRahaEiRiittaKassaEiKasva() {
        paate.syoMaukkaasti(1);
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiKortillaRahaEiRiita() {
        Maksukortti kortti = new Maksukortti(100);
        assertTrue(!paate.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaa() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(paate.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoEdullisestiKortillaRahaEiRiitaSaldoEiMuutu() {
        Maksukortti kortti = new Maksukortti(100);
        paate.syoEdullisesti(kortti);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaaSaldoMuuttuu() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoEdullisesti(kortti);
        assertEquals(760, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaEiRiita() {
        Maksukortti kortti = new Maksukortti(100);
        assertTrue(!paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaa() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaEiRiitaSaldoEiMuutu() {
        Maksukortti kortti = new Maksukortti(100);
        paate.syoMaukkaasti(kortti);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaaSaldoMuuttuu() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoMaukkaasti(kortti);
        assertEquals(600, kortti.saldo());
    }   
    
    @Test
    public void edullinenLaskuri() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoEdullisesti(kortti);
        paate.syoEdullisesti(240);
        assertEquals(2, paate.edullisiaLounaitaMyyty());
    }    
    
    @Test
    public void maukasLaskuri() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(400);
        assertEquals(2, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void lataaRahaaKassaKasvaa() {
        paate.lataaRahaaKortille(new Maksukortti(0), 100);
        assertEquals(100100, paate.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaaNegatiivinen() {
        paate.lataaRahaaKortille(new Maksukortti(0), -100);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
}
