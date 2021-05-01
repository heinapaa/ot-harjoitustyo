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
        
        User u1 = new User("testaaja1");
        User u2 = new User("testaaja2");
        
        userDao.create(u1);
        userDao.create(u2);
        
        Recipe r1 = new Recipe("resepti1", 2, "kasvis", u1);
        Recipe r2 = new Recipe("resepti2", 4, "kasvis", u2);
        
        recipeDao.create(r1);
        recipeDao.create(r2);
        
        Ingredient i1 = new Ingredient(0, "aines1", 1, "kg", recipeDao.findByName("resepti1"));
        Ingredient i2 = new Ingredient(0, "aines2", 4, "dl", recipeDao.findByName("resepti2"));
        
        ingredientDao.create(i1);
        ingredientDao.create(i2);

        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao);
        this.ingredientService = new IngredientService(recipeDao, ingredientDao, validator);
    }
    
    @Test
    public void canGetAllIngredientsByRecipe() {
        List<Ingredient> ingredients = ingredientService.getIngredients("resepti1");
        assertEquals(1, ingredients.size());
        assertTrue(ingredients.get(0).getName().equals("aines1"));
        assertTrue(ingredients.get(0).getUnit().equals("kg"));
        assertTrue(ingredients.get(0).getAmount() == 1);
    }
    
    @Test
    public void canGetOneIngredient() {
        Ingredient ingredient = ingredientService.getIngredient("resepti1", "aines1");
        assertTrue(ingredient.getName().equals("aines1"));
        assertTrue(ingredient.getUnit().equals("kg"));
        assertTrue(ingredient.getAmount() == 1);        
    }
    
}
