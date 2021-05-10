package generator.dao.sql;

import generator.dao.IngredientDao;
import generator.models.Ingredient;
import generator.models.Recipe;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ainesosia tietokantaan tallentava luokka, joka toteuttaa IngredientDao-rajapinnan.
 * 
 */

public class SQLIngredientDao implements IngredientDao {
    
    private final SQLIngredientConnection connection;
    
    /**
     * Konstruktori, joka luo yhteyden ainesosat sisältävään tietokantatauluun.
     * @param conn {@code SQLConnection}-olio, jota käytetään tietokantapyyntöjen toteuttamiseen
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLIngredientConnection#createIngredientTable() 
     */
    
    public SQLIngredientDao(SQLIngredientConnection conn) throws SQLException, ClassNotFoundException {
        this.connection = conn;
        connection.createIngredientTable();
    }
    
    /**
     * Metodi tallentaa uuden ainesosan tiedot tietokantaan.
     * @param ingredient    ainesosa, joka halutaan tallentaa
     * @return true jos ainesosan tietojen tallentaminen onnistuu, muuten false
     * @see generator.dao.sql.connection.SQLIngredientConnection#insertIngredient(java.lang.String, double, java.lang.String, int) 
     */

    @Override
    public boolean create(Ingredient ingredient) {
        try {
            return connection.insertIngredient(ingredient.getName(), ingredient.getAmount(), ingredient.getUnit().toString(), ingredient.getRecipe().getId());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * Metodi poistaa valitun ainesosan tiedot tietokannasta.
     * @param ingredient    ainesosa, joka halutaan poistaa
     * @return true jos ainesosan tietojen poistaminen onnistuu, muuten false
     * @see generator.dao.sql.connection.SQLIngredientConnection#deleteIngredient(java.lang.String, int) 
     */

    @Override
    public boolean remove(Ingredient ingredient) {
        try {
            return connection.deleteIngredient(ingredient.getName(), ingredient.getRecipe().getId());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * Metodi poistaa kaikki tiettyyn reseptiin liittyvät ainesosat tietokannasta.
     * @param recipe    resepti, johon liittyvät ainesosat halutaan poistaa
     * @return true jos ainesosien tietojen poistaminen onnistuu, muuten false
     * @see generator.dao.sql.connection.SQLIngredientConnection#deleteAllIngredientsByRecipe(int) 
     */

    @Override
    public boolean removeByRecipe(Recipe recipe) {
        try {
            return connection.deleteAllIngredientsByRecipe(recipe.getId());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa ainesosan nimen ja reseptin perusteella.
     * @param name  haettavan ainesosan nimi
     * @param recipe    resepti, johon haettava ainesosa liittyy
     * @return Haettava ainesosa {@code Ingredient}-olion muodossa, jos ainesosaa ei löydy niin null
     * @see generator.dao.sql.connection.SQLIngredientConnection#selectOneIngredientByNameAndRecipe(java.lang.String, int) 
     */

    @Override
    public Ingredient findByNameAndRecipe(String name, Recipe recipe) {
        try {
            Ingredient ingredient = connection.selectOneIngredientByNameAndRecipe(name, recipe.getId());
            if (ingredient != null) {
                ingredient.setRecipe(recipe);
            }
            return ingredient;            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipe resepti, johon liittyvät ainesosat halutaan
     * @return Lista, joka sisältää kaikki märiteltyyn reseptiin liittyvät ainesosat {@code Ingredient}-olioina
     * @see generator.dao.sql.connection.SQLIngredientConnection#selectAllIngredientsByRecipe(int) 
     */

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        try {
            return connection.selectAllIngredientsByRecipe(recipe.getId()); 
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Meto hakee ja palauttaa kaikki tallennetut ainesosat.
     * @return Lista, joka sisältää kaikki tallennetut ainesosat {@code Ingredient}-olioina
     * @see generator.dao.sql.connection.SQLIngredientConnection#selectAllIngredients() 
     */

    @Override
    public List<Ingredient> findAll() {
        try {
            return connection.selectAllIngredients();    
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }  
    }
    
}
