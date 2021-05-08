package generator.ui;

import generator.dao.*;
import generator.dao.file.*;
import generator.dao.sql.*;
import generator.services.*;
import generator.models.Recipe;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Router extends Application {

    private Stage mainStage;
    public Stage shoppingListStage;
    private Stage addIngredientStage;   
    
    private UserDao userDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;

    private UserService userService;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private ShoppingListService shoppingListService;
 
    private LogInView logInView;    
    private RecipeListView recipeListView;
    private IngredientView ingredientView;
    private ShoppingListView shoppingListView;
    
    @Override
    public void init() {
        Properties properties = loadProperties();
        List<String> recipeTypes = loadRecipeTypes();          
        String userFile = properties.getProperty("userFile", "users.txt");
        String recipeFile = properties.getProperty("recipeFile", "recipes.txt");
        String ingredientFile = properties.getProperty("ingredientFile", "ingredients.txt");
        String dbName = properties.getProperty("dbName", "database");
        String dbUsername = properties.getProperty("dbUsername", "sa");
        String dbPassword = properties.getProperty("dwPassword", "");
        
        try {
            this.userDao = new SQLUserDao(new SQLUserConnection(dbName, dbUsername, dbPassword));                   
            this.recipeDao = new SQLRecipeDao(new SQLRecipeConnection(dbName, dbUsername, dbPassword));
            this.ingredientDao = new SQLIngredientDao(new SQLIngredientConnection(dbName, dbUsername, dbPassword));            
        } catch (ClassNotFoundException | SQLException e) {
            try {
                this.userDao = new FileUserDao(userFile);
                this.recipeDao = new FileRecipeDao(recipeFile, userDao);
                this.ingredientDao = new FileIngredientDao(ingredientFile, recipeDao);                
            } catch (IOException ioe) {
                System.out.println("Tietojen tallentaminen ei onnistu!");
            }
        }

        InputValidator validator = new InputValidator(recipeTypes);
        this.userService = new UserService(userDao, validator);
        this.recipeService = new RecipeService(recipeDao, ingredientDao, validator);
        this.ingredientService = new IngredientService(ingredientDao, validator);
        this.shoppingListService = new ShoppingListService(ingredientDao);  
        this.logInView = new LogInView(this, userService);
        this.recipeListView = new RecipeListView(this, userService, recipeService, ingredientService, recipeTypes);
        this.ingredientView = new IngredientView(this, ingredientService);
        this.shoppingListView = new ShoppingListView(this, shoppingListService, recipeTypes);
    }
    
    @Override
    public void start(Stage stage) {
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
    
    private Properties loadProperties() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();        
        try (InputStream stream = loader.getResourceAsStream("config.properties")) {
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            return properties;
        }       
    }
    
    private List<String> loadRecipeTypes() {
        List<String> recipeTypes = new ArrayList<>();
        try (Scanner tiedostonLukija = new Scanner(Paths.get("recipeTypes.txt"))) {
            while (tiedostonLukija.hasNextLine()) {
                String rivi = tiedostonLukija.nextLine();
                recipeTypes.add(rivi);
            }  
        } catch (Exception e) {
            System.out.println("Kustomoitujen reseptityyppien hakeminen epäonnistui. Ohjelma käyttää oletustyyppejä.\n");
        }
        if (recipeTypes.isEmpty()) {
            recipeTypes = setDefaultRecipeTypes();
        }
        return recipeTypes;
    }
    
    private List<String> setDefaultRecipeTypes() {
        List<String> recipeTypes = new ArrayList<>();
        recipeTypes.add("kala");
        recipeTypes.add("liha");
        recipeTypes.add("kasvis");
        recipeTypes.add("makea");            
        return recipeTypes;
    }
}
