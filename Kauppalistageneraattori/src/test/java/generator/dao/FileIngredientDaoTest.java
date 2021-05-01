package generator.dao;

import generator.domain.FakeIngredientDao;
import generator.domain.FakeRecipeDao;
import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    
    @Before
    public void setUp() throws Exception {
        ingredientFile = testFolder.newFile("testfile_ingredients.txt");  
        this.recipeDao = new FakeRecipeDao();
        recipeDao.create(new Recipe("testiresepti", 3, "kasvis", null));
        int indeksi = recipeDao.findByName("testiresepti").getId();
        
        try (FileWriter file = new FileWriter(ingredientFile.getAbsolutePath())) {
            file.write("1;eka;3.0;kpl;"+indeksi+"\n");
        }
        
        ingredientDao = new FileIngredientDao(ingredientFile.getAbsolutePath(), recipeDao);              
    }
    
    @Test
    public void ingredientsAreReadCorrectly() {
        List<Ingredient> ingredients = ingredientDao.findAll();
        assertEquals(1, ingredients.size());
        Ingredient ingredient= ingredients.get(0);
        assertEquals(1, ingredient.getId());
        assertEquals("eka", ingredient.getName());
        assertEquals(3, ingredient.getAmount(), 0);
    }   
    
    @Test
    public void ingredientsAreCreatedCorrectly() {
        ingredientDao.create(new Ingredient("toka", 10, "kpl"));
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
    
    @After
    public void tearDown() {
        ingredientFile.delete();
    }    
}
