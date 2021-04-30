package generator.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IngredientView {

    private TextField ingredientNameTextField;
    private TextField ingredientAmountTextField;
    private ComboBox ingredientUnitComboBox;
    private String inputIngredientName;
    private String inputIngredientAmount;
    private String inputIngredientUnit;
    
    public BorderPane set(Button commitAddIngredient, Button cancelAddIngredient) {  
        BorderPane pane = new BorderPane();
        
        VBox addIngredientLabels = new VBox(new Label("Ainesosan nimi:"), new Label("Määrä:"), new Label("Yksikkö:"));
        
        this.ingredientNameTextField = new TextField();
        this.ingredientAmountTextField = new TextField();
        this.ingredientUnitComboBox = new ComboBox();
        
        ingredientNameTextField.setOnKeyReleased(event -> {
            this.inputIngredientName = ingredientNameTextField.getText();
        }); 

        ingredientAmountTextField.setOnKeyReleased(event -> {
            this.inputIngredientAmount = ingredientAmountTextField.getText();
        });        
        
        ingredientUnitComboBox.getItems().addAll(
            "kg",
            "g",
            "l",
            "dl",
            "kpl"
        ); 
        
        VBox addIngredientInputs = new VBox(ingredientNameTextField, ingredientAmountTextField, ingredientUnitComboBox); 

        HBox addIngredientViewLines = new HBox(addIngredientLabels, addIngredientInputs);       
        HBox addIngredientViewButtons = new HBox(commitAddIngredient, cancelAddIngredient); 
        
        pane.setRight(addIngredientViewLines);
        pane.setBottom(addIngredientViewButtons);
         
        return pane;
    }

    public void clearView() {
        ingredientNameTextField.setText("");
        ingredientAmountTextField.setText("");
        ingredientUnitComboBox.setValue(null);
    }    
    
    public String getInputIngredientName() {
        return this.inputIngredientName;
    }

    public String getInputIngredientAmount() {
        return this.inputIngredientAmount;
    }

    String getInputIngredientUnit() {
        return ingredientUnitComboBox.getValue().toString();
    }

    void commitCreateIngredientFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}  