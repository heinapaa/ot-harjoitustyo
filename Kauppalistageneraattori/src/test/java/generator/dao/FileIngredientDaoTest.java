package generator.dao;

import generator.dao.file.FileIngredientDao;
import generator.models.Ingredient;
import generator.models.Recipe;
import generator.models.User;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class FileIngredientDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();    
  
    File ingredientFile;  
    RecipeDao recipeDao;
    IngredientDao ingredientDao;  
    
    private User u1;
    private User u2;
    private Recipe r1;
    private Recipe r2;
    private Ingredient i1;
    private Ingredient i2;
    private Ingredient i3;
    
    @Before
    public void setUp() throws Exception {
        this.ingredientFile = testFolder.newFile("testfile_ingredients.txt");  
        this.recipeDao = new FakeRecipeDao();
        
        this.u1 = new User("tester1");
        this.u2 = new User("tester2");
        this.r1 = new Recipe(1, "recipe1", 3, "kasvis", u1);
        this.r2 = new Recipe(2, "recipe2", 5, "liha", u2);
        this.i1 = new Ingredient("ingredient1", 3, "kpl", r1);
        this.i2 = new Ingredient("ingredient2", 10, "kpl", r1);
        this.i3 = new Ingredient("ingredient2", 10, "kpl", r2);        
        
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        try (FileWriter file = new FileWriter(ingredientFile.getAbsolutePath())) {
            file.write("ingredient1;;3.0;;kpl;;1\n");
        }
        
        ingredientDao = new FileIngredientDao(ingredientFile.getAbsolutePath(), recipeDao);              
    }
    
    @Test
    public void ingredientsAreReadCorrectly() {
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(1, ingredients.size());
        assertEquals(i1, ingredients.get(0));
    }    
    
    @Test
    public void ingredientsAreCreatedCorrectly() {
        ingredientDao.create(i2);
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(2, ingredients.size());
        assertTrue(ingredients.contains(i2));
    } 

    @Test
    public void ingredientsAreRemovedCorrectly() {
        assertTrue(ingredientDao.remove(i1));
        List<Ingredient> remaining = ingredientDao.findAll();
        assertFalse(remaining.contains(i1));
    } 
    
    @Test
    public void ingredientIsReadCorrectlyByNameAndRecipe() {
        ingredientDao.create(i2);
        ingredientDao.create(i3);
        assertEquals(i2, ingredientDao.findByNameAndRecipe("ingredient2", r1));
    }    

    @Test
    public void ingredientsAreReadCorrectlyByRecipe() {
        assertTrue(ingredientDao.create(i3));
        List<Ingredient> ingredients = ingredientDao.findByRecipe(r1);
        assertEquals(1, ingredients.size());
        assertEquals(i1, ingredients.get(0));
    }   
    
    @Test
    public void ingredientsAreRemovedCorrectlyByRecipe() {
        ingredientDao.create(i2);
        ingredientDao.create(i3);   
        assertEquals(3, ingredientDao.findAll().size());
        assertTrue(ingredientDao.removeByRecipe(r1));
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(1, ingredients.size());
        assertEquals(i3, ingredients.get(0));       
    }  
    
    @After
    public void tearDown() {
        ingredientFile.delete();
    }    
}