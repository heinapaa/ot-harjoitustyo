package generator.ui;

import generator.models.Recipe;
import generator.services.ShoppingListService;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
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
import javafx.stage.FileChooser;

public class ShoppingListView implements View {
    
    private final Router router;
    private final ShoppingListService shoppingListService; 
    private final List<String> recipeTypes;    
    
    private BorderPane pane;
    private FilteredList<Recipe> filteredRecipes;           
    private ObservableList<Recipe> remainingRecipesItems;
    private ObservableList<Recipe> chosenRecipesItems;
    private ListView<Recipe> remainingRecipesList;
    private ListView<Recipe> chosenRecipesList;      
    
    public ShoppingListView(Router router, ShoppingListService shoppingListService, List<String> recipeTypes) {
        this.router = router;
        this.shoppingListService = shoppingListService;       
        this.remainingRecipesItems = FXCollections.observableArrayList();        
        this.chosenRecipesItems = FXCollections.observableArrayList();     
        this.recipeTypes = recipeTypes;
    }
    
    @Override
    public Scene create() {
        this.pane = new BorderPane();
        recipeChoosingMode();
        return new Scene(pane);          
    }
    
    public void addCurrentRecipes(ObservableList<Recipe> currentRecipes) {
        this.remainingRecipesItems.clear();
        for (Recipe recipe : currentRecipes) {
            remainingRecipesItems.add(recipe);
        }          
        this.chosenRecipesItems.clear();   
    }
      
    public void recipeChoosingMode() {      
        Label labelRecipeType = new Label("Suodata reseptin tyypin mukaan:");
        ComboBox recipeTypeComboBox = new ComboBox();
        for (String type : recipeTypes) {
            recipeTypeComboBox.getItems().add(type);
        }

        recipeTypeComboBox.setOnAction(event -> {
            String chosenType = recipeTypeComboBox.getSelectionModel().getSelectedItem().toString();
            if (chosenType.equals("Näytä kaikki") || chosenType.isBlank()) {
                filteredRecipes.setPredicate(r -> true);
            } else {
                filteredRecipes.setPredicate(r -> r.getType().equals(chosenType));                
            }       
        });
        
        HBox recipeTypeRow = new HBox(labelRecipeType, recipeTypeComboBox);
        recipeTypeRow.setSpacing(20);
        
        this.filteredRecipes = new FilteredList<>(remainingRecipesItems);        
        this.remainingRecipesList = new ListView<>(filteredRecipes);       
        remainingRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);         
        remainingRecipesList.setCellFactory(p -> new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);
                if (empty || recipe == null || recipe.getName() == null) {
                    setText("");
                } else {
                    setText(recipe.getName());
                }
            }
        });
        
        this.chosenRecipesList = new ListView<>(chosenRecipesItems);       
        chosenRecipesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);        
        chosenRecipesList.setCellFactory(p -> new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);
                if (empty || recipe == null || recipe.getName() == null) {
                    setText("");
                } else {
                    setText(recipe.getName());
                }
            }
        });        
        
        remainingRecipesList.setOnMouseClicked(event -> {
            chosenRecipesList.getSelectionModel().clearSelection();
        });
        
        chosenRecipesList.setOnMouseClicked(event -> {
            remainingRecipesList.getSelectionModel().clearSelection();           
        });    
        
        Button addToShoppingList = new Button("Lisää listalle");
        addToShoppingList.setOnMouseClicked(event -> {
            if (!remainingRecipesList.getSelectionModel().isEmpty()) {          
                chosenRecipesItems.add(remainingRecipesList.getSelectionModel().getSelectedItem());  
                remainingRecipesItems.remove(remainingRecipesList.getSelectionModel().getSelectedItem());               
            }
        }); 
        
        Button removeFromShoppingList = new Button("Poista listalta");
        removeFromShoppingList.setOnMouseClicked(event -> {
            if (!chosenRecipesList.getSelectionModel().isEmpty()) {
                remainingRecipesItems.add(chosenRecipesList.getSelectionModel().getSelectedItem());                  
                chosenRecipesItems.remove(chosenRecipesList.getSelectionModel().getSelectedItem());    
            }
        });         
        
        Button generateShoppingList = new Button("Luo ostoslista");
        generateShoppingList.setOnMouseClicked(event -> {
            String shoppingList = shoppingListService.createShoppingList(chosenRecipesItems);
            shoppingListMode(shoppingList);
        });  
        
        HBox shoppingListButtons = new HBox(addToShoppingList, removeFromShoppingList, generateShoppingList);        
        shoppingListButtons.setSpacing(20);
        
        SplitPane shoppingListSplitPane = new SplitPane(remainingRecipesList, chosenRecipesList);     
        
        pane.setTop(recipeTypeRow);
        pane.setCenter(shoppingListSplitPane);
        pane.setBottom(shoppingListButtons);              
    }
    
    public void shoppingListMode(String shoppingList) {
        TextArea shoppingListBox = new TextArea();
        shoppingListBox.setPrefWidth(400);
        shoppingListBox.setPrefHeight(300);
        
        Button goBackToRecipes = new Button("Palaa reseptien valintaan");
        goBackToRecipes.setOnMouseClicked(event -> recipeChoosingMode());
        
        Button saveShoppingList = new Button("Talenna ostoslista");
        saveShoppingList.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(router.shoppingListStage);
            if (file != null) {
                shoppingListService.saveToFile(shoppingListBox.getText(), file);
            }            
        });   
        
        Button cancelShoppingList = new Button("Sulje ikkuna");
        cancelShoppingList.setOnMouseClicked(event -> router.closeShoppingListWindow());
        
        HBox buttons = new HBox(goBackToRecipes, saveShoppingList, cancelShoppingList);   
        buttons.setSpacing(20);
        
        shoppingListBox.setText(shoppingList);
        pane.setTop(new Label("Ostoslista:"));
        pane.setCenter(shoppingListBox);
        pane.setBottom(buttons);   
    }      
}
