package generator.ui;

import generator.domain.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RecipeListView {

    private ComboBox recipeTypeComboBox;
    
    private HBox recipeNameRow;
    private HBox recipePortionRow;
    
    private Label infoRecipeName;
    private Label infoRecipePortion;     
    private Label infoRecipeType;    
    private Label labelRecipeName;
    private Label labelRecipePortion;
    private Label labelRecipeType;    
    private Label recipeTitle;    
    private Label errorLabel;
   
    private String inputRecipeName;
    private String inputRecipePortion;    
    
    private TextField inputFieldRecipeName;
    private TextField inputFieldRecipePortion;
    
    private VBox recipeButtons;
    private HBox recipeTypeRow;
    
    public BorderPane set(Button addRecipe, Button newShoppingList, Button changeUser, Button commitAddRecipe, ListView recipeList,
            ListView ingredientList) {
        
        // Left

        this.recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));           
        
        this.labelRecipeName = new Label("Nimi:");
        this.infoRecipeName = new Label(); 
        this.inputFieldRecipeName = new TextField();
        
        inputFieldRecipeName.setOnKeyReleased(event -> {
            inputRecipeName = inputFieldRecipeName.getText();
            errorLabel.setText("inputFieldRecipeName = " + inputRecipeName);
        });
        
        this.labelRecipePortion = new Label("Annoskoko:");
        this.infoRecipePortion = new Label();    
        this.inputFieldRecipePortion = new TextField();    
        
        inputFieldRecipePortion.setOnKeyReleased(event -> {
            inputRecipePortion = inputFieldRecipePortion.getText();
            errorLabel.setText("inputFieldRecipePortion = " + inputRecipePortion);            
        });        
        
        this.labelRecipeType = new Label("Tyyppi:");
        this.infoRecipeType = new Label();
        this.recipeTypeComboBox = new ComboBox();
        recipeTypeComboBox.getItems().addAll(
            "kala",
            "kasvis",
            "liha",
            "makea"
        );          
        
        this.recipeNameRow = new HBox(labelRecipeName, infoRecipeName);
        recipeNameRow.setSpacing(20);     
        
        this.recipePortionRow = new HBox(labelRecipePortion, infoRecipePortion);
        recipePortionRow.setSpacing(20);    
        
        this.recipeTypeRow = new HBox(labelRecipeType, infoRecipeType);
        recipeTypeRow.setSpacing(20);
                                   
        this.recipeButtons = new VBox();    
        recipeButtons.setSpacing(20);   
        
        VBox infoBox = new VBox(recipeTitle, recipeNameRow, recipePortionRow, recipeTypeRow, recipeButtons);
        infoBox.setSpacing(20);   
        infoBox.setMinWidth(300);
        
        // Center  
        SplitPane splitPane = new SplitPane(recipeList, ingredientList);       
        
        // Top
        HBox topButtons = new HBox(addRecipe, newShoppingList, changeUser);
        topButtons.setSpacing(10);
        
        // Bottom
        this.errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);  

        BorderPane pane = new BorderPane();
        pane.setTop(topButtons);
        pane.setLeft(infoBox);
        pane.setCenter(splitPane);
        pane.setBottom(errorLabel);
        return pane;        
    }
    
    public void setDefaultDisplay() {
        setInfoRows();
        errorLabel.setText("");        
        infoRecipeName.setText("");
        infoRecipePortion.setText("");
        infoRecipeType.setText("");
        recipeButtons.getChildren().clear();      
    }
    
    public void setInfoDisplay(Recipe recipe, Button editRecipe) {
        setInfoRows();
        errorLabel.setText("");        
        infoRecipeName.setText(recipe.getName());
        infoRecipePortion.setText(String.valueOf(recipe.getPortion()));
        infoRecipeType.setText(recipe.getType());
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().add(editRecipe);
    }
    
    public void setAddDisplay(Button commitAddRecipe, Button cancelAddRecipe) {
        setInputRows();
        errorLabel.setText("");        
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().addAll(commitAddRecipe, cancelAddRecipe);
    }
    
    public void setEditDisplay(Recipe recipe, Button commitEditRecipe, Button cancelEditRecipe, Button addIngredient, Button deleteIngredient, 
            Button deleteRecipe) {
        setInputRows();
        errorLabel.setText("");
        inputFieldRecipeName.setText(recipe.getName());
        inputRecipeName = recipe.getName();        
        inputFieldRecipePortion.setText(String.valueOf(recipe.getPortion()));
        inputRecipePortion = String.valueOf(recipe.getPortion());
        recipeTypeComboBox.getSelectionModel().select(recipe.getType());
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().addAll(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
    }    
    
    public void setInputRows() {
        recipeNameRow.getChildren().clear();
        recipeNameRow.getChildren().add(labelRecipeName);
        recipeNameRow.getChildren().add(inputFieldRecipeName);        
        recipePortionRow.getChildren().clear();
        recipePortionRow.getChildren().add(labelRecipePortion); 
        recipePortionRow.getChildren().add(inputFieldRecipePortion);      
        recipeTypeRow.getChildren().clear();
        recipeTypeRow.getChildren().add(labelRecipeType);
        recipeTypeRow.getChildren().add(recipeTypeComboBox);
    }    
    
    public void setInfoRows() {
        recipeNameRow.getChildren().clear();
        recipeNameRow.getChildren().add(labelRecipeName);
        recipeNameRow.getChildren().add(infoRecipeName);        
        recipePortionRow.getChildren().clear();
        recipePortionRow.getChildren().add(labelRecipePortion); 
        recipePortionRow.getChildren().add(infoRecipePortion);   
        recipeTypeRow.getChildren().clear();
        recipeTypeRow.getChildren().add(labelRecipeType);
        recipeTypeRow.getChildren().add(infoRecipeType);        
    }  

    public String getInputRecipeName() {
        return inputRecipeName;
    }

    public String getInputRecipePortion() {
        return inputRecipePortion;
    }    
    
    public String getInputRecipeType() {
        return recipeTypeComboBox.getValue().toString();
    }
    
    public void createRecipeFailure() {
        errorLabel.setText("Virhe! Reseptiä " + inputRecipeName + "ei voitu luoda.");
    }
    
    public void editRecipeFailure() {
        errorLabel.setText("Virhe! Reseptiä ei voida muokata.");
    } 
    
    public void commitEditRecipeFailure() {
        errorLabel.setText("Virhe! Muutosten tallennus epäonnistui.");
    }     
    
    public void deleteIngredientFailure() {
        errorLabel.setText("Virhe! Ainesosan poistaminen epäonnistui!");
    }    
    
    public void deleteRecipeFailure() {
        errorLabel.setText("Virhe! Reseptiä " + inputRecipeName + " ei voitu poistaa.");
    }        
}