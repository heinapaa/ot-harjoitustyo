package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InputValidatorTest {
    
    private InputValidator validator;
    
    @Before
    public void setUp() {
        UserDao userDao = new FakeUserDao();
        RecipeDao recipeDao = new FakeRecipeDao();
        IngredientDao ingredientDao = new FakeIngredientDao();
        InputValidator validator = new InputValidator(userDao, recipeDao, ingredientDao);
        this.validator = validator;
    }
    
    @Test
    public void validUserNameIsValid() {
        assertTrue(validator.isValidUserName("testaaja"));
        assertTrue(validator.isValidUserName("3380"));
        assertTrue(validator.isValidUserName("/test"));
        assertTrue(validator.isValidUserName("hhh"));
    }
    
    @Test
    public void invalidUserNameIsInvalid() {
        assertFalse(validator.isValidUserName("hh"));
        assertFalse(validator.isValidUserName(""));
        assertFalse(validator.isValidUserName("   "));
        assertFalse(validator.isValidUserName("  h  h     "));
        assertFalse(validator.isValidUserName(" h h h "));     
        assertFalse(validator.isValidUserName("Teuvo Testaaja"));        
    }
    
    @Test
    public void validRecipeNameIsValid() {
        assertTrue(validator.isValidRecipeName("resepti"));
        assertTrue(validator.isValidRecipeName("3380"));
        assertTrue(validator.isValidRecipeName("/test"));
        assertTrue(validator.isValidRecipeName("oikein hyvä resepti"));
    }
    
    @Test
    public void invalidRecipeNameIsInvalid() {
        assertFalse(validator.isValidRecipeName(""));
        assertFalse(validator.isValidRecipeName("   "));
        assertFalse(validator.isValidRecipeName(" r r r "));        
    }
    
    @Test
    public void validRecipePortionIsValid() {
        assertTrue(validator.isValidRecipePortion("3"));
        assertTrue(validator.isValidRecipePortion("14"));        
    }
    
    @Test
    public void invalidRecipePortionIsInvalid() {
        assertFalse(validator.isValidRecipePortion("annos"));
        assertFalse(validator.isValidRecipePortion(""));
        assertFalse(validator.isValidRecipePortion("  "));  
        assertFalse(validator.isValidRecipePortion("2.0"));
        assertFalse(validator.isValidRecipePortion("  8   3 ")); 
        assertFalse(validator.isValidRecipePortion(" 3 "));        
    }    

    @Test
    public void validIngredientNameIsValid() {
        assertTrue(validator.isValidIngredientName("ainesosa"));
        assertTrue(validator.isValidIngredientName("3548"));
        assertTrue(validator.isValidIngredientName("/test"));
        assertTrue(validator.isValidIngredientName("yksi ainesosa"));
    }
    
    @Test
    public void invalidIngredientNameIsInvalid() {
        assertFalse(validator.isValidIngredientName(""));
        assertFalse(validator.isValidIngredientName("   "));
        assertFalse(validator.isValidIngredientName(" a a "));        
    }
    
    @Test
    public void validIngredientAmountIsValid() {
        assertTrue(validator.isValidIngredientAmount("3"));
        assertTrue(validator.isValidIngredientAmount("14")); 
        assertTrue(validator.isValidIngredientAmount("14.7"));        
    }
    
    @Test
    public void invalidIngredientAmountIsInvalid() {
        assertFalse(validator.isValidIngredientAmount("määrä"));
        assertFalse(validator.isValidIngredientAmount(""));
        assertFalse(validator.isValidIngredientAmount("  "));  
        assertFalse(validator.isValidIngredientAmount(" 3 "));
        assertFalse(validator.isValidIngredientAmount("  8   . 8"));         
        assertFalse(validator.isValidIngredientAmount("  8   3 "));    
        assertFalse(validator.isValidIngredientAmount("14,7"));         
    }   
    
    @Test
    public void validIngredientUnitIsValid() {
        assertTrue(validator.isValidIngredientUnit("g"));
        assertTrue(validator.isValidIngredientUnit("kg"));
        assertTrue(validator.isValidIngredientUnit("dl"));
        assertTrue(validator.isValidIngredientUnit("l"));
        assertTrue(validator.isValidIngredientUnit("kpl"));        
    }
    
    public void invalidIngredientUnitIsInvalid() {
        assertFalse(validator.isValidIngredientUnit("kappale"));
        assertFalse(validator.isValidIngredientUnit(""));      
        assertFalse(validator.isValidIngredientUnit(" "));    
        assertFalse(validator.isValidIngredientUnit(" g"));        
    }
}
