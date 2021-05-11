package generator.models;

import generator.models.Unit;
import generator.models.Ingredient;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class UnitTest {
    
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Ingredient ingredient3;
    private Ingredient ingredient4;
    private Ingredient ingredient5;    
    
    @Before
    public void setUp() {
        this.ingredient1 = new Ingredient("ingredient1", 1, "kpl", null);
        this.ingredient2 = new Ingredient("ingredient2", 1, "kg", null);
        this.ingredient3 = new Ingredient("ingredient3", 1, "g", null);
        this.ingredient4 = new Ingredient("ingredient4", 1, "l", null);
        this.ingredient5 = new Ingredient("ingredient5", 1, "dl", null);           
    }

    @Test
    public void unitIsSetCorrectly() {       
        assertEquals(Unit.KPL, ingredient1.getUnit());
        assertEquals(Unit.KG, ingredient2.getUnit());
        assertEquals(Unit.G, ingredient3.getUnit());
        assertEquals(Unit.L, ingredient4.getUnit());
        assertEquals(Unit.DL, ingredient5.getUnit());        
    }    
    
    @Test
    public void weightIsWeight() {
        assertTrue(ingredient2.getUnit().isWeight());
        assertTrue(ingredient3.getUnit().isWeight());        
    }
    
    @Test
    public void volumeIsVolume() {
        assertTrue(ingredient4.getUnit().isVolume());
        assertTrue(ingredient5.getUnit().isVolume());        
    }  
    
    @Test
    public void amountIsAmount() {
        assertTrue(ingredient1.getUnit().isAmount());       
    }  
    
    @Test
    public void noFalseWeights() {
        assertFalse(ingredient1.getUnit().isWeight());
        assertFalse(ingredient4.getUnit().isWeight());
        assertFalse(ingredient5.getUnit().isWeight());        
    }
    
    @Test
    public void noFalseVolumes() {
        assertFalse(ingredient1.getUnit().isVolume());
        assertFalse(ingredient2.getUnit().isVolume());
        assertFalse(ingredient3.getUnit().isVolume());        
    }    
    
    @Test
    public void noFalseAmounts() {
        assertFalse(ingredient2.getUnit().isAmount());
        assertFalse(ingredient3.getUnit().isAmount());        
        assertFalse(ingredient4.getUnit().isAmount());
        assertFalse(ingredient5.getUnit().isAmount());        
    }    
    
    @Test
    public void sameUnitTypesAreSame() {
        assertTrue(ingredient2.getUnit().hasSameType(ingredient3.getUnit()));
        assertTrue(ingredient4.getUnit().hasSameType(ingredient5.getUnit()));        
    }
    
    @Test
    public void differentUnitTypesAreDifferent() {
        assertFalse(ingredient2.getUnit().hasSameType(ingredient4.getUnit()));
        assertFalse(ingredient3.getUnit().hasSameType(ingredient5.getUnit()));          
    }
    
}
