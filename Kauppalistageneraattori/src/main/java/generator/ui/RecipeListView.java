package generator.ui;

import generator.models.Ingredient;
import generator.models.Recipe;
import generator.services.IngredientService;
import generator.services.RecipeService;
import generator.services.UserService;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RecipeListView implements View {

    private BorderPane pane;    
    private ComboBox recipeTypeComboBox; 
    private HBox recipeNameRow;
    private HBox recipePortionRow;
    private HBox recipeTypeRow;
    private Label infoRecipeName;
    private Label infoRecipePortion;     
    private Label infoRecipeType;    
    private Label labelRecipeName;
    private Label labelRecipePortion;
    private Label labelRecipeType;      
    private Label errorLabel;   
    private TextField inputFieldRecipeName;
    private TextField inputFieldRecipePortion;
    private VBox recipeButtons;

    private final Router router;
    private final UserService userService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    
    private final ObservableList<Recipe> recipeListItems;
    private final ObservableList<Ingredient> ingredientListItems;
    private final ListView<Recipe> recipeList;
    private final ListView<Ingredient> ingredientList;    
    private final List<String> acceptableTypes;
    
    public RecipeListView(Router router, UserService userService, RecipeService recipeService, IngredientService ingredientService, List<String> acceptableTypes) {
        this.router = router;
        this.userService = userService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService; 
        this.acceptableTypes = acceptableTypes;
        
        this.recipeListItems = FXCollections.observableArrayList();
        this.recipeList = new ListView<>(recipeListItems);    
        recipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
        recipeList.setCellFactory(p -> new ListCell<Recipe>() {
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
        
        recipeList.setOnMouseClicked(event -> {       
            if (recipeList.getSelectionModel().getSelectedItem() != null) {
                Recipe recipe = recipeList.getSelectionModel().getSelectedItem();   
                infoMode(recipe);  
                updateIngredientList(recipe);                                
            }
        });         
        
        this.ingredientListItems = FXCollections.observableArrayList();         
        this.ingredientList = new ListView<>(ingredientListItems);       
        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);      
        ingredientList.setCellFactory(p -> new ListCell<Ingredient>() {
            @Override
            protected void updateItem(Ingredient ingredient, boolean empty) {
                super.updateItem(ingredient, empty);
                    if (empty || ingredient == null || ingredient.getName() == null) {
                        setText("");
                    } else {
                        setText(ingredient.getName() + ", " + String.valueOf(ingredient.getAmount()) + " " + ingredient.getUnit());                        
                    }                  
            }
        });         
    }
    

    @Override
    public Scene create() {        
        Button addRecipe = new Button("Lisää resepti");
        addRecipe.setOnMouseClicked(event -> addRecipeMode()); 
        
        Button newShoppingList = new Button("Uusi ostoslista");
        newShoppingList.setOnMouseClicked(event -> router.openShoppingListWindow(recipeListItems));
       
        Button changeUser = new Button("Vaihda käyttäjää");
        changeUser.setOnMouseClicked(event -> router.setLogInView());
        
        HBox topButtons = new HBox(addRecipe, newShoppingList, changeUser);
        topButtons.setSpacing(10);
        topButtons.setPadding(new Insets(5, 5, 5, 5));        
        
        this.errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED); 
        errorLabel.setPadding(new Insets(5, 5, 5, 5));
        
        SplitPane splitPane = new SplitPane(recipeList, ingredientList);       

        Label recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));           
        
        this.labelRecipeName = new Label("Nimi:");
        this.infoRecipeName = new Label(); 
        this.inputFieldRecipeName = new TextField();
        
        this.labelRecipePortion = new Label("Annoskoko:");
        this.infoRecipePortion = new Label();   
        this.inputFieldRecipePortion = new TextField();
        
        this.labelRecipeType = new Label("Tyyppi:");
        this.infoRecipeType = new Label();   
        this.recipeTypeComboBox = new ComboBox();
        
        for (String type : acceptableTypes) {
            recipeTypeComboBox.getItems().add(type);
        }          
        
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
        infoBox.setPadding(new Insets(5, 10, 5, 10));
        infoBox.setMinWidth(300);   
     
        this.pane = new BorderPane();
        pane.setTop(topButtons);
        pane.setCenter(splitPane);
        pane.setBottom(errorLabel); 
        pane.setLeft(infoBox);            
        defaultMode();
        updateRecipeList();  
        updateIngredientList();
        return new Scene(pane);    
    }  
    
    public void defaultMode() {
        infoRecipeName.setText("");
        infoRecipePortion.setText("");
        infoRecipeType.setText("");
        setInfoRows();
        
        recipeButtons.getChildren().clear();
        
        errorLabel.setText("");  
    }
    
    public void infoMode(Recipe recipe) {
        infoRecipeName.setText(recipe.getName());
        infoRecipePortion.setText(String.valueOf(recipe.getPortion()));
        infoRecipeType.setText(recipe.getType());         
        setInfoRows();
        
        errorLabel.setText("");
        
        Button editRecipe = new Button("Muokkaa reseptiä");
        editRecipe.setOnMouseClicked(event -> editRecipeMode(recipe));
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().add(editRecipe);   
    }
    
    public void addRecipeMode() { 
        inputFieldRecipeName.setText("");
        inputFieldRecipePortion.setText("");
        recipeTypeComboBox.getSelectionModel().select(null);
        setInputRows();
        
        errorLabel.setText("");           
        
        Button commitAddRecipe = new Button("Tallenna resepti");
        commitAddRecipe.setOnMouseClicked(event -> {
            String recipeName = inputFieldRecipeName.getText();
            String recipePortion = inputFieldRecipePortion.getText();
            if (!recipeTypeComboBox.getSelectionModel().isEmpty() && !recipeName.isBlank() && !recipePortion.isBlank()) {
                String recipeType = recipeTypeComboBox.getSelectionModel().getSelectedItem().toString();  
                try {
                    if (recipeService.createRecipe(recipeName, recipePortion, recipeType, userService.getLoggedIn())) {
                        updateRecipeList();
                        Recipe recipe = recipeService.getRecipe(recipeName.strip(), userService.getLoggedIn());
                        recipeList.getSelectionModel().select(recipe);
                        editRecipeMode(recipe);                
                    } else {
                        errorLabel.setText("Virhe! Reseptin tallennus epäonnistui.");
                    }                     
                } catch (NumberFormatException e) {
                    errorLabel.setText("Virhe! Reseptin annoksen täytyy olla kokonaisluku.");
                }              
            } else {
                errorLabel.setText("Virhe! Varmista, että kaikki kentät on täytetty.");
            }
        });        
        
        Button cancelAddRecipe = new Button("Peruuta");
        cancelAddRecipe.setOnMouseClicked(event -> defaultMode()); 
        
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().addAll(commitAddRecipe, cancelAddRecipe);
    }
    
    public void editRecipeMode(Recipe recipe) {
        inputFieldRecipeName.setText(recipe.getName());
        inputFieldRecipePortion.setText(String.valueOf(recipe.getPortion()));
        recipeTypeComboBox.getSelectionModel().select(recipe.getType());
        setInputRows();
        
        errorLabel.setText("");

        Button commitEditRecipe = new Button("Tallenna muutokset");
        commitEditRecipe.setOnMouseClicked(event -> {
            String newRecipeName = inputFieldRecipeName.getText();
            String newRecipePortion = inputFieldRecipePortion.getText();
            if (!recipeTypeComboBox.getSelectionModel().isEmpty() && !newRecipeName.isBlank() && !newRecipePortion.isBlank()) {
                String newRecipeType = recipeTypeComboBox.getSelectionModel().getSelectedItem().toString();
                try {
                    if (recipeService.updateRecipe(recipe, newRecipeName, newRecipePortion, newRecipeType, userService.getLoggedIn())) {  
                        Recipe newRecipe = recipeService.getRecipe(newRecipeName.strip(), userService.getLoggedIn());
                        updateRecipeList();
                        recipeList.getSelectionModel().select(newRecipe);                         
                        infoMode(newRecipe);               
                    } else {
                        errorLabel.setText("Virhe! Muutosten tallennus epäonnistui.");
                    }                     
                } catch (NumberFormatException e) {
                    errorLabel.setText("Virhe! Reseptin annoksen täytyy olla kokonaisluku.");
                }                
            } else {
                errorLabel.setText("Virhe! Varmista, että kaikki kentät on täytetty.");
            } 
        });
          
        Button cancelEditRecipe = new Button("Peruuta");
        cancelEditRecipe.setOnMouseClicked(event -> infoMode(recipe));
        
        Button addIngredient = new Button("Lisää ainesosa");
        addIngredient.setOnMouseClicked(event -> router.openIngredientWindow(recipe));
        
        Button deleteIngredient = new Button("Poista ainesosa");
        deleteIngredient.setOnMouseClicked(event -> {
            if (ingredientList.getSelectionModel().getSelectedItem() != null) {
                Ingredient ingredient = ingredientList.getSelectionModel().getSelectedItem();                  
                Alert alert = new Alert(AlertType.NONE, "Poistetaanko " + ingredient.getName() + "?\nVaroitus! Poistettua ainesosaa ei voida palauttaa.", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    if (ingredientService.removeIngredient(recipe, ingredient)) {
                        updateIngredientList(recipe);                
                    } else {
                        errorLabel.setText("Virhe! Ainesosan poistaminen epäonnistui.");
                    }  
                }                  
            } else {
                errorLabel.setText("Virhe! Valitse poistettava ainesosa.");
            }
        });
        
        Button deleteRecipe = new Button("Poista resepti");
        deleteRecipe.setOnMouseClicked(event -> {
            Alert alert = new Alert(AlertType.NONE, "Poistetaanko " + recipe.getName() + "?\nVaroitus! Poistettua reseptiä ei voida palauttaa.", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                if (recipeService.removeRecipe(recipe, userService.getLoggedIn())) {
                    updateRecipeList();
                    updateIngredientList();
                    defaultMode();
                } else {
                    errorLabel.setText("Virhe! Reseptin poistaminen epäonnistui.");
                } 
            }                   
        });
                 
        recipeButtons.getChildren().clear();      
        recipeButtons.getChildren().addAll(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
    
        updateIngredientList(recipe);
    }
    
    public void updateRecipeList() {
        recipeListItems.clear();
        List<Recipe> allRecipes = recipeService.getAllRecipesByUser(userService.getLoggedIn());
        if (allRecipes != null) {
            for (Recipe recipe : allRecipes) {
                recipeListItems.add(recipe);
            }
            Collections.sort(recipeListItems);            
        }
    } 
    
    public void updateIngredientList() {
        ingredientListItems.clear();
    }
    
    public void updateIngredientList(Recipe recipe) {
        ingredientListItems.clear();
        List<Ingredient> ingredients = ingredientService.getIngredients(recipe);
        for (Ingredient ingredient : ingredients) {
            ingredientListItems.add(ingredient);
        }
        Collections.sort(ingredientListItems);
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
}