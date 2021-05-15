package generator.dao;

import generator.dao.file.FileRecipeDao;
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
    
    private File recipeFile;
    private UserDao userDao;
    private RecipeDao recipeDao;
    
    private User u1;
    private Recipe r1;
    
    @Before
    public void setUp() throws Exception {
        this.u1 = new User("tester");          
        this.r1 = new Recipe(1, "recipe1", 1, "kasvis", u1); 
        
        this.userDao = new FakeUserDao();
        userDao.create(u1);
        
        recipeFile = testFolder.newFile("testfile_recipes.txt");          
        try (FileWriter file = new FileWriter(recipeFile.getAbsolutePath())) {
            file.write("1;;recipe1;;1;;kasvis;;tester\n");
        }
        recipeDao = new FileRecipeDao(recipeFile.getAbsolutePath(), userDao);          
    }
    
    
    @Test
    public void recipesAreReadCorrectly() {
        List<Recipe> recipes = recipeDao.findAll();
        assertEquals(1, recipes.size());
        assertEquals(r1, recipes.get(0));
    }

    @Test
    public void recipesAreCreatedCorrectly() {
        Recipe r2 = new Recipe(2, "recipe2", 10, "kasvis", u1);
        assertTrue(recipeDao.create(r2));
        List<Recipe> recipes = recipeDao.findAll();
        assertEquals(2, recipes.size());
        assertTrue(recipes.contains(r2));
    }   
    
    @Test
    public void recipesAreRemovedCorrectly() {    
        assertTrue(recipeDao.findAll().contains(r1));
        assertTrue(recipeDao.remove(r1));
        assertFalse(recipeDao.findAll().contains(r1));
    }
    
    @Test
    public void recipeCanBeCorrectlySearchedById() {
        assertEquals(r1, recipeDao.findById(1));
    }
    
    @Test
    public void recipesCanBeCorrectlySearchedByNameAndUser() {
        assertNotNull(recipeDao.findByNameAndUser("recipe1",u1));
        assertEquals(r1, recipeDao.findByNameAndUser("recipe1", u1));
        assertNull(recipeDao.findByNameAndUser("fake recipe", u1));
        assertNull(recipeDao.findByNameAndUser("recipe1", new User("fake user")));        
    }
    
    @Test
    public void recipesCanBeCorrectlySearchedByType() {
        Recipe r2 = new Recipe(2, "recipe2", 10, "kala", u1);
        recipeDao.create(r2);
        List<Recipe> recipes = recipeDao.findByTypeAndUser("kasvis", u1);
        assertEquals(1, recipes.size());
        assertEquals(r1, recipes.get(0));
    }
    
    @Test
    public void recipesCanBeCorrectlySearchedByUser() {       
        List<Recipe> u1Recipes = recipeDao.findByUser(u1);
        assertEquals(1, u1Recipes.size());
        User u2 = new User("fake Tester");
        List<Recipe> u2Recipes = recipeDao.findByUser(u2);
        assertEquals(0, u2Recipes.size());
    }
        
    
    @Test
    public void recipIsUpdatedCorrectly() {
        recipeDao.update("new Name", 100, "kala", r1);
        Recipe newRecipe = recipeDao.findByNameAndUser("new Name", u1);
        Recipe oldRecipe = recipeDao.findByNameAndUser("recipe2", u1);
        assertNotNull(newRecipe);
        assertNull(oldRecipe);  
        assertEquals(100, newRecipe.getPortion());
        assertEquals("kala", newRecipe.getType());
    }       
    
    @Test
    public void idIsCreatedCorrectly() {
        User u2 = new User("tester2");
        Recipe r2 = new Recipe("recipe2", 10, "makea", u1);
        Recipe r3 = new Recipe("recipe2", 10, "makea", u2);
        assertEquals(1, recipeDao.findByNameAndUser("recipe1", u1).getId());
        recipeDao.create(r2);
        assertEquals(2, recipeDao.findByNameAndUser("recipe2", u1).getId());
        recipeDao.remove(r1);
        recipeDao.create(r3);
        assertEquals(3, recipeDao.findByNameAndUser("recipe2", u2).getId());
        recipeDao.remove(r2);
        recipeDao.create(r2);
        assertEquals(4, recipeDao.findByNameAndUser("recipe2", u1).getId());  
    }       
    
    @After
    public void tearDown() {
        recipeFile.delete();
    }
    
}