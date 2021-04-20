package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeServiceRecipeTest {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;
    private RecipeService recipeService;
    
    @Before
    public void setUp() {
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();
        
        User u1 = new User("testaaja1");
        User u2 = new User("testaaja2");
        
        userDao.create(u1);
        userDao.create(u2);
        
        Recipe r1 = new Recipe("resepti1", 2, u1);
        Recipe r2 = new Recipe("resepti2", 4, u2);
        
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient(0, "aines1", 1, "kg", r1);
        Ingredient i2 = new Ingredient(0, "aines2", 4, "dl", r2);
        
        ingredientDao.create(i1);
        ingredientDao.create(i2);
        
        this.recipeService = new RecipeService(userDao, recipeDao, ingredientDao);
    }
    
    @Test
    public void noRecipesWithoutLogin() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        assertEquals(0, recipes.size());
    }
    
    @Test
    public void listConstainsRecipesAfterLogin() {
        recipeService.login("testaaja1");
        List<Recipe> recipes = recipeService.getAllRecipes();
        assertEquals(1, recipes.size());        
    }
    
    @Test
    public void existQueryWorksForOwner() {
        recipeService.login("testaaja1");
        assertTrue(recipeService.recipeExists("resepti1"));
        assertFalse(recipeService.recipeExists("fakeNews"));
    }


    @Test
    public void recipeCanBeAdded() {
        recipeService.login("testaaja1");
        recipeService.createRecipe("resepti3", 2);
        assertTrue(recipeService.recipeExists("resepti3"));       
    }
    
    @Test
    public void recipeCanBeRemoved() {
        recipeService.login("testaaja1");
        recipeService.removeRecipe("resepti1");
        assertFalse(recipeService.recipeExists("resepti1"));
    }
    
}