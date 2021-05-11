package generator.dao.sql;

import generator.models.Ingredient;
import generator.models.Recipe;
import generator.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Ainesosiin liittyvistä tietokantakyselyistä vastaava luokka, joka laajentaa {@Code SQLConnection}-luokkaa.
 * 
 */
public class SQLIngredientConnection extends SQLConnection {
    
    private final String CREATE_INGREDIENT_TABLE =  "CREATE TABLE IF NOT EXISTS Ingredients (name VARCHAR(255) NOT NULL, amount DOUBLE NOT NULL, unit VARCHAR(255) NOT NULL, recipe_id INT NOT NULL)";         
    private final String INSERT_INGREDIENT = "INSERT INTO Ingredients(name, amount, unit, recipe_id) VALUES(?,?,?,?)"; 
    private final String SELECT_INGREDIENT_BY_NAME_AND_RECIPE = "SELECT * FROM Ingredients WHERE name = ? AND recipe_id = ?";  
    private final String SELECT_INGREDIENTS_BY_RECIPE = "SELECT Ingredients.*, Recipes.* FROM Ingredients JOIN Recipes ON Ingredients.recipe_id = Recipes.id WHERE Ingredients.recipe_id = ?";     
    private final String SELECT_ALL_INGREDIENTS = "SELECT Ingredients.*, Recipes.* FROM Ingredients JOIN Recipes ON Ingredients.recipe_id = Recipes.id";    
    private final String DELETE_INGREDIENT_BY_NAME_AND_RECIPE = "DELETE FROM Ingredients WHERE name = ? AND recipe_id = ?";
    private final String DELETE_INGREDIENTS_BY_RECIPE = "DELETE FROM Ingredients WHERE recipe_id = ?";
    
    /**
     * Konstruktori
     * @param fileName  tietokannan tiedostonimi
     * @param username  tietokannan käyttäjänimi
     * @param password  tietokannan salasana
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#SQLConnection(java.lang.String, java.lang.String, java.lang.String) 
     */    
    
    public SQLIngredientConnection(String fileName, String username, String password) throws ClassNotFoundException {
        super(fileName, username, password);   
    }
    
    /**
     * Metodi luo tietokannan ainesosien tallennusta varten (kenttinä nimi, määrä, yksikkö ja ainesosaan liittyvän reseptin id-tunniste), jos sellaista ei ole jo olemassa.
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endConnection(java.sql.Connection, java.sql.Statement) 
     */

    public void createIngredientTable() throws SQLException {               
        System.out.println("Yhdistetään ainesosataulukkoa...");            
        Statement stmt = connect().createStatement();   
        stmt.execute(CREATE_INGREDIENT_TABLE);
        System.out.println("Ainesosataulukko yhdistetty!\n");
        endConnection(stmt);                  
    }  
    
    /**
     * Metodi lisää tietokantaan annetut ainesosan tiedot.
     * @param name  ainesosan nimi
     * @param amount    ainesosan määrä 
     * @param unit  ainesosan yksikkö
     * @param recipeId  sen reseptin id-tunniste, johon ainesosa liittyy
     * @return true jos ainesosan tietojen tallennus onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean insertIngredient(String name, double amount, String unit, int recipeId) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(INSERT_INGREDIENT);
        pstmt.setString(1, name);
        pstmt.setDouble(2, amount);
        pstmt.setString(3, unit);
        pstmt.setInt(4, recipeId);
        completePreparedConnection(pstmt);        
        return true;
    } 
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden ainesosan tiedot nimen ja reseptin, johon ainesosa liittyy, perusteella
     * @param name  ainesosan nimi
     * @param recipeId  sen reseptin id-tunniste, johon ainesosa liittyy
     * @return Haetun ainesosan tiedot {@code Ingredient}-oliona, null mikäli anesosaa ei löydy
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public Ingredient selectOneIngredientByNameAndRecipe(String name, int recipeId) throws SQLException {
        Ingredient ingredient = null;   
        PreparedStatement pstmt = connect().prepareStatement(SELECT_INGREDIENT_BY_NAME_AND_RECIPE);
        pstmt.setString(1, name);
        pstmt.setInt(2, recipeId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            ingredient = new Ingredient(name, (double) rs.getFloat("amount"), rs.getString("unit"), null);                    
        }
        endPreparedConnection(pstmt, rs);
        return ingredient;
    }     
    
    /**
     * Metodi hakee ja palauttaa kaikki tietokantaan tallennetut ainesosat.
     * @return Lista, joka sisältää kaikki tietokantaan tallennetut ainesosat {@code Ingredient}-olioina.
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Ingredient> selectAllIngredients() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        PreparedStatement pstmt = connect().prepareStatement(SELECT_ALL_INGREDIENTS);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Recipe recipe = new Recipe(rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("owner")));
            ingredients.add(new Ingredient(rs.getString("name"), rs.getDouble("amount"), rs.getString("unit"), recipe));           
        }
        endPreparedConnection(pstmt, rs);
        return ingredients;
    }  
    
    /**
     * Metodi hakee ja palauttaa tietokannasta kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipeId  sen reseptin id-tunniste, johon liittyviä ainesosia haetaan
     * @return Lista, joka sisältää reseptiin liittyvät ainesosat {@code Ingredient}-olioina.
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Ingredient> selectAllIngredientsByRecipe(int recipeId) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        PreparedStatement pstmt = connect().prepareStatement(SELECT_INGREDIENTS_BY_RECIPE);
        pstmt.setInt(1, recipeId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            User user = new User(rs.getString("Recipes.user"));
            Recipe recipe = new Recipe(rs.getInt("Recipes.id"), rs.getString("Recipes.name"), rs.getInt("Recipes.portion"), rs.getString("Recipes.type"), user);
            ingredients.add(new Ingredient(rs.getString("Ingredients.name"), rs.getDouble("Ingredients.amount"), rs.getString("Ingredients.unit"), recipe));            
        }
        
        super.endPreparedConnection(pstmt, rs);
        return ingredients;
    }   
    
    /**
     * Metodi poistaa tietyn ainesosan tietokannasta nimen ja sen reseptin id-tunnisteen perusteella, johon ainesosa liittyy
     * @param name  ainesosan nimi
     * @param recipeId  sen reseptin id-tunniste, johon ainesosa liittyy
     * @return true jos ainesosan poistaminen onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean deleteIngredient(String name, int recipeId) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(DELETE_INGREDIENT_BY_NAME_AND_RECIPE);
        pstmt.setString(1, name);
        pstmt.setInt(2, recipeId);
        completePreparedConnection(pstmt);
        return true;
    }  
    
    /**
     * Metodi poistaa tietokannasta kaikki tiettyyn reseptiin liittyvät ainesosat.
     * @param recipeId  sen reseptin id-tunniste, johon liittyvät ainesosat halutaan poistaa
     * @return true jos ainesosien poistaminen onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean deleteAllIngredientsByRecipe(int recipeId) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(DELETE_INGREDIENTS_BY_RECIPE);
        pstmt.setInt(1, recipeId);
        completePreparedConnection(pstmt);
        return true;
    }    
    
}
