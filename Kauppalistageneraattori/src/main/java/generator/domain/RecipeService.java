package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Reseptien käsittelyyn liittyvästä sovelluslogiikasta vastaava luokka.
 */


public class RecipeService {
    
    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final InputValidator validator;    
    
    public RecipeService(RecipeDao recipeDao, IngredientDao ingredientDao, InputValidator validator) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }    
    
    /**
     * Luo annettujen syötteiden perusteella uuden reseptin.
     * 
     * @param name      Syötteenä annettu nimi
     * @param portion   Syötteenä annettu annoskoko
     * @param type      Syötteenä annettu reseptin tyyppi
     * @param user      Käyttäjä, johon luotava resepti liitetään
     * 
     * @see             generator.domain.RecipeService#recipeExists(java.lang.String, generator.domain.User) 
     * @see             generator.dao.RecipeDao#create(generator.domain.Recipe) 
     * 
     * @return          true jos uuden reseptin luonti onnistuu, muuten false.
     */
    
    public boolean createRecipe(String name, String portion, String type, User user) {
        String nm = name.strip();
        String pn = StringUtils.deleteWhitespace(portion);
        String tp = type.strip();
        if (!validInputs(nm, pn, tp)) {
            return false;
        } else if (recipeExists(nm, user)) {
            return false;
        } else {
            Recipe newRecipe = new Recipe(nm, Integer.parseInt(pn), tp, user);
            try {
                recipeDao.create(newRecipe);   
            } catch (Exception e) {
                return false;
            }            
        }
        return true;
    }
    
    /**
     * Poistaa halutun reseptin.
     * 
     * @param recipe    poistettava resepti
     * @param user      käyttäjä, johon poistettava resepti liittyy
     * 
     * @see             generator.dao.RecipeDao#remove(generator.domain.Recipe)
     * 
     * @return          true jos reseptin poistaminen onnistuu, muuten false
     */
    
    public boolean removeRecipe(Recipe recipe, User user) {
        if (!recipeExists(recipe.getName(), user)) {
            return false;
        }
        try {
            ingredientDao.removeByRecipe(recipe);
            recipeDao.remove(recipe);            
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Päivittää annettujen syötteiden perusteella reseptin tietoja.
     * 
     * @param recipe        Päivitettävä resepti
     * @param newName       Syötteenä annettu uusi nimi
     * @param newPortion    Syötteenä annettu uusi annoskoko
     * @param newType       Syötteenä annettu uusi reseptityyppi.
     * @param user          Käyttäjä, johon päivitettävä resepti liittyy
     * 
     * @see                 generator.domain.RecipeService#recipeExists(java.lang.String, generator.domain.User) 
     * @see                 generator.dao.RecipeDao#update(java.lang.String, int, java.lang.String, generator.domain.Recipe) 
     * 
     * @return              true jos reseptin tietojen päivitys onnistuu, muuten false
     */
    
    public boolean updateRecipe(Recipe recipe, String newName, String newPortion, String newType, User user) {
        String newnm = newName.strip();
        String newpn = StringUtils.deleteWhitespace(newPortion); 
        String newtp = newType.strip();
        if (!validInputs(newnm, newpn, newtp)) {
            return false;
        } else if (!recipe.getName().equals(newnm) && recipeExists(newnm, user)) {
            return false;
        } else  {
            try {
                recipeDao.update(newnm, Integer.parseInt(newpn), newtp, recipe);                   
            } catch (Exception e) {
                return false;
            }            
        }
        return true;
    }
    
    /**
     * Palauttaa kaikki tiettyyn käyttäjään liittyvät reseptit.
     * 
     * @param user  Käyttäjä, johon reseptit liittyvät
     * 
     * @see         generator.dao.RecipeDao#findAll()
     * 
     * @return      List-rakenne joka sisältää käyttäjään liittyvät reseptit Recipe-luokan olioina, null jos reseptejä ei ole
     */
    
    public List<Recipe> getAllRecipes(User user) {
        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getOwner().equals(user)) {
                returnRecipes.add(recipe);
            }
        }
        if (returnRecipes.size() == 0) {
            return null;
        } else {
            return returnRecipes;
        } 
    }
    
    /**
     * Hakee reseptin tietyn käyttäjän ja syötteenä annetun nimen perusteella.
     * 
     * @param name  Haetun reseptin nimi
     * @param user  Käyttäjä, johon resepti liittyy
     *
     * @see         generator.dao.RecipeDao#findByNameAndUser(java.lang.String, generator.domain.User) 
     * 
     * @return      Haettua vastaava Recipe-luokan olio jos tällainen on olemassa, muuten null
     */
    
    public Recipe getRecipe(String name, User user) {
        String nm = name.strip();
        return recipeDao.findByNameAndUser(nm, user);
    }
    
    /**
     * Tarkistaa tietyn käyttäjän ja syötteenä annetun reseptin nimen perusteella, liittyykö kyseiseen käyttäjään sen niminen resepti.
     * 
     * @param name  Syötteenä annettu reseptin nimi
     * @param user  Käyttäjä, johon resepti liittyy
     * 
     * @see         generator.dao.RecipeDao#findByNameAndUser(java.lang.String, generator.domain.User)
     * 
     * @return      true jos resepti löytyy, muuten false
     */
      
    public boolean recipeExists(String name, User user) {      
        if (recipeDao.findByNameAndUser(name, user) == null) {
            return false;
        }
        return true;
    }
    
    private boolean validInputs(String recipeName, String recipePortion, String recipeType) {
        if (!validator.isValidRecipeName(recipeName) || !validator.isValidRecipePortion(recipePortion) || !validator.isValidRecipeType(recipeType)) {
            return false;
        }
        return true;
    }
}