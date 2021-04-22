package generator.ui;

import generator.dao.FileIngredientDao;
import generator.dao.FileRecipeDao;
import generator.dao.FileUserDao;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import generator.domain.ShoppingListService;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GeneratorUI extends Application {
    
    private RecipeService recipeService;
    private ShoppingListService shoppingListService;
    private ObservableList<String> recipeListItems;
    private ObservableList<String> ingredientListItems;  
    private ObservableList<String> chosenListItems;
    private ObservableList<String> remainingListItems;
            
    
    @Override
    public void init() throws Exception {
        UserDao userDao = new FileUserDao();
        RecipeDao recipeDao = new FileRecipeDao(userDao);
        IngredientDao ingredientDao = new FileIngredientDao(recipeDao);
        this.recipeService = new RecipeService(userDao, recipeDao, ingredientDao);
        this.shoppingListService = new ShoppingListService(userDao, recipeDao, ingredientDao);
        recipeService.login("heinapaa");
    }    
    
    public void updateRecipeList() {
        recipeListItems.clear();
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        for (Recipe recipe : allRecipes) {
            recipeListItems.add(recipe.getName());
        }
    }
    
    public void updateIngredientList(String recipeName) {
        ingredientListItems.clear();
        List<Ingredient> ingredients = recipeService.getIngredients(recipeName);
        for (Ingredient ingredient : ingredients) {
            ingredientListItems.add(ingredient.toString());
        }        
    }  
    
    public void updateRemainingList() {
        remainingListItems.clear();
        for (String recipeName : recipeListItems) {
            remainingListItems.add(recipeName);
        }        
    }
    
    public void updateChosenList() {
        chosenListItems.clear();        
    }    

    @Override
    public void start(Stage window) {

        window.setTitle("Kauppalistageneraattori");
        
        BorderPane listView = new BorderPane();                     
               
        // Resepti-infolaatikko
        
        Label recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));        
        VBox recipeInfoTitles = new VBox();
        VBox recipeInfoContents = new VBox();
        
        Text recipeInfoTitle1 = new Text("Nimi:");
        Text recipeInfo1 = new Text(""); 
        Text recipeInfoTitle2 = new Text("Annoskoko:");
        Text recipeInfo2 = new Text("");              
        
        recipeInfoTitles.getChildren().add(recipeInfoTitle1);
        recipeInfoTitles.getChildren().add(recipeInfoTitle2);        

        recipeInfoContents.getChildren().add(recipeInfo1);
        recipeInfoContents.getChildren().add(recipeInfo2);
        
        // Infobox-asettelu
        
        VBox infoBox = new VBox();
        infoBox.setSpacing(20);
        
        HBox recipeInfo = new HBox();
        recipeInfo.setSpacing(10);
        
        recipeInfo.getChildren().add(recipeInfoTitles);
        recipeInfo.getChildren().add(recipeInfoContents);        
                   
        infoBox.getChildren().add(recipeTitle);
        infoBox.getChildren().add(recipeInfo);
        infoBox.setMinWidth(300);
        
        // Splitpane
        
        this.recipeListItems = FXCollections.observableArrayList();
        this.ingredientListItems = FXCollections.observableArrayList();         
        
        ListView<String> recipeList = new ListView<>(recipeListItems);    
        updateRecipeList();
        recipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
        
        ListView<String> ingredientList = new ListView<>(ingredientListItems);       
        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);     
        
        SplitPane splitPane = new SplitPane(recipeList, ingredientList);     
        
        // Reseptin lisäys -näkymä

        HBox editRecipeView = new HBox();

        
        VBox editRecipeLabels = new VBox(new Label("Reseptin nimi:"), new Label("Annoskoko:"));
        
        TextField recipeNameInput = new TextField();  
        TextField recipePortionInput = new TextField();        
        VBox editRecipeInputs = new VBox(recipeNameInput, recipePortionInput);
        
        editRecipeView.getChildren().add(editRecipeLabels);
        editRecipeView.getChildren().add(editRecipeInputs);            
        
        editRecipeView.setMinWidth(300);
        
        // Aineksen lisäys -ikkuna
        
        BorderPane addIngredientView = new BorderPane(); 
        HBox addIngredientViewLines = new HBox();
        
        VBox addIngredientLabels = new VBox(new Label("Ainesosan nimi:"), new Label("Määrä:"), new Label("Yksikkö:"));
        
        TextField ingredientNameInput = new TextField();
        TextField ingredientAmountInput = new TextField();
        ComboBox ingredientUnitInput = new ComboBox();
        
        ingredientUnitInput.getItems().addAll(
            "kg",
            "g",
            "l",
            "dl",
            "kpl"
        );     
        
        VBox addIngredientInputs = new VBox();
        addIngredientInputs.getChildren().add(ingredientNameInput);
        addIngredientInputs.getChildren().add(ingredientAmountInput);
        addIngredientInputs.getChildren().add(ingredientUnitInput);    
        
        addIngredientViewLines.getChildren().add(addIngredientLabels);
        addIngredientViewLines.getChildren().add(addIngredientInputs);        
        
        HBox addIngredientViewButtons = new HBox();
        Button commitAddIngredient = new Button("Lisää ainesosa");
        Button cancelAddIngredient = new Button("Lopeta");
        addIngredientViewButtons.getChildren().add(commitAddIngredient);
        addIngredientViewButtons.getChildren().add(cancelAddIngredient);   
        
        addIngredientView.setRight(addIngredientViewLines);
        addIngredientView.setBottom(addIngredientViewButtons);

        Stage addIngredientWindow = new Stage();
        addIngredientWindow.initModality(Modality.APPLICATION_MODAL);            
        
        Scene ingredientScene = new Scene(addIngredientView);
        addIngredientWindow.setScene(ingredientScene);
        addIngredientWindow.setWidth(300);  
        
        // Uusi ostoslista -ikkunan napit
        
        BorderPane shoppingListView = new BorderPane();  
        
        HBox shoppingListButtons = new HBox();
        
        Button addToShoppingList = new Button("Lisää ostoslistalle");
        Button removeFromShoppingList = new Button("Poista ostoslistalta");        
        Button generateShoppingList = new Button("Luo ostoslista");
        Button cancelShoppingList = new Button("Peruuta");

        shoppingListButtons.getChildren().add(addToShoppingList); 
        shoppingListButtons.getChildren().add(removeFromShoppingList);          
        shoppingListButtons.getChildren().add(generateShoppingList);
        shoppingListButtons.getChildren().add(cancelShoppingList);

        shoppingListView.setBottom(shoppingListButtons);        
        
        // Uusi ostoslista -valintaikkuna
        
        this.remainingListItems = FXCollections.observableArrayList();  
        this.chosenListItems = FXCollections.observableArrayList();         
          
        ListView<String> remainingRecipesListView = new ListView<>(remainingListItems);    
        remainingRecipesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
        
        ListView<String> chosenRecipesListView = new ListView<>(chosenListItems);       
        chosenRecipesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);     
        
        SplitPane shoppingListSplitPane = new SplitPane(remainingRecipesListView, chosenRecipesListView);     
        
        shoppingListView.setCenter(shoppingListSplitPane);
        
        Stage shoppingListWindow = new Stage();
        shoppingListWindow.initModality(Modality.APPLICATION_MODAL);            
        
        Scene shoppingListScene = new Scene(shoppingListView);
        shoppingListWindow.setScene(shoppingListScene);
        shoppingListWindow.setWidth(500);  
        shoppingListWindow.setHeight(400);
        
        // Uusi ostoslista -tulosikkuna
        
        TextArea shoppingListBox = new TextArea();
        shoppingListBox.setPrefWidth(400);
        shoppingListBox.setPrefHeight(300);
        
        Button goBackToChoosingRecipes = new Button("Palaa reseptien valintaan");
        
        HBox shoppingListButtonsAlternative = new HBox();
        shoppingListButtonsAlternative.getChildren().add(goBackToChoosingRecipes);
        shoppingListButtonsAlternative.getChildren().add(cancelShoppingList);        

        // Reseptin lisäys -napit
        
        Button commitAddRecipe = new Button("Tallenna resepti");
        Button cancelAddRecipe = new Button("Peruuta");      
        HBox addRecipeButtons = new HBox(commitAddRecipe, cancelAddRecipe);
        
        // Reseptin muokkaus -napit
        
        Button commitEditRecipe = new Button("Tallenna muutokset");
        Button cancelEditRecipe = new Button("Peruuta"); 
        Button addIngredient = new Button("Lisää ainesosa");
        Button deleteIngredient = new Button("Poista ainesosa");        
        Button deleteRecipe = new Button("Poista resepti");
        HBox editRecipeButtons = new HBox(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
     
        // Ylänapit
        
        HBox topButtons = new HBox();
        topButtons.setSpacing(10);
        
        Button addRecipe = new Button("Lisää resepti");
        Button editRecipe = new Button("Muokkaa reseptiä");        
        Button newShoppingList = new Button("Uusi ostoslista");
        
        topButtons.getChildren().add(addRecipe);
        topButtons.getChildren().add(editRecipe);        
        topButtons.getChildren().add(newShoppingList);    
           
        // Listeners
        
        recipeList.setOnMouseClicked(event -> {
            listView.setLeft(infoBox);            
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            updateIngredientList(recipeName);
            recipeInfo1.setText(recipeName);
            recipeInfo2.setText(String.valueOf(recipeService.getRecipe(recipeName).getServing()));    ;
        });          
        
        addRecipe.setOnMouseClicked(event -> {
            recipeList.setMouseTransparent(true);            
            listView.getChildren().remove(listView.getLeft());
            listView.setLeft(editRecipeView);
            listView.setBottom(addRecipeButtons);
        });    
        
        commitAddRecipe.setOnMouseClicked(event -> {
            String recipeName = recipeNameInput.getText();
            int recipePortion = Integer.valueOf(recipePortionInput.getText());
            recipeService.createRecipe(recipeName, recipePortion);     
            updateRecipeList();
            recipeNameInput.setText("");
            recipePortionInput.setText("");            
            listView.getChildren().remove(listView.getLeft());
            listView.getChildren().remove(listView.getBottom());            
            listView.setLeft(infoBox);
            recipeList.setMouseTransparent(false);             
        });  
        
        cancelAddRecipe.setOnMouseClicked(event -> {
            recipeNameInput.setText("");
            recipePortionInput.setText("");
            listView.getChildren().remove(listView.getLeft());
            listView.getChildren().remove(listView.getBottom());            
            listView.setLeft(infoBox);
            recipeList.setMouseTransparent(false);   
        });        
                 
        editRecipe.setOnMouseClicked(event -> {
            if (recipeList.getSelectionModel().getSelectedItem() != null) {
                recipeList.setMouseTransparent(true);                 
                listView.setLeft(editRecipeView);
                listView.setBottom(editRecipeButtons);
                String recipeName = recipeList.getSelectionModel().getSelectedItem();
                recipeNameInput.setText(recipeName);
                recipePortionInput.setText(String.valueOf(recipeService.getRecipe(recipeName).getServing()));            
            }       
        });     
        
        cancelEditRecipe.setOnMouseClicked(event -> {
            recipeNameInput.setText("");
            recipePortionInput.setText("");
            listView.getChildren().remove(listView.getLeft());
            listView.getChildren().remove(listView.getBottom());             
            listView.setLeft(infoBox);
            recipeList.setMouseTransparent(false);             
        }); 
        
        deleteRecipe.setOnMouseClicked(event -> {     
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            recipeService.removeRecipe(recipeName);
            updateRecipeList();
            updateIngredientList(recipeName);
            recipeNameInput.setText("");
            recipePortionInput.setText("");
            recipeInfo1.setText("");
            recipeInfo2.setText("");            
            listView.getChildren().remove(listView.getLeft());
            listView.getChildren().remove(listView.getBottom());             
            listView.setLeft(infoBox);
            recipeList.setMouseTransparent(false);             
        });    
        
        addIngredient.setOnMouseClicked(event -> { 
            addIngredientWindow.show();
        });   
        
        commitAddIngredient.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientNameInput.getText();
            double ingredientAmount = Double.valueOf(ingredientAmountInput.getText());
            String ingredientUnit = ingredientUnitInput.getValue().toString();
            recipeService.addIngredient(recipeName, ingredientName,ingredientUnit, ingredientAmount);
            updateIngredientList(recipeName);
            ingredientNameInput.setText("");
            ingredientAmountInput.setText("");
            ingredientUnitInput.setValue(null);       
        });      
        
        cancelAddIngredient.setOnMouseClicked(event -> {
            ingredientNameInput.setText("");
            ingredientAmountInput.setText("");
            ingredientUnitInput.setValue(null);            
            addIngredientWindow.close();
        });         
        
        deleteIngredient.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientList.getSelectionModel().getSelectedItem().split(",")[0];        
            recipeService.removeIngredient(recipeName, ingredientName);
            updateIngredientList(recipeName);
        });      
        
        newShoppingList.setOnMouseClicked(event -> {
            shoppingListWindow.show();
            updateRemainingList();
            updateChosenList();            
        });         
        
        cancelShoppingList.setOnMouseClicked(event -> {
            shoppingListWindow.close();
        });   
        
        remainingRecipesListView.setOnMouseClicked(event -> {
            chosenRecipesListView.getSelectionModel().clearSelection();
        });    
        
        chosenRecipesListView.setOnMouseClicked(event -> {
            remainingRecipesListView.getSelectionModel().clearSelection();
        });           
        
        addToShoppingList.setOnMouseClicked(event -> {
            if (remainingRecipesListView.getSelectionModel() != null) {
                String chosenRecipe = remainingRecipesListView.getSelectionModel().getSelectedItem();
                remainingListItems.remove(chosenRecipe);
                chosenListItems.add(chosenRecipe);
                Collections.sort(chosenListItems);                
            }
        });        
        
        removeFromShoppingList.setOnMouseClicked(event -> {
            if (chosenRecipesListView.getSelectionModel() != null) {
            String chosenRecipe = chosenRecipesListView.getSelectionModel().getSelectedItem();
            chosenListItems.remove(chosenRecipe);
            remainingListItems.add(chosenRecipe);
            Collections.sort(remainingListItems);                
            }
        });  
        
        generateShoppingList.setOnMouseClicked(event -> {
            
            String shoppingList = shoppingListService.createShoppingList(chosenListItems);
            shoppingListBox.setText(shoppingList);
            
            shoppingListView.getChildren().remove(shoppingListView.getCenter());
            shoppingListView.getChildren().remove(shoppingListView.getBottom());            
            shoppingListView.setCenter(shoppingListBox);
            shoppingListView.setBottom(shoppingListButtonsAlternative);
        });
        
        goBackToChoosingRecipes.setOnMouseClicked(event -> {
            shoppingListView.getChildren().remove(shoppingListView.getCenter());
            shoppingListView.getChildren().remove(shoppingListView.getBottom());              
            shoppingListView.setCenter(shoppingListSplitPane);
            shoppingListView.setBottom(shoppingListButtons);
        });        
             
        // Asettelu
        listView.setTop(topButtons);
        listView.setCenter(splitPane);
        listView.setLeft(infoBox);
             
        Scene scene = new Scene(listView);
        window.setScene(scene);
        window.setWidth(900);
        window.show();
    }

    public static void main(String[] args) {
        launch(GeneratorUI.class);
    }
    
}
