package generator.ui;

import generator.dao.*;
import generator.domain.*;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GeneratorUI extends Application {
    
    private UserService userService;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private ShoppingListService shoppingListService;
    private ObservableList<String> recipeListItems;
    private ObservableList<String> ingredientListItems;   
    
    private Button addRecipe;
    private Button newShoppingList;
    private Button changeUser;
    private ListView<String> recipeList;
    private ListView<String> ingredientList;
  
    private ObservableList<String> remainingRecipesItems;
    private ObservableList<String> chosenRecipesItems;

    
    @Override
    public void init() throws Exception {
        /*Properties properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream("/config.properties");
        properties.load(inputStream)
        String userFile = properties.getProperty("userFile");
        String recipeFile = properties.getProperty("recipeFile");
        String ingredientFile = properties.getProperty("ingredientFile");*/
        
        String userFile = "users.txt";
        String recipeFile = "recipes.txt";
        String ingredientFile = "ingredients.txt";
        
        UserDao userDao = new FileUserDao(userFile);
        RecipeDao recipeDao = new FileRecipeDao(recipeFile, userDao);
        IngredientDao ingredientDao = new FileIngredientDao(ingredientFile, recipeDao);
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
        Collections.sort(recipeListItems);
    }
    
    public void updateIngredientList(String recipeName) {
        ingredientListItems.clear();
        List<Ingredient> ingredients = ingredientService.getIngredients(recipeName);
        for (Ingredient ingredient : ingredients) {
            ingredientListItems.add(ingredient.toString());
        }
        Collections.sort(ingredientListItems);
    }  
    
    public void updateRemainingList() {
        remainingRecipesItems.clear();
        for (String recipeName : recipeListItems) {
            remainingRecipesItems.add(recipeName);
        }        
    }
    
    public void updateChosenList() {
        chosenRecipesItems.clear();        
    }      
    
    public void enableClicking(){
        //recipeList.setMouseTransparent(false);
        //addRecipe.setMouseTransparent(false);
        //changeUser.setMouseTransparent(false);
        //newShoppingList.setMouseTransparent(false);  
    }    
    
    public void disableClicking(){
        //recipeList.setMouseTransparent(true);
        //addRecipe.setMouseTransparent(true);
        //changeUser.setMouseTransparent(true);
        //newShoppingList.setMouseTransparent(true);  
    }   

    @Override
    public void start(Stage mainWindow) {        
        this.recipeListItems = FXCollections.observableArrayList();
        this.recipeList = new ListView<>(recipeListItems);    
        recipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
        
        this.ingredientListItems = FXCollections.observableArrayList();         
        this.ingredientList = new ListView<>(ingredientListItems);       
        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
        
        this.remainingRecipesItems = FXCollections.observableArrayList();        
        this.chosenRecipesItems = FXCollections.observableArrayList();                  

        Button logIn = new Button("Kirjaudu");
        Button createUser = new Button("Luo uusi käyttäjä");  
        
        Button addRecipe = new Button("Lisää resepti"); 
        Button newShoppingList = new Button("Uusi ostoslista");
        Button changeUser = new Button("Kirjaudu ulos");        
        
        Button commitAddRecipe = new Button("Tallenna resepti");
        Button cancelAddRecipe = new Button("Peruuta");    
        
        Button editRecipe = new Button("Muokkaa reseptiä");  
        
        Button commitEditRecipe = new Button("Tallenna muutokset");
        Button cancelEditRecipe = new Button("Peruuta"); 
        Button addIngredient = new Button("Lisää ainesosa");
        Button deleteIngredient = new Button("Poista ainesosa");        
        Button deleteRecipe = new Button("Poista resepti");
        
        Button commitAddIngredient = new Button("Tallenna ainesosa");
        Button cancelAddIngredient = new Button("Peruuta");  
       
        Button addToShoppingList = new Button("Lisää ostoslistalle");
        Button removeFromShoppingList = new Button("Poista ostoslistalta");          
        Button generateShoppingList = new Button("Luo ostoslista");
        Button cancelShoppingList = new Button("Sulje");
        
        
        
        LogInView logInView = new LogInView();
        RecipeListView recipeListView = new RecipeListView();
        IngredientView ingredientView = new IngredientView();
        ShoppingListView shoppingListView = new ShoppingListView();            
        
        Scene logInScene = new Scene(logInView.set(logIn, createUser));
        Scene recipeScene = new Scene(recipeListView.set(addRecipe, newShoppingList, changeUser, commitAddRecipe, recipeList, ingredientList));   
        Scene ingredientScene = new Scene(ingredientView.set(commitAddIngredient, cancelAddIngredient)); 
        Scene chooseRecipeScene = new Scene(shoppingListView.set(addToShoppingList, removeFromShoppingList, generateShoppingList, 
                cancelShoppingList, remainingRecipesItems, chosenRecipesItems));
        Scene shoppingListScene = new Scene(shoppingListView.setShoppingListView(""));
        
        Stage ingredientWindow = new Stage();
        ingredientWindow.initModality(Modality.APPLICATION_MODAL);
        ingredientWindow.setScene(ingredientScene);
        ingredientWindow.setWidth(300);       
        ingredientWindow.setTitle("Syötä ainesosa");
        
        Stage shoppingListWindow = new Stage();
        shoppingListWindow.initModality(Modality.APPLICATION_MODAL);       
        shoppingListWindow.setScene(shoppingListScene);
        shoppingListWindow.setWidth(500);  
        shoppingListWindow.setHeight(400); 
        shoppingListWindow.setTitle("Ostoslista");
    
        //LOG IN LISTENERS
        
        logIn.setOnMouseClicked(event -> {
            if (userService.login(logInView.getUserNameInput())) {
                mainWindow.setScene(recipeScene);
                updateRecipeList();
            } else {
                logInView.logInFailure();
            }
        });   

        createUser.setOnMouseClicked(event -> {
            if (userService.addNewUser(logInView.getUserNameInput())) {
                mainWindow.setScene(recipeScene);
                updateRecipeList();
            } else {
                logInView.createUserFailure();
            }
        });  
        
        // RECIPE VIEW BASIC LISTENERS

        recipeList.setOnMouseClicked(event -> {       
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            Recipe recipe = recipeService.getRecipe(recipeName);
            recipeListView.setInfoDisplay(recipe, editRecipe);   
            updateIngredientList(recipeName);
        });          
        
        addRecipe.setOnMouseClicked(event -> {
            disableClicking();
            recipeListView.setAddDisplay(commitAddRecipe, cancelAddRecipe);
        });
        
        newShoppingList.setOnMouseClicked(event-> {
            shoppingListWindow.show();
        });
        
        changeUser.setOnMouseClicked(event -> {
            mainWindow.setScene(logInScene);
        });  
        
        editRecipe.setOnMouseClicked(event -> {             
            if (recipeList.getSelectionModel().getSelectedItem() != null) {
                disableClicking();
                Recipe recipe = recipeService.getRecipe(recipeList.getSelectionModel().getSelectedItem());            
                recipeListView.setEditDisplay(recipe, commitEditRecipe, cancelEditRecipe, addIngredient, 
                        deleteIngredient, deleteRecipe);
            } else {
                recipeListView.editRecipeFailure();
            }       
        });                       
        
        // ADD RECIPE LISTENERS
        
        commitAddRecipe.setOnMouseClicked(event -> {
            String recipeName = recipeListView.getInputRecipeName();
            String recipePortion = recipeListView.getInputRecipePortion();
            if (recipeService.createRecipe(recipeName, recipePortion, userService.getLoggedIn())) {
                updateRecipeList();
                Recipe recipe = recipeService.getRecipe(recipeName.strip());
                recipeListView.setInfoDisplay(recipe, editRecipe);
            } else {
                recipeListView.createRecipeFailure();
            }
        });          
        
        cancelAddRecipe.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            Recipe recipe = recipeService.getRecipe(recipeName);
            recipeListView.setInfoDisplay(recipe, editRecipe);
            enableClicking();
        });   
        
        // EDIT RECIPE LISTENERS
        
        commitEditRecipe.setOnMouseClicked(event -> {  
            String oldRecipeName = recipeList.getSelectionModel().getSelectedItem();
            String newRecipeName = recipeListView.getInputRecipeName();
            String newRecipePortion = recipeListView.getInputRecipePortion();
            if (recipeService.updateRecipe(oldRecipeName, newRecipeName, newRecipePortion)) {
                updateRecipeList();
                Recipe recipe = recipeService.getRecipe(newRecipeName.strip());
                recipeListView.setInfoDisplay(recipe, editRecipe);
                enableClicking();
            } else {
                recipeListView.commitEditRecipeFailure();
            }       
        });    
        
        cancelEditRecipe.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            Recipe recipe = recipeService.getRecipe(recipeName);
            recipeListView.setInfoDisplay(recipe, editRecipe); 
            enableClicking();
        });   
        
        addIngredient.setOnMouseClicked(event -> {            
            ingredientWindow.show();
        });        
        
        deleteIngredient.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientList.getSelectionModel().getSelectedItem().split(",")[0];        
            if (ingredientService.removeIngredient(recipeName, ingredientName)) {
                updateIngredientList(recipeName);                
            } else {
                recipeListView.deleteIngredientFailure();
            }
        });         
        
        deleteRecipe.setOnMouseClicked(event -> {   
            String recipeName = recipeList.getSelectionModel().getSelectedItem();            
            if (recipeService.removeRecipe(recipeName)) {
                updateRecipeList();      
                recipeListView.setDefaultDisplay();
                enableClicking();
            } else {
                recipeListView.deleteRecipeFailure();
            }
        }); 
        
        // ADD INGREDIENT WINDOW LISTENERS
        
        commitAddIngredient.setOnMouseClicked(event -> {
            String recipeName = recipeList.getSelectionModel().getSelectedItem();
            String ingredientName = ingredientView.getInputIngredientName();
            String ingredientAmount = ingredientView.getInputIngredientAmount();
            String ingredientUnit = ingredientView.getInputIngredientUnit();         
           
            if (ingredientService.addIngredient(recipeName, ingredientName,ingredientUnit, ingredientAmount)) {
                updateIngredientList(recipeName);
                ingredientView.clearView();               
            } else {
                ingredientView.commitCreateIngredientFailure();
            }
        });         

        cancelAddIngredient.setOnMouseClicked(event -> {          
            ingredientWindow.close();
        }); 
        
        // SHOPPING LIST WINDOW LISTENERS      
        
        addToShoppingList.setOnMouseClicked(event -> {
            String chosenRecipe = shoppingListView.getChosenRecipe();             
            if (!chosenRecipe.isEmpty()) {
                remainingRecipesItems.remove(chosenRecipe);
                chosenRecipesItems.add(chosenRecipe);           
            }
        });        
        
        removeFromShoppingList.setOnMouseClicked(event -> {
            String chosenRecipe = shoppingListView.getChosenRecipe();             
            if (!chosenRecipe.isEmpty()) {
                chosenRecipesItems.remove(chosenRecipe);
                remainingRecipesItems.add(chosenRecipe);              
            }
        });         
        
        generateShoppingList.setOnMouseClicked(event -> {
            String shoppingList = shoppingListService.createShoppingList(chosenRecipesItems);
            shoppingListWindow.setScene(new Scene(shoppingListView.setShoppingListView(shoppingList)));
        });          
        
        cancelShoppingList.setOnMouseClicked(event -> {
            shoppingListWindow.close();
        });        

        mainWindow.setScene(logInScene);
        mainWindow.setWidth(900);
        mainWindow.setHeight(600);          
        mainWindow.setTitle("Kauppalistageneraattori");      
        
        mainWindow.show();
    }
        
    public static void main(String[] args) {
        launch(GeneratorUI.class);
    }   

}