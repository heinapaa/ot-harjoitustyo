package generator.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class IngredientView {

    private TextField ingredientNameTextField;
    private TextField ingredientAmountTextField;
    private ComboBox ingredientUnitComboBox;
    private String inputIngredientName;
    private String inputIngredientAmount;
    private String inputIngredientUnit;
    private Label errorLabel;
    
    public BorderPane set(Button commitAddIngredient, Button cancelAddIngredient) {  
        BorderPane pane = new BorderPane();
        
        this.ingredientNameTextField = new TextField();
        HBox rowIngredientName = new HBox(new Label("Ainesosan nimi:"), ingredientNameTextField);
        
        
        ingredientNameTextField.setOnKeyReleased(event -> {
            inputIngredientName = ingredientNameTextField.getText();
        });         
        
        this.ingredientAmountTextField = new TextField();
        HBox rowIngredientAmount = new HBox(new Label("Määrä:"), ingredientAmountTextField);

        ingredientAmountTextField.setOnKeyReleased(event -> {
            inputIngredientAmount = ingredientAmountTextField.getText();
        });  
        
        this.ingredientUnitComboBox = new ComboBox();
        ingredientUnitComboBox.getItems().addAll(
            "kg",
            "g",
            "l",
            "dl",
            "kpl"
        );         
        HBox rowIngredientUnit = new HBox(new Label("Yksikkö:"), ingredientUnitComboBox);      
        
        this.errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);        
        
        VBox addIngredientViewLines = new VBox(rowIngredientName, rowIngredientAmount, rowIngredientUnit, errorLabel);       
        HBox addIngredientViewButtons = new HBox(commitAddIngredient, cancelAddIngredient); 
        
        pane.setCenter(addIngredientViewLines);
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

    public String getInputIngredientUnit() {
        return ingredientUnitComboBox.getValue().toString();
    }

    public void commitCreateIngredientFailure() {
        errorLabel.setText("Virhe! Ainesosan " + inputIngredientName + " luonti ei onnistunut!");
    }
    
}  