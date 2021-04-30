package generator.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
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
    private ListView<String> remainingRecipesList;
    private ListView<String> chosenRecipesList;      
    
    public BorderPane set(Button addToShoppingList, Button removeFromShoppingList, Button generateShoppingList, 
            Button cancelShoppingList, ObservableList<String> remainingRecipesItems, ObservableList<String> chosenRecipesItems) {
        
        this.remainingRecipesList = new ListView<>(remainingRecipesItems);       
        remainingRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);         
        this.chosenRecipesList = new ListView<>(chosenRecipesItems);       
        chosenRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);          
        
        remainingRecipesList.setOnMouseClicked(event -> {
            chosenRecipesList.getSelectionModel().clearSelection();
        });    
        
        chosenRecipesList.setOnMouseClicked(event -> {
            remainingRecipesList.getSelectionModel().clearSelection();
        });        
        
        this.shoppingListButtons = new HBox(addToShoppingList, removeFromShoppingList, generateShoppingList, cancelShoppingList);        

        this.shoppingListBox = new TextArea();
        shoppingListBox.setPrefWidth(400);
        shoppingListBox.setPrefHeight(300);
        
        Button goBackToChoosingRecipes = new Button("Palaa reseptien valintaan");
        
        this.shoppingListButtonsAlternative = new HBox(goBackToChoosingRecipes, cancelShoppingList);         
                
        goBackToChoosingRecipes.setOnMouseClicked(event -> {
            setRecipeChoosingView();
        });
        
        this.shoppingListSplitPane = new SplitPane(remainingRecipesList, chosenRecipesList);     

        return setRecipeChoosingView();
    }
    
    public BorderPane setShoppingListView(String shoppingList) {
        BorderPane pane = new BorderPane();
        this.shoppingListBox.setText(shoppingList);
        pane.setCenter(shoppingListBox);
        pane.setBottom(shoppingListButtonsAlternative);
        return pane;
    }
    
    public BorderPane setRecipeChoosingView() {
        BorderPane pane = new BorderPane();
        pane.setCenter(shoppingListSplitPane);
        pane.setBottom(shoppingListButtons);
        return pane;
    }

    String getChosenRecipe() {
        //remainingRecipesList.getSelectionModel().getSelectedItem();        
        return "";
    }
                   
}
