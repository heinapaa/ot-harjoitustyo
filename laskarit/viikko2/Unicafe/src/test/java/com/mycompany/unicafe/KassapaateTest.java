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
    public void kassanSummaAlussaOikein() {
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void myydytLounaatAlussaOikein() {
        assertEquals(0, paate.maukkaitaLounaitaMyyty() + paate.edullisiaLounaitaMyyty());
    } 
    
    @Test
    public void lataaRahaaKassaKasvaa() {
        paate.lataaRahaaKortille(new Maksukortti(0), 100);
        assertEquals(100100, paate.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaaNegatiivinenEiToimi() {
        paate.lataaRahaaKortille(new Maksukortti(0), -100);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    // EDULLINEN - RAHA RIITTAA
    
    @Test
    public void syoEdullisestiRahaRiittaaVaihtoraha() {
        assertEquals(100, paate.syoEdullisesti(340));
    }
    
    @Test
    public void syoEdullisestiRahaRiitaaMyyntiKasvaa() {
        paate.syoEdullisesti(340);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiRahaRiittaaKassaKasvaa() {
        paate.syoEdullisesti(340);
        assertEquals(100240,paate.kassassaRahaa());
    }
    
    // EDULLINEN - RAHA EI RIITTA
    
    @Test
    public void syoEdullisestiRahaEiRiitaVaihtoraha() {
        assertEquals(1, paate.syoEdullisesti(1));
    }
    
    @Test
    public void syoEdullisestiRahaEiRiitaMyyntiEiKasva() {
        paate.syoEdullisesti(1);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }
      
    @Test
    public void syoEdullisestiRahaEiRiitaKassaEiKasva() {
        paate.syoEdullisesti(1);
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    // MAUKAS - RAHA RIITTAA    
    
    @Test
    public void syoMaukkaastiRahaRiittaaVaihtoraha() {
        assertEquals(100, paate.syoMaukkaasti(500));
    }
    
    @Test
    public void syoMaukkaastiRahaRiitaaMyyntiKasvaa() {
        paate.syoMaukkaasti(500);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiRahaRiittaaKassaKasvaa() {
        paate.syoMaukkaasti(500);
        assertEquals(100400,paate.kassassaRahaa());
    }
     
    // MAUKAS - RAHA EI RIITA  

    @Test
    public void syoMaukkaastiRahaEiRiitaVaihtoraha() {
        assertEquals(1, paate.syoMaukkaasti(1));
    }
    
    @Test
    public void syoMaukkaastiRahaEiRiitaMyyntiEiKasva() {
        paate.syoMaukkaasti(1);
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiRahaEiRiittaKassaEiKasva() {
        paate.syoMaukkaasti(1);
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    // EDULLINEN - KORTTI - RAHA RIITTAA
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaa() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(paate.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaaSaldoMuuttuu() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoEdullisesti(kortti);
        assertEquals(760, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaaMyyntiKasvaa() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoEdullisesti(kortti);
        assertEquals(1, paate.edullisiaLounaitaMyyty());        
    }
    
    @Test
    public void syoEdullisestiKortillaRahaRiittaaKassaEiKasva() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoEdullisesti(kortti);
        assertEquals(100000, paate.kassassaRahaa());        
    }
    
    
    // EDULLINEN - KORTTI - RAHA EI RIITA
    
    @Test
    public void syoEdullisestiKortillaRahaEiRiita() {
        Maksukortti kortti = new Maksukortti(100);
        assertTrue(!paate.syoEdullisesti(kortti));
    }

    @Test
    public void syoEdullisestiKortillaRahaEiRiitaSaldoEiMuutu() {
        Maksukortti kortti = new Maksukortti(100);
        paate.syoEdullisesti(kortti);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiKortillaRahaEiRiittaMyyntiEiKasvaa() {
        Maksukortti kortti = new Maksukortti(1);
        paate.syoEdullisesti(kortti);
        assertEquals(0, paate.edullisiaLounaitaMyyty());        
    }
    
    @Test
    public void syoEdullisestiKortillaRahaEiRiitaKassaEiKasva() {
        Maksukortti kortti = new Maksukortti(1);
        paate.syoEdullisesti(kortti);
        assertEquals(100000, paate.kassassaRahaa());        
    }    
    
    // MAUKAS - KORTTI - RAHA RIITTAA
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaa() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaaSaldoMuuttuu() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoMaukkaasti(kortti);
        assertEquals(600, kortti.saldo());
    }  
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaaMyyntiKasvaa() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoMaukkaasti(kortti);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());        
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaRiittaaKassaEiKasva() {
        Maksukortti kortti = new Maksukortti(1000);
        paate.syoMaukkaasti(kortti);
        assertEquals(100000, paate.kassassaRahaa());        
    }    
    
    // MAUKAS - KORTTI - RAHA EI RIITA    
    
    @Test
    public void syoMaukkaastiKortillaRahaEiRiita() {
        Maksukortti kortti = new Maksukortti(100);
        assertTrue(!paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaEiRiitaSaldoEiMuutu() {
        Maksukortti kortti = new Maksukortti(100);
        paate.syoMaukkaasti(kortti);
        assertEquals(100, kortti.saldo());
    }     

    @Test
    public void syoMaukkaastiKortillaRahaEiRiittaMyyntiEiKasvaa() {
        Maksukortti kortti = new Maksukortti(1);
        paate.syoMaukkaasti(kortti);
        assertEquals(0, paate.maukkaitaLounaitaMyyty());        
    }
    
    @Test
    public void syoMaukkaastiKortillaRahaEiRiitaKassaEiKasva() {
        Maksukortti kortti = new Maksukortti(1);
        paate.syoMaukkaasti(kortti);
        assertEquals(100000, paate.kassassaRahaa());        
    }     
    
}
