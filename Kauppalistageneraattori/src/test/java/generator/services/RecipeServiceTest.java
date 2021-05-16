package generator.services;

import generator.models.*;
import generator.dao.*;

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
    public void setUp() {
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();
        
        User u1 = new User("tester1");
        User u2 = new User("tester2");
        
        userDao.create(u1);
        userDao.create(u2);
        
        Recipe r1 = new Recipe("recipe1", 2, "kasvis", u1);
        Recipe r2 = new Recipe("recipe2", 4, "kala", u2);
        
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient("ingredient1", 1, "kg", r1);
        Ingredient i2 = new Ingredient("ingredient2", 4, "dl", r2);
        
        ingredientDao.create(i1);
        ingredientDao.create(i2);
              
        List<String> recipeTypes = new ArrayList<>();          
        recipeTypes.add("kala");
        recipeTypes.add("liha");
        recipeTypes.add("kasvis");
        recipeTypes.add("makea");                    

        InputValidator validator = new InputValidator(recipeTypes);       
        this.userService = new UserService(userDao, validator);
        this.recipeService = new RecipeService(recipeDao, validator);
        
        userService.logout();
    }
    
    @Test
    public void noRecipesWithoutLogin() {
        List<Recipe> recipes = recipeService.getAllRecipesByUser(userService.getLoggedIn());
        assertTrue(recipes.isEmpty());
    }
    
    @Test
    public void listConstainsRecipesAfterLogin() {
        userService.login("tester1");
        List<Recipe> recipes = recipeService.getAllRecipesByUser(userService.getLoggedIn());
        assertEquals(1, recipes.size());        
    }
    
    @Test
    public void recipeOwnerCanAccessRecipesCorrectly() {
        userService.login("tester1");
        assertNotNull(recipeService.getRecipe("recipe1", userService.getLoggedIn()));
        assertNull(recipeService.getRecipe("fakeRecipe", userService.getLoggedIn()));
    }

    @Test
    public void recipeCanBeAdded() {
        userService.login("tester1");
        recipeService.createRecipe("resepti3", "2", "kasvis", userService.getLoggedIn());
        assertTrue(recipeService.getRecipe("resepti3", userService.getLoggedIn()) != null);       
    }
    
    @Test
    public void trueWhenRecipeIsAdded() {
        userService.login("tester1");
        assertTrue(recipeService.createRecipe("resepti3", "2", "kasvis", userService.getLoggedIn()));
    }
    
    @Test
    public void recipeNotAddedWithBadInputs() {
        userService.login("tester1");
        recipeService.createRecipe(";;", "1", "kasvis", userService.getLoggedIn());
        recipeService.createRecipe("nimi", "1", "hyvä ruoka", userService.getLoggedIn());    
        recipeService.createRecipe("toinenNimi", null, "kasvis", userService.getLoggedIn());
        assertNull(recipeService.getRecipe(";;", userService.getLoggedIn()));  
        assertNull(recipeService.getRecipe("nimi", userService.getLoggedIn())); 
        assertNull(recipeService.getRecipe("toinenNimi", userService.getLoggedIn())); 
    }    
    
    @Test
    public void falseWhenRecipeNotAdded() {
        userService.login("tester1");
        assertFalse(recipeService.createRecipe(";;", "1", "kasvis", userService.getLoggedIn()));
    }    
    
    @Test
    public void recipeCanBeRemoved() {
        userService.login("tester1");
        recipeService.removeRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), userService.getLoggedIn());
        assertNull(recipeService.getRecipe("recipe1", userService.getLoggedIn()));
    }
    
    @Test
    public void recipeCanNotBeRemovedWithoutUser() {
        userService.login("tester1");
        assertFalse(recipeService.removeRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), null));
        assertNotNull(recipeService.getRecipe("recipe1", userService.getLoggedIn()));        
    }
    
    @Test
    public void recipeCanBeUpdated() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), "newName", "1", "kasvis", userService.getLoggedIn());
        assertNotNull(recipeService.getRecipe("newName", userService.getLoggedIn()));
    }
    
    @Test
    public void recipeCanNotBeUpdatedWithBadNewName() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), "newName;;", "1", "kasvis", userService.getLoggedIn());
        assertNull(recipeService.getRecipe("newName;;", userService.getLoggedIn()));  
        assertNotNull(recipeService.getRecipe("recipe1", userService.getLoggedIn()));       
    }
    
    @Test
    public void recipeCanNotBeUpdatedWithEmptyNewName() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), null, "2", "kasvis", userService.getLoggedIn());
        assertNull(recipeService.getRecipe("newName;;", userService.getLoggedIn()));  
        assertNotNull(recipeService.getRecipe("recipe1", userService.getLoggedIn()));       
    }   
    
    @Test
    public void recipeCanNotBeUpdatedWithUsedNewName() {
        userService.login("tester1");
        recipeService.createRecipe("newRecipe", "1", "kala", userService.getLoggedIn());
        assertFalse(recipeService.updateRecipe(recipeDao.findByNameAndUser("newRecipe", userService.getLoggedIn()), "recipe1", "1", "kala", userService.getLoggedIn()));
        assertNotNull(recipeService.getRecipe("newRecipe", userService.getLoggedIn())); 
        assertEquals(2, recipeService.getRecipe("recipe1", userService.getLoggedIn()).getPortion());        
        assertEquals("kasvis", recipeService.getRecipe("recipe1", userService.getLoggedIn()).getType());
    }     
    
    @Test (expected = IllegalArgumentException.class)
    public void badNewPortionThrowsIllegalArgumentException() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), "newName", "kolme", "kasvis", userService.getLoggedIn());        
    }     
    
    @Test
    public void recipeCanNotBeUpdatedWithEmptyNewPortion() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), "newName", null, "kasvis", userService.getLoggedIn());
        assertEquals(2, recipeService.getRecipe("recipe1", userService.getLoggedIn()).getPortion());        
    }    
    
    @Test
    public void recipeCanNotBeUpdatedWithBadNewType() {
        userService.login("tester1");
        recipeService.updateRecipe(recipeDao.findByNameAndUser("recipe1", userService.getLoggedIn()), "newName;;", "1", "jäätelö", userService.getLoggedIn()); 
        assertEquals("kasvis", recipeService.getRecipe("recipe1", userService.getLoggedIn()).getType());         
    }
    
    @Test
    public void recipeCanNotBeUpdatedWithEmptyNewType() {
        userService.login("tester1");
        assertEquals("kasvis", recipeService.getRecipe("recipe1", userService.getLoggedIn()).getType());       
    }    
    
}
