package generator.dao;

import generator.domain.FakeRecipeDao;
import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
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
    
    User u1;
    User u2;
    Recipe r1;
    Recipe r2;
    /*
    @Before
    public void setUp() throws Exception {
        this.ingredientFile = testFolder.newFile("testfile_ingredients.txt");  
        this.recipeDao = new FakeRecipeDao();
        
        this.u1 = new User("tester1");
        this.u2 = new User("tester2");
        this.r1 = new Recipe(1, "recipe1", 3, "kasvis", u1);
        this.r2 = new Recipe(2, "recipe2", 5, "liha", u2);
        
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
        Ingredient ingredient= ingredients.get(0);
        assertEquals("ingredient1", ingredient.getName());
        assertEquals(3, ingredient.getAmount(), 0);
    }   
    
    @Test
    public void ingredientsAreCreatedCorrectly() {
        ingredientDao.create(new Ingredient("ingredient2", 10, "kpl", r1));
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(2, ingredients.size());
    }     
    
    @Test
    public void ingredientsAreRemovedCorrectly() {
        List<Ingredient> ingredients = ingredientDao.findAll();
        Ingredient ingredient= ingredients.get(0);
        ingredientDao.remove(ingredient);
        List<Ingredient> remaining = ingredientDao.findAll();
        assertEquals(0, remaining.size());
    } 
    
    @Test
    public void ingredientAreReadCorrectlyByRecipe() {
        ingredientDao.create(new Ingredient("ingredient2", 10, "kpl", r2));
        List<Ingredient> ingredients = ingredientDao.findByRecipe(r1);
        assertEquals(1, ingredients.size());
        assertTrue(ingredients.get(0).getName().equals("ingredient1"));
    }
    
    @Test
    public void ingredientsAreRemovedCorrectlyByRecipe() {
        ingredientDao.create(new Ingredient("ingredient2", 10, "kpl", r2));
        ingredientDao.removeByRecipe(r1);
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(1, ingredients.size());
        assertTrue(ingredients.get(0).getName().equals("ingredient2"));        
    }
    
    @After
    public void tearDown() {
        ingredientFile.delete();
    }    
    */
}
