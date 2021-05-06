package generator.domain;

import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
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
    
    /**
     * Konstruktori
     * @param recipeDao     RecipeDao-rajapinnan toteuttava olio, joka vastaa reseptien tallentamisesta.
     * @param ingredientDao IngredientDao-rajapinnan toteuttava olio, joka vastaa ainesosien tallentamisesta.
     * @param validator     InputValidator-olio, joka vastaa syötteiden validoinnista.
     */
    
    public RecipeService(RecipeDao recipeDao, IngredientDao ingredientDao, InputValidator validator) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.validator = validator;
    }    
    
    /**
     * Metodi luo annettujen syötteiden perusteella uuden reseptin.
     * 
     * @param name      Syötteenä annettu nimi
     * @param portion   Syötteenä annettu annoskoko
     * @param type      Syötteenä annettu reseptin tyyppi
     * @param user      Käyttäjä, johon luotava resepti liitetään
     * 
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
        } else if (getRecipe(nm, user) != null) {
            return false;
        }
        Recipe newRecipe = new Recipe(nm, Integer.parseInt(pn), tp, user);           
        return recipeDao.create(newRecipe);
    }
    
    /**
     * Metodi poistaa halutun reseptin.
     * 
     * @param recipe    poistettava resepti
     * @param user      käyttäjä, johon poistettava resepti liittyy
     * 
     * @see             generator.dao.RecipeDao#remove(generator.domain.Recipe)
     * 
     * @return          true jos reseptin poistaminen onnistuu, muuten false
     */
    
    public boolean removeRecipe(Recipe recipe, User user) {
        if (getRecipe(recipe.getName(), user) == null) {
            return false;
        }
        if (!ingredientDao.removeByRecipe(recipe)) {
            return false;
        }
        return recipeDao.remove(recipe);
    }    
    
    /**
     * Metodi päivittää annettujen syötteiden perusteella halutun reseptin tietoja.
     * 
     * @param recipe        Päivitettävä resepti
     * @param newName       Syötteenä annettu uusi nimi
     * @param newPortion    Syötteenä annettu uusi annoskoko
     * @param newType       Syötteenä annettu uusi reseptityyppi.
     * @param user          Käyttäjä, johon päivitettävä resepti liittyy
     * 
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
        } else if (!recipe.getName().equals(newnm) && getRecipe(newnm, user) != null) {
            return false;
        }              
        return recipeDao.update(newnm, Integer.parseInt(newpn), newtp, recipe);
    }
    
    /**
     * Metodi palauttaa kaikki tiettyyn käyttäjään liittyvät reseptit.
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
     * Metodi hakee reseptin tietyn käyttäjän ja syötteenä annetun nimen perusteella.
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
    
    private boolean validInputs(String recipeName, String recipePortion, String recipeType) {
        if (!validator.isValidRecipeName(recipeName) || !validator.isValidRecipePortion(recipePortion) || !validator.isValidRecipeType(recipeType)) {
            return false;
        }
        return true;
    }
}