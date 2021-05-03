package generator.ui;

import generator.domain.IngredientService;
import generator.domain.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class IngredientView implements View {

    private final Router router;
    private final IngredientService ingredientService;    
    
    private BorderPane pane;    
    private ComboBox ingredientUnitComboBox;
    private Label errorLabel;
    private TextField ingredientNameTextField;
    private TextField ingredientAmountTextField;
    
    private ObservableList<String> newIngredientsListItems;
    private ListView<String> newIngredientsList;
    
    public IngredientView(Router router, IngredientService ingredientService) {
        this.router = router;
        this.ingredientService = ingredientService;
    }
    
    @Override
    public Scene create() {
        this.ingredientNameTextField = new TextField();
        HBox rowIngredientName = new HBox(new Label("Ainesosan nimi:"), ingredientNameTextField);   
        rowIngredientName.setSpacing(20);
        
        this.ingredientAmountTextField = new TextField();
        HBox rowIngredientAmount = new HBox(new Label("Määrä:"), ingredientAmountTextField); 
        rowIngredientAmount.setSpacing(20);
        
        this.ingredientUnitComboBox = new ComboBox();
        ingredientUnitComboBox.getItems().addAll(
            "kg",
            "g",
            "l",
            "dl",
            "kpl"
        );         
        HBox rowIngredientUnit = new HBox(new Label("Yksikkö:"), ingredientUnitComboBox);   
        rowIngredientUnit.setSpacing(20);
        
        this.errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);        

        this.newIngredientsListItems = FXCollections.observableArrayList();
        this.newIngredientsList = new ListView<>(newIngredientsListItems);         
        
        VBox addIngredientViewLines = new VBox(rowIngredientName, rowIngredientAmount, rowIngredientUnit, errorLabel);     
        addIngredientViewLines.setSpacing(20);
        addIngredientViewLines.setPadding(new Insets(5, 5, 5, 5));
        
        this.pane = new BorderPane();
        pane.setTop(addIngredientViewLines);
        pane.setBottom(newIngredientsList);
        
        return new Scene(pane);     
    }   
    
    public void clearInputFields() {
        ingredientNameTextField.setText("");
        ingredientAmountTextField.setText("");
        ingredientUnitComboBox.getSelectionModel().select(null);
    }  
    
    public void addTo(Recipe recipe) {
        Button commitAddIngredient = new Button("Lisää ainesosa");
        commitAddIngredient.setOnMouseClicked(event -> {
            String ingredientName = ingredientNameTextField.getText();
            String ingredientAmount = ingredientAmountTextField.getText();
            if (!ingredientUnitComboBox.getSelectionModel().isEmpty() && !ingredientName.isBlank() && !ingredientAmount.isBlank()) {
                String ingredientUnit = ingredientUnitComboBox.getSelectionModel().getSelectedItem().toString();         
                if (ingredientService.addIngredient(recipe, ingredientName, ingredientUnit, ingredientAmount)) {
                    clearInputFields(); 
                    newIngredientsListItems.add(ingredientName + ", " + ingredientAmount + " " + ingredientUnit);
                } else {
                    errorLabel.setText("Virhe! Ainesosan tallennus epäonnistui!");
                }                
            } else {
                errorLabel.setText("Virhe! Varmista, että kaikki kentät on täytetty");
            }
        });          
        Button cancelAddIngredient = new Button("Sulje ikkuna");
        cancelAddIngredient.setOnMouseClicked(event -> router.closeIngredientWindow(recipe));
        HBox addIngredientViewButtons = new HBox(commitAddIngredient, cancelAddIngredient); 
        addIngredientViewButtons.setSpacing(20);
        addIngredientViewButtons.setPadding(new Insets(5, 5, 5, 5));
        pane.setCenter(addIngredientViewButtons);
    }
}  