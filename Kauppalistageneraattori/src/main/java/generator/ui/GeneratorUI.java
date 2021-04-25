package generator.ui;

import generator.dao.FileIngredientDao;
import generator.dao.FileRecipeDao;
import generator.dao.FileUserDao;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.domain.Ingredient;
import generator.domain.IngredientService;
import generator.domain.InputValidator;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import generator.domain.ShoppingListService;
import generator.domain.UserService;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GeneratorUI extends Application {
    
    private UserService userService;
    private RecipeService recipeService;
    private IngredientService ingredientService;
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
        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao);
        this.userService = new UserService(userDao, validator);
        this.recipeService = new RecipeService(recipeDao, ingredientDao, validator);
        this.ingredientService = new IngredientService(recipeDao, ingredientDao, validator);
        this.shoppingListService = new ShoppingListService(recipeDao, ingredientDao);
    }    
    
    public void updateRecipeList() {
        recipeListItems.clear();
        List<Recipe> allRecipes = recipeService.getAllRecipes(userService.getLoggedIn());
        for (Recipe recipe : allRecipes) {
            recipeListItems.add(recipe.getName());
        }
    }
    
    public void updateIngredientList(String recipeName) {
        ingredientListItems.clear();
        List<Ingredient> ingredients = ingredientService.getIngredients(recipeName);
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
        
        // Login-ruutu
        
        Label userName = new Label("Syötä käyttäjätunnus (vähintään 3 merkkiä):");
        TextField userNameInput = new TextField();
        Button logIn = new Button("Kirjaudu");
        Button createUser = new Button("Luo uusi käyttäjä");
        Label logInError = new Label("");
        logInError.setTextFill(Color.RED);        
        
        VBox logInView = new VBox();
        logInView.setAlignment(Pos.CENTER);
        logInView.setSpacing(20);    
        
        HBox logInInput = new HBox();
        logInInput.setAlignment(Pos.CENTER);
        logInInput.setSpacing(20);   
        
        HBox logInButtons = new HBox();
        logInButtons.setAlignment(Pos.CENTER);
        logInButtons.setSpacing(20);           
               
        logInInput.getChildren().add(userName);
        logInInput.getChildren().add(userNameInput);
        logInInput.getChildren().add(logInError);   
        
        logInButtons.getChildren().add(logIn);
        logInButtons.getChildren().add(createUser);
        
        logInView.getChildren().add(logInInput);
        logInView.getChildren().add(logInButtons);         

        
      
        // Resepti-infolaatikko                             
        
        Label recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));           
        
        Text recipeInfoTitle1 = new Text("Nimi:");
        Text recipeInfo1 = new Text(""); 
        Text recipeInfoTitle2 = new Text("Annoskoko:");
        Text recipeInfo2 = new Text("");    
        
        Button editRecipe = new Button("Muokkaa reseptiä");        

        VBox infoBox = new VBox();
        infoBox.setSpacing(20);

        HBox recipeNameRow = new HBox();
        recipeNameRow.setSpacing(20);          
        HBox recipeServingRow = new HBox();
        recipeServingRow.setSpacing(20);         
        
        recipeNameRow.getChildren().add(recipeInfoTitle1);
        recipeNameRow.getChildren().add(recipeInfo1);        

        recipeServingRow.getChildren().add(recipeInfoTitle2);
        recipeServingRow.getChildren().add(recipeInfo2);        
                              
        infoBox.getChildren().add(recipeTitle);
        infoBox.getChildren().add(recipeNameRow);
        infoBox.getChildren().add(recipeServingRow); 
        infoBox.setMinWidth(300);
        
        
        
        // Reseptit ja ainekset
        
        this.recipeListItems = FXCollections.observableArrayList();
        this.ingredientListItems = FXCollections.observableArrayList();         
        
        ListView<String> recipeList = new ListView<>(recipeListItems);    
        updateRecipeList();
        recipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
        
        ListView<String> ingredientList = new ListView<>(ingredientListItems);       
        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);     
        
        SplitPane splitPane = new SplitPane(recipeList, ingredientList);     
        
        
        
        // Reseptin lisäys -näkymä

        TextField recipeNameInput = new TextField();  
        TextField recipeServingInput = new TextField();        
        
        
        
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
        VBox editRecipeButtons = new VBox(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
     
        
        
        // Perusnapit
        
        HBox topButtons = new HBox();
        topButtons.setSpacing(10);
        
        Button addRecipe = new Button("Lisää resepti");        
        Button newShoppingList = new Button("Uusi ostoslista");
        Button changeUser = new Button("Vaihda käyttäjää");
        
        topButtons.getChildren().add(addRecipe);       
        topButtons.getChildren().add(newShoppingList);
        topButtons.getChildren().add(changeUser);
        
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
          
        
        
        // Listeners
        
        recipeList.setOnMouseClicked(event -> {
            listView.setLeft(infoBox);            
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            updateIngredientList(recipeName);
            recipeInfo1.setText(recipeName);
            recipeInfo2.setText(String.valueOf(recipeService.getRecipe(recipeName).getServing()));
            infoBox.getChildren().add(editRecipe);            
        });          
        
        addRecipe.setOnMouseClicked(event -> {
            recipeList.setMouseTransparent(true);
            changeUser.setMouseTransparent(true);
            newShoppingList.setMouseTransparent(true);
            errorLabel.setText("");            
            recipeInfo1.setText("");
            recipeInfo2.setText("");
            
            recipeNameRow.getChildren().remove(recipeInfo1);
            recipeNameRow.getChildren().add(recipeNameInput);
            recipeServingRow.getChildren().remove(recipeInfo2);
            recipeServingRow.getChildren().add(recipeServingInput);
            infoBox.getChildren().add(addRecipeButtons);
        });
        
        commitAddRecipe.setOnMouseClicked(event -> {
            errorLabel.setText("");       
            
            String recipeName = recipeNameInput.getText();
            String recipeServing = recipeServingInput.getText();
            if (recipeService.createRecipe(recipeName, recipeServing, userService.getLoggedIn())) {
                updateRecipeList();
                recipeList.getSelectionModel().select(recipeName);
                infoBox.getChildren().remove(addRecipeButtons);
                infoBox.getChildren().add(editRecipeButtons);                  
            } else {
                errorLabel.setText("Reseptin lisääminen epäonnistui!");
            }
        });  
        
        cancelAddRecipe.setOnMouseClicked(event -> {
            errorLabel.setText("");            
            recipeNameInput.setText("");
            recipeServingInput.setText("");
            
            recipeNameRow.getChildren().remove(recipeNameInput);
            recipeNameRow.getChildren().add(recipeInfo1);
            recipeServingRow.getChildren().remove(recipeServingInput);
            recipeServingRow.getChildren().add(recipeInfo2);
            infoBox.getChildren().remove(addRecipeButtons);
            
            recipeList.setMouseTransparent(false);   
            changeUser.setMouseTransparent(false);
            newShoppingList.setMouseTransparent(false);            
        });        
                 
        editRecipe.setOnMouseClicked(event -> {
            errorLabel.setText("");          
            
            if (recipeList.getSelectionModel().getSelectedItem() != null) {
                recipeList.setMouseTransparent(true);   
                changeUser.setMouseTransparent(true);
                newShoppingList.setMouseTransparent(true);                
                
                recipeNameRow.getChildren().remove(recipeInfo1);
                recipeNameRow.getChildren().add(recipeNameInput);
                recipeServingRow.getChildren().remove(recipeInfo2);
                recipeServingRow.getChildren().add(recipeServingInput);
                String recipeName = recipeList.getSelectionModel().getSelectedItem();
                recipeNameInput.setText(recipeName);
                recipeServingInput.setText(String.valueOf(recipeService.getRecipe(recipeName).getServing()));  
                infoBox.getChildren().remove(editRecipe);
                infoBox.getChildren().add(editRecipeButtons);
            } else {
                errorLabel.setText("Valitse muokattava resepti!");
            }       
        });     
        
        cancelEditRecipe.setOnMouseClicked(event -> {
            errorLabel.setText("");            
            recipeNameInput.setText("");
            recipeServingInput.setText("");
            
            recipeNameRow.getChildren().remove(recipeNameInput);
            recipeNameRow.getChildren().add(recipeInfo1);
            recipeServingRow.getChildren().remove(recipeServingInput);
            recipeServingRow.getChildren().add(recipeInfo2);            
            infoBox.getChildren().remove(editRecipeButtons);
            
            recipeList.setMouseTransparent(false);  
            changeUser.setMouseTransparent(false);
            newShoppingList.setMouseTransparent(false);            
        }); 
        
        deleteRecipe.setOnMouseClicked(event -> {   
            errorLabel.setText(""); 
            
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            if (recipeService.removeRecipe(recipeName)) {
                updateRecipeList();
                ingredientListItems.clear();
                recipeNameInput.setText("");
                recipeServingInput.setText("");
                recipeInfo1.setText("");
                recipeInfo2.setText("");            
                recipeNameRow.getChildren().remove(recipeNameInput);
                recipeNameRow.getChildren().add(recipeInfo1);
                recipeServingRow.getChildren().remove(recipeServingInput);
                recipeServingRow.getChildren().add(recipeInfo2);            
                infoBox.getChildren().remove(editRecipeButtons);
                
                recipeList.setMouseTransparent(false);       
                changeUser.setMouseTransparent(false);
                newShoppingList.setMouseTransparent(false);                
            } else {
                errorLabel.setText("Reseptin poistaminen epäonnistui!");
            }
            
        });    
        
        addIngredient.setOnMouseClicked(event -> { 
            errorLabel.setText("");            
            addIngredientWindow.show();
        });   
        
        commitAddIngredient.setOnMouseClicked(event -> {
            errorLabel.setText("");
            
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientNameInput.getText();
            String ingredientAmount = ingredientAmountInput.getText();
            String ingredientUnit = ingredientUnitInput.getValue().toString();
            if (ingredientService.addIngredient(recipeName, ingredientName,ingredientUnit, ingredientAmount)) {
                updateIngredientList(recipeName);
                ingredientNameInput.setText("");
                ingredientAmountInput.setText("");
                ingredientUnitInput.setValue(null);                  
            } else {
                errorLabel.setText("Ainesosan lisääminen epäonnistui!");
            }
        });      
        
        cancelAddIngredient.setOnMouseClicked(event -> {
            ingredientNameInput.setText("");
            ingredientAmountInput.setText("");
            ingredientUnitInput.setValue(null);            
            addIngredientWindow.close();
        });         
        
        deleteIngredient.setOnMouseClicked(event -> {
            errorLabel.setText("");
            
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientList.getSelectionModel().getSelectedItem().split(",")[0];        
            if (ingredientService.removeIngredient(recipeName, ingredientName)) {
                updateIngredientList(recipeName);                
            } else {
                errorLabel.setText("Ainesosan poistaminen epäonnistui!");
            }
        });      
        
        newShoppingList.setOnMouseClicked(event -> {
            errorLabel.setText("");
            
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
        
        logIn.setOnMouseClicked(event -> {
            logInError.setText("");
            if (userService.login(userNameInput.getText())) {
                updateRecipeList();
                userNameInput.setText("");
                logInError.setText("");
                listView.getChildren().remove(listView.getCenter());
                listView.setTop(topButtons);
                listView.setCenter(splitPane);
                listView.setLeft(infoBox);    
                listView.setBottom(errorLabel);
            } else {
                logInError.setText("Virheellinen käyttäjätunnus!");
            }
        });
        
        createUser.setOnMouseClicked(event -> {
            logInError.setText("");
            if (userService.addNewUser(userNameInput.getText())) {
                userService.login(userNameInput.getText());
                updateRecipeList();
                userNameInput.setText("");
                logInError.setText("");
                listView.getChildren().remove(listView.getCenter());
                listView.setTop(topButtons);
                listView.setCenter(splitPane);
                listView.setLeft(infoBox);       
                listView.setBottom(errorLabel);
            } else {
                logInError.setText("Virheellinen käyttäjätunnus!");
            }

        });        
        
        changeUser.setOnMouseClicked(event -> {
            errorLabel.setText("");
            logInError.setText("");
            recipeInfo1.setText("");
            recipeInfo2.setText("");
            
            recipeListItems.clear();
            ingredientListItems.clear();            
            
            listView.getChildren().remove(listView.getTop());
            listView.getChildren().remove(listView.getLeft());            
            listView.getChildren().remove(listView.getCenter());
            listView.getChildren().remove(listView.getBottom());            
            listView.setCenter(logInView);
        });
        
       
        
        // Asettelu
        listView.setCenter(logInView);    
        Scene scene = new Scene(listView);
        window.setScene(scene);
        window.setWidth(900);
        window.setHeight(600);
        window.show();
    }

    public static void main(String[] args) {
        launch(GeneratorUI.class);
    }   
}