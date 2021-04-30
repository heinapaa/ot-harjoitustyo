package generator.ui;

import generator.domain.Recipe;
import javafx.scene.control.Button;
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

    private Label errorLabel;
    private Label infoRecipeName;
    private TextField inputFieldRecipeName;
    private Label infoRecipePortion;
    private TextField inputFieldRecipePortion;
    private VBox recipeButtons;
    private Label recipeTitle;
    private HBox recipeNameRow;
    private HBox recipePortionRow;
    private Label labelRecipeName;
    private Label labelRecipePortion;
    private String inputRecipeName;
    private String inputRecipePortion;
    
    public BorderPane set(Button addRecipe, Button newShoppingList, Button changeUser, Button commitAddRecipe, ListView recipeList,
            ListView ingredientList) {
        
        // Left

        this.recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));           
        
        this.labelRecipeName = new Label("Nimi:");
        this.infoRecipeName = new Label(); 
        this.inputFieldRecipeName = new TextField();
        
        inputFieldRecipeName.setOnKeyReleased(event -> {
            this.inputRecipeName = inputFieldRecipeName.getText();
        });
        
        this.labelRecipePortion = new Label("Annoskoko:");
        this.infoRecipePortion = new Label();    
        this.inputFieldRecipePortion = new TextField();    
        
        inputFieldRecipePortion.setOnKeyReleased(event -> {
            this.inputRecipePortion = inputFieldRecipePortion.getText();
        });        
        
        this.recipeNameRow = new HBox(labelRecipeName, infoRecipeName);
        recipeNameRow.setSpacing(20);     
        
        this.recipePortionRow = new HBox(labelRecipePortion, infoRecipePortion);
        recipePortionRow.setSpacing(20);    
                                   
        this.recipeButtons = new VBox();    
        recipeButtons.setSpacing(20);   
        
        VBox infoBox = new VBox(recipeTitle, recipeNameRow, recipePortionRow, recipeButtons);
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
        pane.setBottom(this.errorLabel);
        return pane;        
    }
    
    public void setDefaultDisplay() {
        setInfoRows();
        this.errorLabel.setText("");        
        this.infoRecipeName.setText("");
        this.infoRecipePortion.setText("");
        this.recipeButtons.getChildren().clear();      
    }
    
    public void setInfoDisplay(Recipe recipe, Button editRecipe) {
        setInfoRows();
        this.errorLabel.setText("");        
        this.infoRecipeName.setText(recipe.getName());
        this.infoRecipePortion.setText(String.valueOf(recipe.getPortion()));
        this.recipeButtons.getChildren().clear();
        this.recipeButtons.getChildren().add(editRecipe);
    }
    
    public void setAddDisplay(Button commitAddRecipe, Button cancelAddRecipe) {
        setInputRows();
        this.errorLabel.setText("");        
        this.recipeButtons.getChildren().clear();
        this.recipeButtons.getChildren().addAll(commitAddRecipe, cancelAddRecipe);
    }
    
    public void setEditDisplay(Recipe recipe, Button commitEditRecipe, Button cancelEditRecipe, Button addIngredient, Button deleteIngredient, 
            Button deleteRecipe) {
        setInputRows();
        this.errorLabel.setText("");
        this.inputFieldRecipeName.setText(recipe.getName());
        this.inputFieldRecipePortion.setText(String.valueOf(recipe.getPortion()));
        this.recipeButtons.getChildren().clear();
        this.recipeButtons.getChildren().addAll(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
    }    
    
    public void setInputRows() {
        recipeNameRow.getChildren().clear();
        recipeNameRow.getChildren().add(labelRecipeName);
        recipeNameRow.getChildren().add(inputFieldRecipeName);        
        recipePortionRow.getChildren().clear();
        recipePortionRow.getChildren().add(labelRecipePortion); 
        recipePortionRow.getChildren().add(inputFieldRecipePortion);         
    }    
    
    public void setInfoRows() {
        recipeNameRow.getChildren().clear();
        recipeNameRow.getChildren().add(labelRecipeName);
        recipeNameRow.getChildren().add(infoRecipeName);        
        recipePortionRow.getChildren().clear();
        recipePortionRow.getChildren().add(labelRecipePortion); 
        recipePortionRow.getChildren().add(infoRecipePortion);    
    }  

    public String getInputRecipeName() {
        return this.inputRecipeName;
    }

    public String getInputRecipePortion() {
        return this.inputRecipePortion;
    }    
    
    public void createRecipeFailure() {
        this.errorLabel.setText("Virhe! Reseptiä " + this.inputRecipeName + "ei voitu luoda.");
    }
    
    public void editRecipeFailure() {
        this.errorLabel.setText("Virhe! Reseptiä ei voida muokata.");
    } 
    
    public void commitEditRecipeFailure() {
        this.errorLabel.setText("Virhe! Muutosten tallennus epäonnistui.");
    }     
    
    public void deleteIngredientFailure() {
        this.errorLabel.setText("Virhe! Ainesosan poistaminen epäonnistui!");
    }    
    
    public void deleteRecipeFailure() {
        this.errorLabel.setText("Virhe! Reseptiä " + this.inputRecipeName + " ei voitu poistaa.");
    }        
}