package generator.ui;

import generator.domain.Recipe;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ShoppingListView {
    
    private SplitPane shoppingListSplitPane;
    private HBox shoppingListButtons;
    private HBox shoppingListButtonsAlternative;
    private TextArea shoppingListBox;
    private ListView<Recipe> remainingRecipesList;
    private ListView<Recipe> chosenRecipesList;      
    private String chosenRecipe;
    private ComboBox recipeTypeComboBox;
    private FilteredList<Recipe> filteredRecipes;
    private Label labelRecipeType;
    private HBox recipeTypeRow;
    
    public BorderPane set(Button addToShoppingList, Button removeFromShoppingList, Button generateShoppingList, 
            Button cancelShoppingList, Button goBackToRecipes, ObservableList<Recipe> remainingRecipesItems, 
            ObservableList<Recipe> chosenRecipesItems) {
        
        this.filteredRecipes = new FilteredList<>(remainingRecipesItems, s -> true);        
        
        this.labelRecipeType = new Label("Suodata reseptin tyypin mukaan:");
        this.recipeTypeComboBox = new ComboBox();
        recipeTypeComboBox.getItems().addAll(
            "kaikki",
            "kala",
            "kasvis",
            "liha",
            "makea"
        ); 

        recipeTypeComboBox.setOnAction(event -> {
            String chosenType = recipeTypeComboBox.getValue().toString();
            filteredRecipes.setPredicate(r -> r.getType().equals(chosenType));
        });
        
        this.recipeTypeRow = new HBox(labelRecipeType, recipeTypeComboBox);
        
        this.remainingRecipesList = new ListView<>(filteredRecipes);       
        remainingRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);         
        remainingRecipesList.setCellFactory(p -> new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty){
            super.updateItem(recipe, empty);
                if(empty || recipe == null || recipe.getName() == null){
                    setText("");
                }
                else{
                    setText(recipe.getName());
                };
            };
        });
        
        this.chosenRecipesList = new ListView<>(chosenRecipesItems);       
        chosenRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);        
        chosenRecipesList.setCellFactory(p -> new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty){
            super.updateItem(recipe, empty);
                if(empty || recipe == null || recipe.getName() == null){
                    setText("");
                }
                else{
                    setText(recipe.getName());
                    //Change listener implemented.
                    /*
                    remainingRecipesList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Recipe> observable, Recipe oldValue, Recipe newValue) -> {
                        if(remainingRecipesList.isFocused()){
                            //textArea.setText(newValue.toString());
                        }
                    });  
                    */
                };
            };
        });        
        
        remainingRecipesList.setOnMouseClicked(event -> {
            chosenRecipesList.getSelectionModel().clearSelection();
            chosenRecipe = remainingRecipesList.getSelectionModel().getSelectedItem().getName();
        });
        
        
        chosenRecipesList.setOnMouseClicked(event -> {
            remainingRecipesList.getSelectionModel().clearSelection();
            chosenRecipe = chosenRecipesList.getSelectionModel().getSelectedItem().getName();            
        });        
        
        this.shoppingListButtons = new HBox(addToShoppingList, removeFromShoppingList, generateShoppingList, cancelShoppingList);        

        this.shoppingListBox = new TextArea();
        shoppingListBox.setPrefWidth(400);
        shoppingListBox.setPrefHeight(300);
        
        this.shoppingListButtonsAlternative = new HBox(goBackToRecipes, cancelShoppingList);         
                
        this.shoppingListSplitPane = new SplitPane(remainingRecipesList, chosenRecipesList);     

        return setRecipeChoosingView();
    }
    
    public BorderPane setShoppingListView(String shoppingList) {
        BorderPane pane = new BorderPane();
        shoppingListBox.setText(shoppingList);
        pane.setCenter(shoppingListBox);
        pane.setBottom(shoppingListButtonsAlternative);
        return pane;
    }
    
    public BorderPane setRecipeChoosingView() {
        BorderPane pane = new BorderPane();
        pane.setTop(recipeTypeRow);
        pane.setCenter(shoppingListSplitPane);
        pane.setBottom(shoppingListButtons);
        return pane;
    }

    public String getChosenRecipe() {    
        String chosen = chosenRecipe;
        chosenRecipe = "";
        return chosen;
    }
    
    public void filterRemainingList(String recipeType) {
        filteredRecipes.setPredicate(recipe -> recipe.getType().equals(recipeType));
    }
                   
}
