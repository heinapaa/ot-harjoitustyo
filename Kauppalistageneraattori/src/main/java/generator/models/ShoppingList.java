package generator.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingList {
    
    private Map<String, Double> weightList;
    private Map<String, Double> volumeList;
    private Map<String, Double> pcsList;
    
    public ShoppingList() {
        this.weightList = new HashMap<>();
        this.volumeList = new HashMap<>();
        this.pcsList = new HashMap<>();        
    }
    
    public void addToList(List<Ingredient> ingredients) {     
        for (Ingredient ingredient : ingredients) {
            String name = ingredient.getName();
            if (ingredient.getUnit().isWeight()) {
                addToWeightList(ingredient);
            } else if (ingredient.getUnit().isVolume()) {              
                addToVolumeList(ingredient);
            } else if (ingredient.getUnit().isAmount()) {
                addToPcsList(ingredient);
            }
        }       
    }
       
    @Override
    public String toString() {
        StringBuilder shoppingList = new StringBuilder();
        List<String> lines = new ArrayList<>();
        weightList.keySet().stream()
                .forEach(s -> lines.add(s + ", " + weightList.get(s) + " kg"));
        volumeList.keySet().stream()
                .forEach(s -> lines.add(s + ", " + volumeList.get(s) + " l"));
        pcsList.keySet().stream()
                .forEach(s -> lines.add(s + ", " + pcsList.get(s) + " kpl"));        
        Collections.sort(lines);
        for (String line : lines) {
            shoppingList.append(line + "\n");
        }
        return shoppingList.toString();        
    }    
    
    private void addToWeightList(Ingredient ingredient){
        weightList.putIfAbsent(ingredient.getName(), 0.0);
        weightList.put(ingredient.getName(), weightList.get(ingredient.getName()) + convertWeight(ingredient));        
    }
    
    private void addToVolumeList(Ingredient ingredient){
        volumeList.putIfAbsent(ingredient.getName(), 0.0);
        volumeList.put(ingredient.getName(), volumeList.get(ingredient.getName()) + convertVolume(ingredient));        
    }

    private void addToPcsList(Ingredient ingredient){
        pcsList.putIfAbsent(ingredient.getName(), 0.0);
        pcsList.put(ingredient.getName(), pcsList.get(ingredient.getName()) + ingredient.getAmount());        
    }   
    
    private double convertWeight(Ingredient ingredient) {
        if (ingredient.getUnit() == Unit.G) {
            return ingredient.getAmount() / 1000;
        } else {
            return ingredient.getAmount();
        }
    }
    
    private double convertVolume(Ingredient ingredient) {
        if (ingredient.getUnit() == Unit.DL) {
            return ingredient.getAmount() / 10;
        } else {
            return ingredient.getAmount();
        }
    }      
}
