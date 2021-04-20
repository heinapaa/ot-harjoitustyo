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
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GeneratorUI extends Application {
    
    private RecipeService recipeService;
    
    @Override
    public void init() throws Exception {
        UserDao userDao = new FileUserDao();
        RecipeDao recipeDao = new FileRecipeDao(userDao);
        IngredientDao ingredientDao = new FileIngredientDao(recipeDao);
        this.recipeService = new RecipeService(userDao, recipeDao, ingredientDao);
        recipeService.login("heinapaa");
    }    
    
    public ListView updateRecipeList() {
        ListView updatedRecipeList = new ListView();
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        for (Recipe recipe : allRecipes) {
            updatedRecipeList.getItems().add(new Label(recipe.getName()));
        }
        return updatedRecipeList;
    }
    
    public ListView updateIngredientList(String recipeName) {
        ListView updatedIngredientList = new ListView();
        List<Ingredient> ingredients = recipeService.getIngredients(recipeName);
        for (Ingredient ingredient : ingredients) {
            updatedIngredientList.getItems().add(new Label(ingredient.toString()));
        }        
        return updatedIngredientList;
    }
          

    @Override
    public void start(Stage window) {
        window.setTitle("Kauppalistageneraattori");
        
        BorderPane pane = new BorderPane();
        
        HBox topButtons = new HBox();
        topButtons.setSpacing(10);
        
        Button lisaaResepti = new Button("Lisää resepti");
        Button uusiOstoslista = new Button("Uusi ostoslista");
        
        topButtons.getChildren().add(lisaaResepti);
        topButtons.getChildren().add(uusiOstoslista);
        
        SplitPane splitPane = new SplitPane();

        ListView recipeList = updateRecipeList();    
        recipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);        
        
        ListView ingredientList = new ListView();
        
        recipeList.setOnMouseClicked(event -> {
            Label selectedRecipe = (Label) recipeList.getSelectionModel().getSelectedItem();
            splitPane.getItems().set(1, updateIngredientList(selectedRecipe.getText()));
        });        
        
        splitPane.getItems().addAll(recipeList, ingredientList);   
        
        Text recipeText = new Text("Recipe info");      
        Button editRecipe = new Button("Muokkaa reseptiä");
        Button deleteRecipe = new Button("Poista resepti"); 
        
        Text ingredientText = new Text("Ingredient info");         
        Button editIngredient = new Button("Muokkaa ainesosaa");
        Button deleteIngredient = new Button("Poista aineosa"); 
        
        VBox infoBox = new VBox();
        
        HBox recipeInfo = new HBox();
        HBox ingredientInfo = new HBox();
        
        recipeInfo.getChildren().add(recipeText);
        recipeInfo.getChildren().add(editRecipe);
        recipeInfo.getChildren().add(deleteRecipe);
        
        ingredientInfo.getChildren().add(ingredientText);         
        ingredientInfo.getChildren().add(editIngredient);
        ingredientInfo.getChildren().add(deleteIngredient);    
        
        infoBox.getChildren().add(recipeInfo);
        infoBox.getChildren().add(ingredientInfo);        
        
        pane.setTop(topButtons);
        pane.setCenter(splitPane);
        pane.setRight(infoBox);
        
        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(GeneratorUI.class);
    }
    
}
