package generator.dao.sql;

import generator.dao.IngredientDao;
import generator.models.Ingredient;
import generator.models.Recipe;
import generator.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Ainesosia tietokantaan tallentava luokka, joka toteuttaa IngredientDao-rajapinnan.
 * 
 */

public class SQLIngredientDao extends SQLDao implements IngredientDao {

    private final String createIngredientTable =  "CREATE TABLE IF NOT EXISTS Ingredients (name VARCHAR(255) NOT NULL, amount DOUBLE NOT NULL, unit VARCHAR(255) NOT NULL, recipe_id INT NOT NULL)";             
    private final String insertIngredient = "INSERT INTO Ingredients(name, amount, unit, recipe_id) VALUES(?,?,?,?)"; 
    private final String selectIngredientByNameAndRecipe = "SELECT * FROM Ingredients WHERE name = ? AND recipe_id = ?";  
    private final String selectIngredientsByRecipe = "SELECT * FROM Ingredients WHERE recipe_id = ?";     
    private final String selectAllIngredients = "SELECT Ingredients.*, Recipes.* FROM Ingredients JOIN Recipes ON Ingredients.recipe_id = Recipes.id";    
    private final String deleteIngredientByNameAndRecipe = "DELETE FROM Ingredients WHERE name = ? AND recipe_id = ?";
    private final String deleteIngredientsByRecipe = "DELETE FROM Ingredients WHERE recipe_id = ?";    
    private final String selectIngredientsByRecipes = "SELECT Ingredients.*, Recipes.* FROM Ingredients JOIN Recipes ON Ingredients.recipe_id = Recipes.id WHERE (Ingredients.recipe_id IN (?))";
    
    /**
     * Konstruktori, joka luo yhteyden ainesosat sisältävään tietokantatauluun.
     * @param fileName tietokantatiedoston nimi
     * @param username tietokannan käyttäjänimi
     * @param password tietokannan salasana
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    
    public SQLIngredientDao(String fileName, String username, String password) throws SQLException, ClassNotFoundException {
        super(fileName, username, password);
        Statement stmt = connect().createStatement();   
        stmt.execute(createIngredientTable);
        endConnection(stmt);         
    }
    
    /**
     * Metodi tallentaa uuden ainesosan tiedot tietokantaan.
     * @param ingredient    ainesosa, joka halutaan tallentaa
     * @return true jos ainesosan tietojen tallentaminen onnistuu, muuten false
     */

    @Override
    public boolean create(Ingredient ingredient) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(insertIngredient);
            pstmt.setString(1, ingredient.getName());
            pstmt.setDouble(2, ingredient.getAmount());
            pstmt.setString(3, ingredient.getUnit().toString());
            pstmt.setInt(4, ingredient.getRecipe().getId());
            completePreparedConnection(pstmt);        
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Metodi poistaa valitun ainesosan tiedot tietokannasta.
     * @param ingredient    ainesosa, joka halutaan poistaa
     * @return true jos ainesosan tietojen poistaminen onnistuu, muuten false
     */

    @Override
    public boolean remove(Ingredient ingredient) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(deleteIngredientByNameAndRecipe);
            pstmt.setString(1, ingredient.getName());
            pstmt.setInt(2, ingredient.getRecipe().getId());
            completePreparedConnection(pstmt);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Metodi poistaa kaikki tiettyyn reseptiin liittyvät ainesosat tietokannasta.
     * @param recipe    resepti, johon liittyvät ainesosat halutaan poistaa
     * @return true jos ainesosien tietojen poistaminen onnistuu, muuten false
     */

    @Override
    public boolean removeByRecipe(Recipe recipe) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(deleteIngredientsByRecipe);
            pstmt.setInt(1, recipe.getId());
            completePreparedConnection(pstmt);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa ainesosan nimen ja reseptin perusteella.
     * @param name  haettavan ainesosan nimi
     * @param recipe    resepti, johon haettava ainesosa liittyy
     * @return Haettava ainesosa {@code Ingredient}-olion muodossa, jos ainesosaa ei löydy niin null
     */

    @Override
    public Ingredient findByNameAndRecipe(String name, Recipe recipe) {
        try {
            Ingredient ingredient = null;   
            PreparedStatement pstmt = connect().prepareStatement(selectIngredientByNameAndRecipe);
            pstmt.setString(1, name);
            pstmt.setInt(2, recipe.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredient = new Ingredient(rs.getString("name"), rs.getDouble("amount"), rs.getString("unit"), recipe);                            
            }
            endPreparedConnection(pstmt, rs);
            return ingredient;            
        } catch (SQLException e) {
            return null;
        }

    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipe resepti, johon liittyvät ainesosat halutaan
     * @return Lista, joka sisältää kaikki märiteltyyn reseptiin liittyvät ainesosat {@code Ingredient}-olioina
     */

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        try {
            List<Ingredient> ingredients = new ArrayList<>();
            PreparedStatement pstmt = connect().prepareStatement(selectIngredientsByRecipe);
            pstmt.setInt(1, recipe.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), rs.getDouble("amount"), rs.getString("unit"), recipe));            
            }

            super.endPreparedConnection(pstmt, rs);
            return ingredients;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Meto hakee ja palauttaa kaikki tallennetut ainesosat.
     * @return Lista, joka sisältää kaikki tallennetut ainesosat {@code Ingredient}-olioina
     */

    @Override
    public List<Ingredient> findAll() {
        try {
            List<Ingredient> ingredients = new ArrayList<>();
            PreparedStatement pstmt = connect().prepareStatement(selectAllIngredients);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe(rs.getInt("Recipes.id"), rs.getString("Recipes.name"), rs.getInt("Recipes.portion"), rs.getString("Recipes.type"), new User(rs.getString("Recipes.user")));
                ingredients.add(new Ingredient(rs.getString("Ingredients.name"), rs.getDouble("Ingredients.amount"), rs.getString("Ingredients.unit"), recipe));           
            }
            endPreparedConnection(pstmt, rs);
            return ingredients;  
        } catch (SQLException e) {
            return new ArrayList<>();
        }  
    }
    
    @Override
    public List<Ingredient> findByRecipes(List<Recipe> recipes) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(findByRecipe(recipe));
        }
        return ingredients;
    }
    
}
