package generator.dao.sql;

import generator.models.Recipe;
import generator.models.User;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SQLRecipeConnectionTest {
    
    private FakeSQLRecipeConnection conn;
    private Recipe r1;
    private Recipe r2;
    private Recipe r3;
    private Recipe r4;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        this.conn = new FakeSQLRecipeConnection();
        this.r1 = new Recipe(1, "recipe1", 1, "kasvis", new User("tester1"));
        this.r2 = new Recipe(2, "recipe2", 2, "liha", new User("tester1"));
        this.r3 = new Recipe(3, "recipe2", 1, "makea", new User("tester2"));
        this.r4 = new Recipe(4, "recipe3", 3, "kasvis", new User("tester1"));
        conn.createRecipeTable();
    }
    
    @Test
    public void recipesCanBeAdded() throws SQLException {
        assertTrue(conn.insertRecipe("recipe1", 1, "kasvis", "tester1"));
        assertEquals(1, conn.selectAllRecipes().size());
        assertEquals(r1, conn.selectAllRecipes().get(0));       
    }
    
    @Test
    public void canSelectRecipeById() throws SQLException {
        populateTable();
        assertEquals(r1, conn.selectOneRecipeById(1));
        assertEquals(r2, conn.selectOneRecipeById(2));
        assertEquals(r3, conn.selectOneRecipeById(3));        
    }
    
    @Test
    public void canSelectRecipeByNameAndUser() throws SQLException {
        populateTable();        
        assertNotNull(conn.selectOneRecipeByNameAndUser("recipe2", "tester2"));
        assertEquals(r3, conn.selectOneRecipeByNameAndUser("recipe2", "tester2"));
    } 
    
    @Test
    public void canSelectRecipesByUser() throws SQLException {
        populateTable();        
        List<Recipe> recipes = conn.selectAllRecipesByUser("tester1");
        assertNotNull(recipes);
        assertEquals(3, recipes.size());
        assertEquals(r1, recipes.get(0));
        assertEquals(r2, recipes.get(1));
        assertEquals(r4, recipes.get(2));        
    }   
    
    @Test
    public void canSelectRecipesByTypeAndUser() throws SQLException {
        populateTable();        
        List<Recipe> recipes = conn.selectAllRecipesByTypeAndUser("kasvis", "tester1");
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
        assertEquals(r1, recipes.get(0));
        assertEquals(r4, recipes.get(1));
    }    
    
    @Test
    public void canSelectAllRecipes() throws SQLException {
        populateTable();        
        List<Recipe> recipes = conn.selectAllRecipes();
        assertNotNull(recipes);
        assertEquals(4, recipes.size());
    } 

    @Test
    public void canUpdateRecipe() throws SQLException {
        populateTable();        
        assertTrue(conn.updateRecipe("newName", 10, "makea", 1));
        Recipe updatedRecipe = conn.selectOneRecipeById(1);
        assertEquals("newName", updatedRecipe.getName());
        assertEquals(10, updatedRecipe.getPortion());
        assertEquals("makea", updatedRecipe.getType());
    } 
    
    private void populateTable() throws SQLException {
        conn.insertRecipe("recipe1", 1, "kasvis", "tester1");
        conn.insertRecipe("recipe2", 2, "liha", "tester1");
        conn.insertRecipe("recipe2", 1, "makea", "tester2"); 
        conn.insertRecipe("recipe3", 3, "kasvis", "tester1");
    } 
    
    @After
    public void tearDown() throws SQLException {
        conn.closeConnection();
    } 
    
}
