package generator.domain;

/**
 * Yksittäistä ainesosaa vastaava luokka. 
 */

public class Ingredient implements Comparable<Ingredient> {
    
    private int id;
    private String name;
    private double amount;
    private String unit;
    private Recipe recipe;
    
    /**
     * Konstruktori, kun ainesosan tunniste ja resepti ovat tiedossa. 
     * @param   id      Ohjelman luoma tunniste
     * @param   name    Käyttäjän antama nimi
     * @param   amount  Käyttäjän antama määrä
     * @param   unit    Käyttäjän antama yksikkö
     * @param   recipe  Resepti, johon ainesosa liittyy
     */    
    
    public Ingredient(int id, String name, double amount, String unit, Recipe recipe) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
    }    

    /**
     * Konstruktori, kun ainesosan id ja resepti eivät ole tiedossa. 
     * @param   name    Käyttäjän antama nimi
     * @param   amount  Käyttäjän antama määrä
     * @param   unit    Käyttäjän antama yksikkö
     */     
    
    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }     
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
    
    public Recipe getRecipe() {
        return recipe;
    }
    
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    
    /**
     * Metodi muodostaa ja palauttaa merkkijonon muotoa "(ainesosan nimi), (määrä) (yksikkö)", esim. "porkkana, 1 kg".
     * @return ainesosan ominaisuuksien perusteella muodostetu merkkijono muotoa "(ainesosan nimi), (määrä) (yksikkö)"
     */

    @Override
    public String toString() {
        return name + ", " + amount + " " + unit;
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
            if (ainesosa.getId() != this.id
                    || !ainesosa.getName().equals(this.name)
                    || ainesosa.getAmount() != this.amount
                    || !ainesosa.getUnit().equals(this.unit)
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
