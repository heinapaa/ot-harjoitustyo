package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientServiceTest {
    
    private RecipeDao recipeDao;
    private UserDao userDao;
    private IngredientDao ingredientDao;  
    private IngredientService ingredientService;
    
    @Before
    public void setUp() {
        this.userDao = new FakeUserDao();
        this.recipeDao = new FakeRecipeDao();
        this.ingredientDao = new FakeIngredientDao();
        
        User u1 = new User("tester1");
        User u2 = new User("tester2");
        userDao.create(u1);
        userDao.create(u2);
        
        Recipe r1 = new Recipe("recipeWithIngredient", 2, "kasvis", u1);
        Recipe r2 = new Recipe("recipeWithoutIngredient", 4, "kasvis", u2);
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient(0, "ingredient1", 1, "kg", recipeDao.findByName("recipeWithIngredient"));
        ingredientDao.create(i1);

        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao);
        this.ingredientService = new IngredientService(recipeDao, ingredientDao, validator);
    }
    
    @Test
    public void canTestForExistence() {
        assertTrue(ingredientService.ingredientExists(recipeDao.findByName("recipeWithIngredient"), "ingredient1"));
    }
    
    @Test
    public void canAddIngredient() {
        ingredientService.addIngredient(recipeDao.findByName("recipeWithoutIngredient"), "newIngredient", "kpl", "4");
        assertTrue(ingredientService.ingredientExists(recipeDao.findByName("recipeWithoutIngredient"), "newIngredient"));  
    }    
    
    @Test
    public void cantAddIngredientWithSameRecipeAndSameName() {
        ingredientService.addIngredient(recipeDao.findByName("recipeWithIngredient"), "ingredient1", "kpl", "3");
        List<Ingredient> recipes = ingredientDao.findByRecipe(recipeDao.findByName("recipeWithIngredient"));
        assertEquals(1, recipes.size());
    }
    
    @Test
    public void canAddIngredientWithDifferentRecipeAndSameName() {
        ingredientService.addIngredient(recipeDao.findByName("recipeWithoutIngredient"), "ingredient1", "kg", "1");
        assertTrue(ingredientService.ingredientExists(recipeDao.findByName("recipeWithoutIngredient"), "ingredient1"));  
    } 
    
    @Test
    public void canRemoveIngredient() {
        List<Ingredient> ingredients = ingredientService.getIngredients(recipeDao.findByName("recipeWithIngredient"));
        Ingredient ingredient = ingredients.get(0);
        ingredientService.removeIngredient(recipeDao.findByName("recipeWithIngredient"), ingredient);
        assertFalse(ingredientService.ingredientExists(recipeDao.findByName("recipeWithIngredient"), "ingredient2"));
    }
    
    @Test
    public void canGetAllIngredientsByRecipe() {
        ingredientService.addIngredient(recipeDao.findByName("recipeWithIngredient"), "ingredient2", "kpl", "3");        
        List<Ingredient> ingredients = ingredientService.getIngredients(recipeDao.findByName("recipeWithIngredient"));
        assertEquals(2, ingredients.size());
        assertTrue(ingredients.get(0).getName().equals("ingredient1"));
        assertTrue(ingredients.get(0).getUnit().equals("kg"));
        assertTrue(ingredients.get(0).getAmount() == 1);
        assertTrue(ingredients.get(1).getName().equals("ingredient2"));
        assertTrue(ingredients.get(1).getUnit().equals("kpl"));
        assertTrue(ingredients.get(1).getAmount() == 3);        
    }
}
