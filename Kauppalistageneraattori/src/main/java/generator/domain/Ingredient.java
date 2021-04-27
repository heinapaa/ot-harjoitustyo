package generator.domain;

public class Ingredient implements Comparable<Ingredient> {
    
    private int id;
    private String name;
    private double amount;
    private String unit;
    private Recipe recipe;
    
    public Ingredient(int id, String name, double amount, String unit, Recipe recipe) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
    }    
    
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

    @Override
    public String toString() {
        return name + ", " + amount + " " + unit;
    }
    
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

    @Override
    public int compareTo(Ingredient otherIngredient) {
        return this.name.compareTo(otherIngredient.getName());
    }
    
    
  
}
