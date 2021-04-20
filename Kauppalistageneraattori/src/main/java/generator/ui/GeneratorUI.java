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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);            
        
        splitPane.getItems().addAll(recipeList, ingredientList);  
        
        // Resepti-infolaatikko
        
        Label recipeTitle = new Label("Valittu resepti");
        recipeTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));        
        VBox recipeInfoTitles = new VBox();
        VBox recipeInfoContents = new VBox();
        HBox recipeInfoButtons = new HBox();
        
        Text recipeInfoTitle1 = new Text("Nimi:");
        Text recipeInfo1 = new Text(""); 
        Text recipeInfoTitle2 = new Text("Annoskoko:");
        Text recipeInfo2 = new Text(""); 
        
        Button editRecipe = new Button("Muokkaa reseptiä");
        Button deleteRecipe = new Button("Poista resepti");        
        
        recipeInfoTitles.getChildren().add(recipeInfoTitle1);
        recipeInfoTitles.getChildren().add(recipeInfoTitle2);        

        recipeInfoContents.getChildren().add(recipeInfo1);
        recipeInfoContents.getChildren().add(recipeInfo2);
        
        recipeInfoButtons.getChildren().add(editRecipe);
        recipeInfoButtons.getChildren().add(deleteRecipe);
        
        // Ainesosa-infolaatikko

        Label ingredientTitle = new Label("Valittu ainesosa:");
        ingredientTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        VBox ingredientInfoTitles = new VBox();
        VBox ingredientInfoContents = new VBox();
        HBox ingredientInfoButtons = new HBox();
        
        Text ingredientInfoTitle1 = new Text("Nimi:");
        Text ingredientInfo1 = new Text(""); 
        Text ingredientInfoTitle2 = new Text("Määrä:");
        Text ingredientInfo2 = new Text(""); 
        
        Button editIngredient = new Button("Muokkaa ainesosaa");
        Button deleteIngredient = new Button("Poista aineosa");       
        
        ingredientInfoTitles.getChildren().add(ingredientInfoTitle1);
        ingredientInfoTitles.getChildren().add(ingredientInfoTitle2);        

        ingredientInfoContents.getChildren().add(ingredientInfo1);
        ingredientInfoContents.getChildren().add(ingredientInfo2);
        
        ingredientInfoButtons.getChildren().add(editIngredient);
        ingredientInfoButtons.getChildren().add(deleteIngredient);
        
        VBox infoBox = new VBox();
        infoBox.setSpacing(20);
        
        HBox recipeInfo = new HBox();
        recipeInfo.setSpacing(10);
        HBox ingredientInfo = new HBox();
        ingredientInfo.setSpacing(10);
        
        recipeInfo.getChildren().add(recipeInfoTitles);
        recipeInfo.getChildren().add(recipeInfoContents);        
                   
        ingredientInfo.getChildren().add(ingredientInfoTitles);
        ingredientInfo.getChildren().add(ingredientInfoContents);         
        
        infoBox.getChildren().add(recipeTitle);
        infoBox.getChildren().add(recipeInfo);
        infoBox.getChildren().add(recipeInfoButtons);
        infoBox.getChildren().add(ingredientTitle);
        infoBox.getChildren().add(ingredientInfo);  
        infoBox.getChildren().add(ingredientInfoButtons);     
        infoBox.setMinWidth(300);
        
        pane.setTop(topButtons);
        pane.setCenter(splitPane);
        pane.setRight(infoBox);
        
        //Listeners
        
        recipeList.setOnMouseClicked(event -> {
            Label selectedRecipe = (Label) recipeList.getSelectionModel().getSelectedItem();
            String recipeName = selectedRecipe.getText();
            splitPane.getItems().set(1, updateIngredientList(recipeName));
            recipeInfo1.setText(recipeName);
            recipeInfo2.setText(String.valueOf(recipeService.getRecipe(recipeName).getServing()));
        });                
        
        ingredientList.setOnMouseClicked(event -> {
            
            ingredientInfo1.setText("testi");
            
            /*
            Label selectedRecipe = (Label) recipeList.getSelectionModel().getSelectedItem();  
            String recipeName = selectedRecipe.getText();            
            Label selectedIngredient = (Label) ingredientList.getSelectionModel().getSelectedItem();
            String ingredientStr = selectedIngredient.getText();
            String[] ingredientSplit = ingredientStr.split(",");
            String ingredientName = ingredientSplit[0];
            ingredientInfo1.setText(recipeName);
            ingredientInfo2.setText(String.valueOf(recipeService.getIngredient(recipeName, ingredientName).getAmount() + " " + recipeService.getIngredient(recipeName, ingredientName).getUnit() ));
            */
        });         
        
        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.setWidth(900);
        window.show();
    }

    public static void main(String[] args) {
        launch(GeneratorUI.class);
    }
    
}
