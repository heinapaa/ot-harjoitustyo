package generator.dao;

import generator.dao.FakeSQLRecipeConnection;
import generator.dao.FakeSQLIngredientConnection;
import generator.models.Ingredient;
import generator.models.Recipe;
import generator.models.User;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SQLIngredientConnectionTest {

    private FakeSQLIngredientConnection conn;
    private FakeSQLRecipeConnection recipeConn;    
    private Recipe r1;
    private Recipe r2;
    private Recipe r3;
    private Ingredient i11;
    private Ingredient i12;    
    private Ingredient i21;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        this.conn = new FakeSQLIngredientConnection();
        this.recipeConn = new FakeSQLRecipeConnection();

        recipeConn.createRecipeTable();
        this.r1 = new Recipe(1, "recipe1", 1, "kasvis", new User("tester1"));
        this.r2 = new Recipe(2, "recipe2", 2, "liha", new User("tester1"));
        this.r3 = new Recipe(3, "recipe2", 1, "makea", new User("tester2"));
        
        this.i11 = new Ingredient("ingredient1", 1, "kpl", r1);
        this.i12 = new Ingredient("ingredient2", 10, "kg", r1);
        this.i21 = new Ingredient("ingredient1", 1, "kpl", r2);

        conn.createIngredientTable();
    }
    
    @Test
    public void canAddIngredients() throws SQLException {
        recipeConn.insertRecipe(r1.getName(), r1.getPortion(), r1.getType(), r1.getOwner().getUsername());        
        assertTrue(conn.insertIngredient(i11.getName(), i11.getAmount(), i11.getUnit().toString(), r1.getId()));
        assertEquals(1, conn.selectAllIngredients().size());
        assertEquals(i11, conn.selectAllIngredients().get(0));
    } 
    
    @Test
    public void canChooseIngredientsByNameAndRecipe() throws SQLException {
        populateTable();
        assertEquals(i11, conn.selectOneIngredientByNameAndRecipe("ingredient1", r1.getId()));
    }
    
    @Test
    public void canChooseAllIngredients() throws SQLException {
        populateTable();
        List<Ingredient> ingredients = conn.selectAllIngredients();
        assertNotNull(ingredients);
        assertEquals(3, ingredients.size());
        assertEquals(i11, ingredients.get(0));
        assertEquals(i12, ingredients.get(1));
        assertEquals(i21, ingredients.get(2));
    }
    
    @Test
    public void canChooseAllIngredientsByRecipe() throws SQLException {
        populateTable();
        List<Ingredient> ingredients = conn.selectAllIngredientsByRecipe(r1.getId());
        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());
        assertEquals(i11, ingredients.get(0));
        assertEquals(i12, ingredients.get(1));
    }
    
    @Test
    public void canDeleteIngredientByNameAndRecipe() throws SQLException {
        populateTable();
        assertEquals(3, conn.selectAllIngredients().size());
        assertTrue(conn.deleteIngredient("ingredient1", r1.getId()));
        List<Ingredient> ingredients = conn.selectAllIngredients();
        assertEquals(2, ingredients.size());
        assertNotEquals(i11, ingredients.get(0));
        assertNotEquals(i11, ingredients.get(1));
    }
    
    @Test
    public void canDeleteAllIngredientsByRecipe() throws SQLException {
        populateTable();
        assertEquals(3, conn.selectAllIngredients().size());
        assertTrue(conn.deleteAllIngredientsByRecipe(r1.getId()));
        List<Ingredient> ingredients = conn.selectAllIngredients();
        assertEquals(1, ingredients.size());
        assertEquals(i21, ingredients.get(0));       
    }
    
    private void populateTable() throws SQLException {
        recipeConn.insertRecipe("recipe1", 1, "kasvis", "tester1");
        recipeConn.insertRecipe("recipe2", 2, "liha", "tester1");
        recipeConn.insertRecipe("recipe2", 1, "makea", "tester2");        
        conn.insertIngredient("ingredient1", 1, "kpl", r1.getId());
        conn.insertIngredient("ingredient2", 10, "kg", r1.getId());
        conn.insertIngredient("ingredient1", 1, "kpl", r2.getId());
    } 
    
    @After
    public void tearDown() throws SQLException {
        conn.closeConnection();
    }  
    
    
}
