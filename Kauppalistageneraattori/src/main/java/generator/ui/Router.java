package generator.ui;

import generator.dao.FileIngredientDao;
import generator.dao.FileRecipeDao;
import generator.dao.FileUserDao;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.domain.IngredientService;
import generator.domain.InputValidator;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import generator.domain.ShoppingListService;
import generator.domain.UserService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Router extends Application {

    private Stage mainStage;
    public Stage shoppingListStage;
    private Stage addIngredientStage;    

    private UserService userService;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private ShoppingListService shoppingListService;
 
    private LogInView logInView;    
    private RecipeListView recipeListView;
    private IngredientView ingredientView;
    private ShoppingListView shoppingListView;
    
    @Override
    public void init() throws Exception {
        String userFile = "users.txt";
        String recipeFile = "recipes.txt";
        String ingredientFile = "ingredients.txt";
        
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();        
        try (InputStream stream = loader.getResourceAsStream("config.properties")) {
            if (stream != null) {
                properties.load(stream);                 
                userFile = properties.getProperty("userFile");
                recipeFile = properties.getProperty("recipeFile");     
                ingredientFile = properties.getProperty("ingredientFile");                
                try {
                    stream.close();
                } catch (Exception e) {
                    System.out.println("Kustomoitujen tiedostosijaintien asetus epäonnistui.");
                }
            }            
        }    

        List<String> recipeTypes = new ArrayList<>();          
        try (InputStream stream = loader.getResourceAsStream("recipeTypes.properties")) {
            if (stream != null) {
                properties.load(stream);                 
                for (String type : properties.getProperty("types").split(",")) {
                    recipeTypes.add(type);
                }
                try {
                    stream.close();
                } catch (Exception e) {
                    System.out.println("Kustomoitujen reseptityyppien asettaminen epäonnistui.");
                }
            }            
        }
        
        if (recipeTypes.size() == 0) {
            recipeTypes.add("kala");
            recipeTypes.add("liha");
            recipeTypes.add("kasvis");
            recipeTypes.add("makea");            
        }
        
        UserDao userDao = new FileUserDao(userFile);
        RecipeDao recipeDao = new FileRecipeDao(recipeFile, userDao);
        IngredientDao ingredientDao = new FileIngredientDao(ingredientFile, recipeDao);
        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao, recipeTypes);
        
        this.userService = new UserService(userDao, validator);
        this.recipeService = new RecipeService(recipeDao, ingredientDao, validator);
        this.ingredientService = new IngredientService(recipeDao, ingredientDao, validator);
        this.shoppingListService = new ShoppingListService(recipeDao, ingredientDao);  
        this.logInView = new LogInView(this, userService);
        this.recipeListView = new RecipeListView(this, userService, recipeService, ingredientService, recipeTypes);
        this.ingredientView = new IngredientView(this, ingredientService);
        this.shoppingListView = new ShoppingListView(this, shoppingListService);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        mainStage.setWidth(900);
        mainStage.setHeight(600);
        mainStage.setTitle("Kauppalistageneraattori");
        
        this.shoppingListStage = new Stage();
        shoppingListStage.initModality(Modality.APPLICATION_MODAL);       
        shoppingListStage.setWidth(500);  
        shoppingListStage.setHeight(400); 
        shoppingListStage.setTitle("Ostoslista");        
        
        this.addIngredientStage = new Stage();
        addIngredientStage.initModality(Modality.APPLICATION_MODAL);
        addIngredientStage.setWidth(300);       
        addIngredientStage.setTitle("Syötä ainesosa");      
        
        setLogInView();
        mainStage.show();         
    }
    
    public void setLogInView() {
        mainStage.setScene(logInView.create());
    }
    
    public void setRecipeListView() {
        mainStage.setScene(recipeListView.create());
    }
    
    public void openIngredientWindow(Recipe recipe) {    
        addIngredientStage.setScene(ingredientView.create());
        ingredientView.addTo(recipe);       
        addIngredientStage.show();
    }
    
    public void closeIngredientWindow(Recipe recipe) {
        addIngredientStage.close();
        recipeListView.editRecipeMode(recipe);
    }
    
    public void openShoppingListWindow(ObservableList<Recipe> currentRecipes) {
        shoppingListStage.setScene(shoppingListView.create());
        shoppingListView.addCurrentRecipes(currentRecipes);
        shoppingListStage.show();        
    }
    
    public void closeShoppingListWindow() {
        shoppingListStage.close();
    }
    
    public static void main(String[] args) {
        launch(Router.class);
    }       
}
