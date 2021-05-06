package generator.domain;

/**
 * Yksittäistä ainesosaa vastaava luokka. 
 */

public class Ingredient implements Comparable<Ingredient> {
    
    private String name;
    private double amount;
    private Recipe recipe;
    private Unit unit;  
    
    /**
     * Konstruktori, kun ainesosan tunniste ja resepti ovat tiedossa. 
     * @param   name    Käyttäjän antama nimi
     * @param   amount  Käyttäjän antama määrä
     * @param   unit    Käyttäjän antama yksikkö
     * @param   recipe  Resepti, johon ainesosa liittyy
     */    
    
    public Ingredient(String name, double amount, String unit, Recipe recipe) {
        this.name = name;
        this.amount = amount;
        this.recipe = recipe;
        setUnit(unit);
    }    

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        if (unit.equals("kg")) {
            this.unit = Unit.KG;
        } else if (unit.equals("g")) {
            this.unit = Unit.G;
        } else if (unit.equals("l")) {
            this.unit = Unit.L;
        } else if (unit.equals("dl")) {
            this.unit = Unit.DL;
        } else if (unit.equals("kpl")) {
            this.unit = Unit.KPL;
        }
    }
    
    public Recipe getRecipe() {
        return recipe;
    }
    
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    
    /**
     * Metodi testaa, voidaanko ainesosa laskea yhteen toisen ainesosan kanssa.
     * @param ingredient    ainesosa, jonka kanssa valittu ainesosa halutaan summata
     * @see generator.domain.Unit#hasSameType(generator.domain.Unit) 
     * @return true jos ainesosat voidaan laskea yhteen, muuten false
     */
    
    public boolean canBeSummed(Ingredient ingredient) {
        return unit.hasSameType(ingredient.getUnit());
    }

    /**
     * Metodi muodostaa ja palauttaa merkkijonon muotoa "(ainesosan nimi), (määrä) (yksikkö)", esim. "porkkana, 1 kg".
     * @return ainesosan ominaisuuksien perusteella muodostetu merkkijono muotoa "(ainesosan nimi), (määrä) (yksikkö)"
     */
        
    @Override
    public String toString() {
        return name + ", " + amount + " " + unit;
    }
    
    
    public boolean nameEquals(String input) {
        return name.equals(input.strip());
    }    
    
    /**
     * Metodi tarkastaa, ovatko kaksi oliota sama ainesosa.
     * @param object    olio, johon ainesosaa halutaan verrata
     * @return true jos olio on sama kuin resepti, muuten false
     */

    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (getClass() != object.getClass()) {
            return false;
        } else {
            Ingredient ainesosa = (Ingredient) object;
            if (!ainesosa.getName().equals(this.name)
                    || ainesosa.getAmount() != this.amount
                    || ainesosa.getUnit() != this.unit
                    || !ainesosa.getRecipe().equals(this.recipe)) {
                return false;
            }  
        }
        return true;
    }   
    
    /**
     * Metodi vertaa kahta ainesosaa toisiinsa, perustaen vertauksen niiden nimiin.
     * @param otherIngredient   toinen ainesosa, johon ainesosaa halutaan verrata
     * @see String#compareTo(java.lang.String) 
     * @return 0 jos nimet ovat samat, muuten positiivinen tai negatiivinen luku riippuen siitä miten nimet vertautuvat toisiinsa 
     */

    @Override
    public int compareTo(Ingredient otherIngredient) {
        return this.name.compareTo(otherIngredient.getName());
    }
}
