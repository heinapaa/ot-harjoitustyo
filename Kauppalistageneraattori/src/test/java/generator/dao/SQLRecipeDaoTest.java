package generator.dao;

import generator.dao.sql.SQLIngredientDao;
import generator.dao.sql.SQLRecipeDao;
import generator.models.User;
import generator.models.Recipe;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SQLRecipeDaoTest {

    private FakeSQLRecipeConnection conn;
    private RecipeDao recipeDao;
    private User u1;
    private Recipe r1;
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        this.u1 = new User("tester");          
        this.r1 = new Recipe(1, "recipe1", 1, "kasvis", u1); 
        this.conn = new FakeSQLRecipeConnection(); 
        this.recipeDao = new SQLRecipeDao(conn);
        IngredientDao ingredientDao = new SQLIngredientDao(new FakeSQLIngredientConnection());
        conn.insertRecipe("recipe1", 1, "kasvis", "tester");
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
    public void tearDown() throws SQLException {
        conn.closeConnection();
    }    
}
