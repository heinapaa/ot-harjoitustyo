package generator.domain;

import generator.services.IngredientService;
import generator.services.InputValidator;
import generator.models.User;
import generator.models.Unit;
import generator.models.Recipe;
import generator.models.Ingredient;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientServiceTest {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;  
    private IngredientService ingredientService;
    private User u1;
    private User u2;
    private Recipe r1;
    private Recipe r2;
    
    @Before
    public void setUp() {
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();
        
        this.u1 = new User("tester1");
        this.u2 = new User("tester2");
        userDao.create(u1);
        userDao.create(u2);
        
        this.r1 = new Recipe("recipeWithIngredient", 2, "kasvis", u1);
        this.r2 = new Recipe("recipeWithoutIngredient", 4, "kasvis", u2);
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient("ingredient1", 1, "kg", r1);
        ingredientDao.create(i1);
        
        List<String> recipeTypes = new ArrayList<>();          
        recipeTypes.add("kala");
        recipeTypes.add("liha");
        recipeTypes.add("kasvis");
        recipeTypes.add("makea");        

        InputValidator validator = new InputValidator(recipeTypes);
        this.ingredientService = new IngredientService(ingredientDao, validator);
    }
    
    @Test
    public void canTestForExistence() {
        assertTrue(ingredientService.ingredientExists(recipeDao.findByNameAndUser("recipeWithIngredient", u1), "ingredient1"));
    }
    
    @Test
    public void canAddIngredient() {
        ingredientService.addIngredient(r1, "newIngredient", "kpl", "4");
        assertTrue(ingredientService.ingredientExists(r1, "newIngredient"));  
    }    
    
    @Test
    public void cantAddIngredientWithSameRecipeAndSameName() {
        ingredientService.addIngredient(r1, "ingredient1", "kpl", "3");
        List<Ingredient> recipes = ingredientDao.findByRecipe(r1);
        assertEquals(1, recipes.size());
    }
    
    @Test
    public void canAddIngredientWithDifferentRecipeAndSameName() {
        ingredientService.addIngredient(r2, "ingredient1", "kg", "1");
        assertTrue(ingredientService.ingredientExists(r2, "ingredient1"));  
    } 
    
    @Test
    public void canRemoveIngredient() {
        List<Ingredient> ingredients = ingredientService.getIngredients(r1);
        Ingredient ingredient = ingredients.get(0);
        ingredientService.removeIngredient(r1, ingredient);
        assertFalse(ingredientService.ingredientExists(r1, "ingredient2"));
    }
    
    @Test
    public void canGetAllIngredientsByRecipe() {
        ingredientService.addIngredient(r1, "ingredient2", "kpl", "3");        
        List<Ingredient> ingredients = ingredientService.getIngredients(r1);
        assertEquals(2, ingredients.size());
        assertTrue(ingredients.get(0).getName().equals("ingredient1"));
        assertTrue(ingredients.get(0).getUnit() == Unit.KG);
        assertTrue(ingredients.get(0).getAmount() == 1);
        assertTrue(ingredients.get(1).getName().equals("ingredient2"));
        assertTrue(ingredients.get(1).getUnit().equals(Unit.KPL));
        assertTrue(ingredients.get(1).getAmount() == 3);        
    }
}
