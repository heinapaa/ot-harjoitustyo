package generator.domain;

import generator.dao.IngredientDao;

public class Ingredient {
    
    private String name;
    private double amount;
    private String unit;
    
    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
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
    
}
