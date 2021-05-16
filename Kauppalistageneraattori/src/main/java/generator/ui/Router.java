package generator.ui;

import generator.dao.*;
import generator.dao.file.*;
import generator.dao.sql.*;
import generator.services.*;
import generator.models.Recipe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private Stage recipeStage;
    
    @Override
    public void init() {
        Properties properties = loadProperties();
        List<String> recipeTypes = loadRecipeTypes();        
        String saveTo = properties.getProperty("saveTo", "1");
        String userFile = properties.getProperty("userFile", "users.txt");
        String recipeFile = properties.getProperty("recipeFile", "recipes.txt");
        String ingredientFile = properties.getProperty("ingredientFile", "ingredients.txt");
        String dbName = properties.getProperty("dbName", "database");
        String dbUsername = properties.getProperty("dbUsername", "sa");
        String dbPassword = properties.getProperty("dwPassword", "");
        
        if (saveTo.equals("1")) {
            if (!initializeSQLDaos(dbName, dbUsername, dbPassword)) {
                if (!initializeFileDaos(userFile, recipeFile, ingredientFile)) {
                    terminateApplication();
                }
            }
        } else if (saveTo.equals("2")) {
            if (!initializeFileDaos(userFile, recipeFile, ingredientFile)) {
                if (!initializeSQLDaos(dbName, dbUsername, dbPassword)) {
                    terminateApplication();
                }
            }            
        } else {
            System.out.println("Tarkista asetukset - ensisijaisen tallennustavan on oltava 1 (tietokanta) tai 2 (tiedosto)!");
            terminateApplication();
        }

        try {
            InputValidator validator = new InputValidator(recipeTypes);
            this.userService = new UserService(userDao, validator);
            this.recipeService = new RecipeService(recipeDao, validator);
            this.ingredientService = new IngredientService(ingredientDao, validator);
            this.shoppingListService = new ShoppingListService(ingredientDao);  
            this.logInView = new LogInView(this, userService);
            this.recipeListView = new RecipeListView(this, userService, recipeService, ingredientService, recipeTypes);
            this.ingredientView = new IngredientView(this, ingredientService);
            this.shoppingListView = new ShoppingListView(this, shoppingListService, recipeTypes);            
        } catch (Exception e) {
            System.out.println("Ohjelman käynnistyksessä tapahtui virhe.");
            terminateApplication();
        }

    }
    
    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        mainStage.setWidth(900);
        mainStage.setHeight(300);
        mainStage.setTitle("Kauppalistageneraattori - kirjaudu sisään!");
        
        this.recipeStage = new Stage();
        recipeStage.setWidth(900);
        recipeStage.setHeight(600);
        mainStage.setTitle("Kauppalistageneraattori");
        
        this.shoppingListStage = new Stage();
        shoppingListStage.initModality(Modality.APPLICATION_MODAL);       
        shoppingListStage.setWidth(500);  
        shoppingListStage.setHeight(400); 
        shoppingListStage.setTitle("Ostoslista");        
        
        this.addIngredientStage = new Stage();
        addIngredientStage.initModality(Modality.APPLICATION_MODAL);
        addIngredientStage.setWidth(600);       
        addIngredientStage.setHeight(500);
        addIngredientStage.setTitle("Syötä ainesosa");      
        
        setLogInView();
        mainStage.show();         
    }
    
    public void setLogInView() {
        mainStage.setScene(logInView.create());
        recipeStage.close();
        mainStage.show();
    }
    
    public void setRecipeListView() {
        recipeStage.setScene(recipeListView.create());
        mainStage.close();
        recipeStage.show();
    }
    
    public void openIngredientWindow(Recipe recipe) {    
        addIngredientStage.setScene(ingredientView.create());
        addIngredientStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                closeIngredientWindow(recipe);
            }
        }); 
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
    
    
    
    private InputStream readFileToStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return inputStream;
    }    

    private Properties loadProperties() {
        Properties properties = new Properties();     
        try (InputStream stream = readFileToStream("config.properties")) {
            properties.load(stream);
            stream.close();
            return properties;
        } catch (IOException e) {
            return properties;
        }       
    }
    
    private List<String> loadRecipeTypes() {
        List<String> recipeTypes = new ArrayList<>();
        try {
            InputStream stream = readFileToStream("recipeTypes.txt"); 
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                recipeTypes.add(line);
            }
            br.close();
            isr.close();
            stream.close();
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
    
    private boolean initializeSQLDaos(String dbName, String dbUsername, String dbPassword) {
        try {
            this.userDao = new SQLUserDao(dbName, dbUsername, dbPassword);                   
            this.recipeDao = new SQLRecipeDao(dbName, dbUsername, dbPassword);
            this.ingredientDao = new SQLIngredientDao(dbName, dbUsername, dbPassword);  
            System.out.println("Ohjelma tallentaa tiedot tietokantaan!");  
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Tietojen tallentaminen tietokantaan ei ole mahdollista.");
            return false;
        }
    }
    
    private boolean initializeFileDaos(String userFile, String recipeFile, String ingredientFile) {
        try {
            this.userDao = new FileUserDao(userFile);
            this.recipeDao = new FileRecipeDao(recipeFile, userDao);
            this.ingredientDao = new FileIngredientDao(ingredientFile, recipeDao);   
            System.out.println("Ohjelma tallentaa tiedot tiedostoon!"); 
            return true;
        } catch (IOException e) {
            System.out.println("Tietojen tallentaminen tiedostoon ei ole mahdollista.");
            return false;
        }
    }
    
    private void terminateApplication() {
        System.out.println("Ohjelma sulkeutuu.");
        System.exit(0);        
    }
}
