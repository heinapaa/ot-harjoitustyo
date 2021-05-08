package generator.domain;

import generator.services.InputValidator;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
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
      
        List<String> recipeTypes = new ArrayList<>();          
        recipeTypes.add("kala");
        recipeTypes.add("liha");
        recipeTypes.add("kasvis");
        recipeTypes.add("makea");                   

        InputValidator validator = new InputValidator(recipeTypes);  
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
        assertFalse(validator.isValidUserName(null));        
        assertFalse(validator.isValidUserName("hh"));
        assertFalse(validator.isValidUserName(""));
        assertFalse(validator.isValidUserName("   "));
        assertFalse(validator.isValidUserName("  h  h     "));
        assertFalse(validator.isValidUserName(" h h h "));     
        assertFalse(validator.isValidUserName("Teuvo Testaaja"));   
        assertFalse(validator.isValidUserName("test;;aaja"));
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
        assertFalse(validator.isValidRecipeName(null));        
        assertFalse(validator.isValidRecipeName(""));
        assertFalse(validator.isValidRecipeName("   "));
        assertFalse(validator.isValidRecipeName(" r r r "));   
        assertFalse(validator.isValidRecipeName("rese;;pti"));      
    }
    
    @Test
    public void validRecipePortionIsValid() {
        assertTrue(validator.isValidRecipePortion("3"));
        assertTrue(validator.isValidRecipePortion("14"));         
    }
    
    @Test
    public void invalidRecipePortionIsInvalid() {
        assertFalse(validator.isValidRecipePortion(null));        
        assertFalse(validator.isValidRecipePortion(""));
        assertFalse(validator.isValidRecipePortion("  "));  
        assertFalse(validator.isValidRecipePortion("  8   3 ")); 
        assertFalse(validator.isValidRecipePortion(" 3 ")); 
        assertFalse(validator.isValidRecipePortion("2;;"));          
    } 
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidRecipePortionThrowsRightException1() {
        validator.isValidRecipePortion("annos");              
    }    
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidRecipePortionThrowsRightException2() {
        validator.isValidRecipePortion("2.0");              
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidRecipePortionThrowsRightException3() {
        validator.isValidRecipePortion("2,0");              
    }    
              
    @Test
    public void validRecipeTypeIsValid() {
        assertTrue(validator.isValidRecipeType("liha")); 
        assertTrue(validator.isValidRecipeType("kala")); 
        assertTrue(validator.isValidRecipeType("kasvis")); 
        assertTrue(validator.isValidRecipeType("makea"));         
    }
    
    @Test
    public void invalidRecipeTypeIsInvalid() {
        assertFalse(validator.isValidRecipeType(null));        
        assertFalse(validator.isValidRecipeType(";;"));
        assertFalse(validator.isValidRecipeType("kala;;"));        
        assertFalse(validator.isValidRecipeType(""));
        assertFalse(validator.isValidRecipeType("  "));  
        assertFalse(validator.isValidRecipeType("2"));
        assertFalse(validator.isValidRecipeType("juusto")); 
        assertFalse(validator.isValidRecipeType("/liha"));
        assertFalse(validator.isValidRecipeType(" liha"));         
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
        assertFalse(validator.isValidIngredientName(null));        
        assertFalse(validator.isValidIngredientName(""));
        assertFalse(validator.isValidIngredientName("   "));
        assertFalse(validator.isValidIngredientName(" a a "));  
        assertFalse(validator.isValidIngredientName("aines;;osa"));
    }
    
    @Test
    public void validIngredientAmountIsValid() {
        assertTrue(validator.isValidIngredientAmount("3"));
        assertTrue(validator.isValidIngredientAmount("14")); 
        assertTrue(validator.isValidIngredientAmount("14.7"));      
    }
    
    @Test
    public void invalidIngredientAmountIsInvalid() {
        assertFalse(validator.isValidIngredientAmount(null));        
        assertFalse(validator.isValidIngredientAmount(""));
        assertFalse(validator.isValidIngredientAmount("  "));  
        assertFalse(validator.isValidIngredientAmount(" 3 "));
        assertFalse(validator.isValidIngredientAmount("3;;"));       
        assertFalse(validator.isValidIngredientAmount("  8   . 8"));         
        assertFalse(validator.isValidIngredientAmount("  8   3 "));         
    } 
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidIngredientAmountThrowsRightException1() {
        validator.isValidIngredientAmount("määrä");                   
    } 
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidIngredientAmountThrowsRightException2() {
        validator.isValidIngredientAmount("14,7");                
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
        assertFalse(validator.isValidIngredientUnit(null));        
        assertFalse(validator.isValidIngredientUnit("kappale"));
        assertFalse(validator.isValidIngredientUnit("kpl;;"));        
        assertFalse(validator.isValidIngredientUnit(""));      
        assertFalse(validator.isValidIngredientUnit(" "));    
        assertFalse(validator.isValidIngredientUnit(" g"));        
    }
}
