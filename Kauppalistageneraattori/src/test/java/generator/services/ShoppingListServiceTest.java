package generator.services;

import generator.dao.*;
import generator.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class ShoppingListServiceTest {
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();      

    private ShoppingListService sls; 
    private IngredientDao ingredientDao;
    
    @Before
    public void setUp() { 
        this.ingredientDao = new FakeIngredientDao();
        this.sls = new ShoppingListService(ingredientDao);
    }
    
    @Test
    public void listCreatedCorrectly1() {
        RecipeDao recipeDao = new FakeRecipeDao();
        Recipe r1 = new Recipe(1, "r1", 1, "kasvis", null);
        Recipe r2 = new Recipe(2, "r2", 2, "kala", null);
        Recipe r3 = new Recipe(3, "r3", 3, "makea", null);
        recipeDao.create(r1);
        recipeDao.create(r2);
        recipeDao.create(r3);
        Ingredient ingredient1 = new Ingredient("a", 100, "kpl", recipeDao.findById(1));
        Ingredient ingredient2 = new Ingredient("v", 100, "l", recipeDao.findById(2));  
        Ingredient ingredient3 = new Ingredient("w", 100, "kg", recipeDao.findById(3)); 
        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);
        ingredientDao.create(ingredient3);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipeDao.findById(1));
        recipeList.add(recipeDao.findById(2));
        recipeList.add(recipeDao.findById(3));
        String shoppingList = sls.createShoppingList(recipeList);   
        String s = "a, 100.0 kpl\n" +
                "v, 100.0 l\n" +
                "w, 100.0 kg\n";
        assertEquals(s, shoppingList);
    }  

    
    @Test
    public void listCreatedCorrectly2() {
        String shoppingList = sls.createShoppingList(null);   
        assertEquals("", shoppingList);
    }    
    
    @Test
    public void listCreatedCorrectly3() {
        List<Recipe> recipeList = new ArrayList<>();        
        String shoppingList = sls.createShoppingList(recipeList);   
        assertEquals("", shoppingList);
    } 
    
    @Test
    public void listCreatedCorrectly4() {
        RecipeDao recipeDao = new FakeRecipeDao();
        Recipe r1 = new Recipe(1, "r1", 1, "kasvis", null);
        Recipe r2 = new Recipe(2, "r2", 2, "kala", null);
        Recipe r3 = new Recipe(3, "r3", 3, "makea", null);
        recipeDao.create(r1);
        recipeDao.create(r2);
        recipeDao.create(r3);
        Ingredient ingredient1 = new Ingredient("a", 100, "kpl", recipeDao.findById(1));
        Ingredient ingredient2 = new Ingredient("a", 100, "l", recipeDao.findById(2));  
        Ingredient ingredient3 = new Ingredient("a", 100, "kg", recipeDao.findById(3)); 
        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);
        ingredientDao.create(ingredient3);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipeDao.findById(1));
        recipeList.add(recipeDao.findById(2));
        recipeList.add(recipeDao.findById(3));
        String shoppingList = sls.createShoppingList(recipeList);   
        String s = "a, 100.0 kg\n" +
                "a, 100.0 kpl\n" +
                "a, 100.0 l\n";
        assertEquals(s, shoppingList);
    }      

    @Test
    public void listSavedToFile() throws IOException {
        String fileName = "shoppingList.txt";
        File file = testFolder.newFile(fileName);          
        String list = "test list";
        sls.saveToFile(list, file);
        assertTrue(file.exists());
        String savedText = "";
        Scanner fileReader = new Scanner(Paths.get(file.getAbsolutePath()));
        while (fileReader.hasNextLine()) {
            savedText = savedText + fileReader.nextLine() + "\n";
        } 
        assertEquals(list.strip(), savedText.strip());
        file.delete();        
    }
    
    @Test
    public void listSavedToFileCorrectly() throws IOException {
        String fileName = "shoppingList.txt";
        File file = testFolder.newFile(fileName);          
        String list = "a, 100.0 kpl\n" +
                "v, 100.0 l\n" +
                "w, 100.0 kg\n";
        sls.saveToFile(list, file);
        assertTrue(file.exists());
        String savedText = "";
        Scanner fileReader = new Scanner(Paths.get(file.getAbsolutePath()));
        while (fileReader.hasNextLine()) {
            savedText = savedText + fileReader.nextLine() + "\n";
        } 
        assertEquals(list.strip(), savedText.strip());
        file.delete();        
    }    
    
    @Test
    public void emptyListSavedToFileCorrectly() throws IOException {
        String fileName = "shoppingList.txt";
        File file = testFolder.newFile(fileName);          
        String list = "";
        sls.saveToFile(list, file);
        assertTrue(file.exists());
        String savedText = "";
        Scanner fileReader = new Scanner(Paths.get(file.getAbsolutePath()));
        while (fileReader.hasNextLine()) {
            savedText = savedText + fileReader.nextLine() + "\n";
        } 
        assertEquals(list.strip(), savedText.strip());
        file.delete();
    }  
    
}
