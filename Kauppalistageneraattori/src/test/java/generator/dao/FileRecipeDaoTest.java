package generator.dao;

import generator.dao.file.FileRecipeDao;
import generator.domain.FakeUserDao;
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

public class FileRecipeDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();       
    
    File recipeFile;
    UserDao userDao;
    RecipeDao recipeDao;
    
    @Before
    public void setUp() throws Exception {
        recipeFile = testFolder.newFile("testfile_recipes.txt");  
        this.userDao = new FakeUserDao();
        userDao.create(new User("testaaja"));
        
        try (FileWriter file = new FileWriter(recipeFile.getAbsolutePath())) {
            file.write("1;;eka;;4;;kasvis;;testaaja\n");
        }
        
        recipeDao = new FileRecipeDao(recipeFile.getAbsolutePath(), userDao);          
    }
    
    @Test
    public void ingredientsAreReadCorrectly() {
        List<Recipe> recipes = recipeDao.findAll();
        assertEquals(1, recipes.size());
        Recipe recipe= recipes.get(0);
        //assertEquals(1, recipe.getId());
        assertEquals("eka", recipe.getName());
        assertEquals(4, recipe.getPortion());
        assertEquals(userDao.findByUsername("testaaja"), recipe.getOwner());
    }    
    
    @Test
    public void recipesAreCreatedCorrectly() {
        recipeDao.create(new Recipe("toka", 10, "kasvis", userDao.findByUsername("testaaja")));
        List<Recipe> recipes = recipeDao.findAll();
        assertEquals(2, recipes.size());
    }   
    
    @Test
    public void recipesAreRemovedCorrectly() {
        List<Recipe> recipes = recipeDao.findAll();
        Recipe recipe= recipes.get(0);
        recipeDao.remove(recipe);
        List<Recipe> remaining = recipeDao.findAll();
        assertEquals(0, remaining.size());
    }   
    
    @Test
    public void recipesCanBeCorrectlySearchedByNameAndUser() {
        assertNotNull(recipeDao.findByNameAndUser("eka", userDao.findByUsername("testaaja")));
        assertNull(recipeDao.findByNameAndUser("feikkiresepti", userDao.findByUsername("testaaja")));
        assertNull(recipeDao.findByNameAndUser("eka", null));        
    }
    
    @Test
    public void recipesCanBeCorrectlySearchedByType() {
        recipeDao.create(new Recipe("toka", 10, "kala", userDao.findByUsername("testaaja")));
        List<Recipe> recipes = recipeDao.findByTypeAndUser("kasvis", userDao.findByUsername("testaaja"));
        assertEquals(1, recipes.size());
    }
    
    @Test
    public void recipesCanBeCorrectlySearchedByUser() {
        List<Recipe> testaajaRecipes = recipeDao.findByUser(userDao.findByUsername("testaaja"));
        assertEquals(1, testaajaRecipes.size());
        User feikkitestaaja = new User("feikkitestaaja");
        userDao.create(feikkitestaaja);
        List<Recipe> feikkitestaajaRecipes = recipeDao.findByUser(feikkitestaaja);
        assertEquals(0, feikkitestaajaRecipes.size());
    }
        
    
    @Test
    public void recipIsUpdatedCorrectly() {
        recipeDao.update("uusiEka", 1, "kala", recipeDao.findByNameAndUser("eka", userDao.findByUsername("testaaja")));
        Recipe newRecipe = recipeDao.findByNameAndUser("uusiEka", userDao.findByUsername("testaaja"));
        Recipe oldRecipe = recipeDao.findByNameAndUser("eka", userDao.findByUsername("testaaja"));
        assertNotNull(newRecipe);
        assertNull(oldRecipe);  
        assertEquals(1, newRecipe.getPortion());
        assertEquals("kala", newRecipe.getType());
    }     
    
    @After
    public void tearDown() {
        recipeFile.delete();
    }
    
}