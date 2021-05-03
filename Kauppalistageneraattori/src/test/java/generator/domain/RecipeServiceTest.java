package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeServiceTest {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;
    private UserService userService;
    private RecipeService recipeService;
    
    @Before
    public void setUp() throws Exception {
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();
        
        User u1 = new User("testaaja1");
        User u2 = new User("testaaja2");
        
        userDao.create(u1);
        userDao.create(u2);
        
        Recipe r1 = new Recipe("resepti1", 2, null, u1);
        Recipe r2 = new Recipe("resepti2", 4, null, u2);
        
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient(0, "aines1", 1, "kg", r1);
        Ingredient i2 = new Ingredient(0, "aines2", 4, "dl", r2);
        
        ingredientDao.create(i1);
        ingredientDao.create(i2);
              
        List<String> recipeTypes = new ArrayList<>();          
        recipeTypes.add("kala");
        recipeTypes.add("liha");
        recipeTypes.add("kasvis");
        recipeTypes.add("makea");                    

        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao, recipeTypes);       
        this.userService = new UserService(userDao, validator);
        this.recipeService = new RecipeService(recipeDao, ingredientDao, validator);
    }
    
    @Test
    public void noRecipesWithoutLogin() {
        List<Recipe> recipes = recipeService.getAllRecipes(userService.getLoggedIn());
        assertEquals(0, recipes.size());
    }
    
    @Test
    public void listConstainsRecipesAfterLogin() {
        userService.login("testaaja1");
        List<Recipe> recipes = recipeService.getAllRecipes(userService.getLoggedIn());
        assertEquals(1, recipes.size());        
    }
    
    @Test
    public void existQueryWorksForOwner() {
        userService.login("testaaja1");
        assertTrue(recipeService.recipeExists("resepti1", userService.getLoggedIn()));
        assertFalse(recipeService.recipeExists("fakeNews", userService.getLoggedIn()));
    }

    @Test
    public void recipeCanBeAdded() {
        userService.login("testaaja1");
        recipeService.createRecipe("resepti3", "2", "kasvis", userService.getLoggedIn());
        assertTrue(recipeService.recipeExists("resepti3", userService.getLoggedIn()));       
    }
    
    @Test
    public void recipeCanBeRemoved() {
        userService.login("testaaja1");
        recipeService.removeRecipe(recipeDao.findByNameAndUser("resepti1", userService.getLoggedIn()), userService.getLoggedIn());
        assertFalse(recipeService.recipeExists("resepti1", userService.getLoggedIn()));
    }
    
    @Test
    public void recipeCanBeUpdated() {
        userService.login("testaaja1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("resepti1", userService.getLoggedIn()), "uusiNimi", "1", "kasvis", userService.getLoggedIn());
        assertTrue(recipeService.recipeExists("uusiNimi", userService.getLoggedIn()));
    }
    
}
