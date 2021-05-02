package generator.ui;

import generator.domain.Ingredient;
import generator.domain.IngredientService;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import generator.domain.UserService;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private String inputRecipeName;
    private String inputRecipePortion;    
    private TextField inputFieldRecipeName;
    private TextField inputFieldRecipePortion;
    private VBox recipeButtons;

    private final Router router;
    private final UserService userService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    
    private final ObservableList<Recipe> recipeListItems;
    private final ObservableList<String> ingredientListItems;
    private final ListView<Recipe> recipeList;
    private final ListView<String> ingredientList;    
    
    public RecipeListView(Router router, UserService userService, RecipeService recipeService, 
            IngredientService ingredientService) {
        this.router = router;
        this.userService = userService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService; 
        
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
        infoBox.setPadding(new Insets(5, 10, 5, 10));
        infoBox.setMinWidth(300);   
     
        this.pane = new BorderPane();
        pane.setTop(topButtons);
        pane.setCenter(splitPane);
        pane.setBottom(errorLabel); 
        pane.setLeft(infoBox);            
        defaultMode();
        updateRecipeList();     
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
        
        Button editRecipe = new Button("Muokkaa reseptiä");
        editRecipe.setOnMouseClicked(event -> editRecipeMode(recipe));
        recipeButtons.getChildren().clear();
        recipeButtons.getChildren().add(editRecipe);   
    }
    
    public void addRecipeMode() {
        setInputRows();
        errorLabel.setText("");    
        inputFieldRecipeName.setText("");
        inputFieldRecipePortion.setText("");
        recipeTypeComboBox.getSelectionModel().select(null);
        
        Button commitAddRecipe = new Button("Tallenna resepti");
        commitAddRecipe.setOnMouseClicked(event -> {
            String recipeName = inputFieldRecipeName.getText();
            String recipePortion = inputFieldRecipePortion.getText();
            String recipeType = recipeTypeComboBox.getSelectionModel().getSelectedItem().toString();
            
            if (recipeService.createRecipe(recipeName, recipePortion, recipeType, userService.getLoggedIn())) {
                updateRecipeList();
                Recipe recipe = recipeService.getRecipe(recipeName.strip());
                recipeList.getSelectionModel().select(recipe);
                editRecipeMode(recipe);                
            } else {
                errorLabel.setText("Virhe! Reseptin tallennus epäonnistui.");
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

        Button commitEditRecipe = new Button("Tallenna muutokset");
        commitEditRecipe.setOnMouseClicked(event -> {
            String oldRecipeName = recipe.getName();
            String newRecipeName = inputFieldRecipeName.getText();
            String newRecipePortion = inputFieldRecipePortion.getText();
            String newRecipeType = recipeTypeComboBox.getSelectionModel().getSelectedItem().toString();
            if (recipeService.updateRecipe(oldRecipeName, newRecipeName, newRecipePortion, newRecipeType)) {  
                Recipe newRecipe = recipeService.getRecipe(newRecipeName.strip());
                updateRecipeList();
                recipeList.getSelectionModel().select(newRecipe);                         
                infoMode(newRecipe);               
            } else {
                errorLabel.setText("Virhe! Muutosten tallennus epäonnistui.");
            }            
        });
          
        Button cancelEditRecipe = new Button("Peruuta");
        cancelEditRecipe.setOnMouseClicked(event -> infoMode(recipe));
        
        Button addIngredient = new Button("Lisää ainesosa");
        addIngredient.setOnMouseClicked(event -> router.openIngredientWindow(recipe));
        
        Button deleteIngredient = new Button("Poista ainesosa");
        deleteIngredient.setOnMouseClicked(event -> {
            if (!ingredientList.getSelectionModel().getSelectedItem().isBlank()) {
                String ingredientName = ingredientList.getSelectionModel().getSelectedItem().split(",")[0]; 
                if (ingredientService.removeIngredient(recipe.getName(), ingredientName)) {
                    updateIngredientList(recipe);                
                } else {
                    errorLabel.setText("Virhe! Ainesosan poistaminen epäonnistui.");
                }                  
            } else {
                errorLabel.setText("Virhe! Valitse poistettava ainesosa.");
            }
        });
        
        Button deleteRecipe = new Button("Poista resepti");
        deleteRecipe.setOnMouseClicked(event -> {         
            if (recipeService.removeRecipe(recipe.getName())) {
                updateRecipeList();
                updateIngredientList();
                defaultMode();
            } else {
                errorLabel.setText("Virhe! Reseptin poistaminen epäonnistui.");
            }            
        });
                 
        recipeButtons.getChildren().clear();      
        recipeButtons.getChildren().addAll(commitEditRecipe, cancelEditRecipe, addIngredient, deleteIngredient, deleteRecipe);
    
        updateIngredientList(recipe);
    }
    
    public void updateRecipeList() {
        recipeListItems.clear();
        List<Recipe> allRecipes = recipeService.getAllRecipes(userService.getLoggedIn());
        if (!allRecipes.isEmpty()) {
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
        List<Ingredient> ingredients = ingredientService.getIngredients(recipe.getName());
        for (Ingredient ingredient : ingredients) {
            ingredientListItems.add(ingredient.toString());
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