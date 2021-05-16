package generator.models;

/**
 * Ainesosan yksikköä kuvaava enum-tyyppinen luokka
 * 
 */

public enum Unit {
    KG("WEIGHT"),
    G("WEIGHT"),
    L("VOLUME"),
    DL("VOLUME"),
    KPL("PCS");

    String unitType;
    
    /**
     * Konstruktori
     * @param unitType yksikköä vastaava merkkijono ("kg", "g", "l", "dl" tai "kpl")
     */

    Unit(String unitType) {
        this.unitType = unitType;
    }
    
    /**
     * Palauttaa tiedon siitä, onko yksikkö paino
     * @return true jos yksikkö on paino, muuten false
     */

    public boolean isWeight() {
        return unitType.equals("WEIGHT");
    }
    
    /**
     * Palauttaa tiedon siitä, onko yksikkö tilavuus
     * @return true jos yksikkö on tilavuus, muuten false
     */    

    public boolean isVolume() {
        return unitType.equals("VOLUME");
    }
    
    /**
     * Palauttaa tiedon siitä, onko yksikkö lukumäärä
     * @return true jos yksikkö on lukumäärä, muuten false
     */    

    public boolean isAmount() {
        return unitType.equals("PCS");
    }
    
    /**
     * Vertaa yksikköä toiseen yksikköön ja palauttaa tiedon siitä, ovatko ne saman tyyppisiä (paino/tilavuus/kpl).
     * @param otherUnit se toinen yksikkö, johon vertailu halutaan suorittaa
     * @return true jos yksiköt ovat saman tyyppisiä, muuten false
     */    

    public boolean hasSameType(Unit otherUnit) {
        if (this.isAmount() && otherUnit.isAmount()) {
            return true;
        } else if (this.isWeight() && otherUnit.isWeight()) {
            return true;
        } else if (this.isVolume() && otherUnit.isVolume()) {
            return true;
        }
        return false;
    }
    
    /**
     * Palauttaa yksikön String-muotoisena esityksenä
     * @return String-merkkijono, joka vastaa yksikön nimeä pienelllä kirjoitettuna ("kg", "g", "l", "dl" tai "kpl")
     */

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }  
    
}
