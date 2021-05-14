package generator.dao.sql;

import generator.models.Recipe;
import generator.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Resepteihin liittyvistä tietokantakyselyistä vastaava luokka, joka laajentaa {@Code SQLConnection}-luokkaa.
 *
 */

public class SQLRecipeConnection extends SQLConnection {
    
    private final String CREATE_RECIPE_TABLE = "CREATE TABLE IF NOT EXISTS Recipes (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255) NOT NULL, portion INT NOT NULL, type VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL)";
    private final String INSERT_RECIPE = "INSERT INTO Recipes (name, portion, type, user) VALUES(?,?,?,?)";
    private final String SELECT_RECIPE_BY_ID = "SELECT * FROM Recipes WHERE id = ?";
    private final String SELECT_RECIPE_BY_NAME_AND_USER = "SELECT * FROM Recipes WHERE name = ? AND user = ?";
    private final String SELECT_RECIPES_BY_USER =  "SELECT * FROM Recipes WHERE user = ?";
    private final String SELECT_RECIPES_BY_TYPE_AND_USER = "SELECT * FROM Recipes WHERE type = ? AND user = ?";
    private final String SELECT_ALL_RECIPES = "SELECT * FROM Recipes";    
    private final String UPDATE_RECIPE = "UPDATE Recipes SET name = ?, portion = ?, type = ? WHERE id = ?";
    private final String DELETE_RECIPE_BY_ID =  "DELETE FROM Recipes WHERE id = ?";
    private final String DELETE_ALL_INGREDIENTS_BY_RECIPE = "DELETE FROM Ingredients WHERE recipe_id = ?"; 
    
    /**
     * Konstruktori
     * @param fileName  tietokannan tiedostonimi
     * @param username  tietokannan käyttäjänimi
     * @param password  tietokannan salasana
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#SQLConnection(java.lang.String, java.lang.String, java.lang.String) 
     */
    
    public SQLRecipeConnection(String fileName, String username, String password) throws ClassNotFoundException {
        super(fileName, username, password);
    }
    
    /**
     * Metodi luo taulun reseptien tietojen tallennusta varten (kenttinä id-tunniste, nimi, annoskoko, tyyppi ja reseptin omistajan käyttäjänimi), jos sellaista ei ole jo olemassa.
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endConnection(java.sql.Connection, java.sql.Statement)
     */
    
    public void createRecipeTable() throws SQLException {          
        Statement stmt = connect().createStatement();   
        stmt.executeUpdate(CREATE_RECIPE_TABLE);
        endConnection(stmt);
    }   
    
    /**
     * Metodi lisää syötteenä annetut reseptin tiedot tietokantaan.
     * @param name  tallennettavan reseptin nimi
     * @param portion   tallennettavan reseptin annoskoko
     * @param type  tallennettavan reseptin tyyppi
     * @param owner sen käyttäjän käyttäjänimi, jolle tallennettava resepti kuuluu
     * @return true jos reseptin tietojen tallennus onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean insertRecipe(String name, int portion, String type, String owner) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(INSERT_RECIPE);
        pstmt.setString(1, name);
        pstmt.setInt(2, portion);
        pstmt.setString(3, type);
        pstmt.setString(4, owner);
        completePreparedConnection(pstmt);
        return true;
    }    
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden reseptin tiedot id-tunnisteen perusteella.
     * @param id    haettavan reseptin id
     * @return haettava resepti {@code Recipe}-oliona, null jos reseptiä ei löydy
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public Recipe selectOneRecipeById(int id) throws SQLException {
        Recipe recipe = null;   
        PreparedStatement pstmt = connect().prepareStatement(SELECT_RECIPE_BY_ID);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));   
        }
        endPreparedConnection(pstmt, rs);  
        return recipe;
    }    
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden reseptin tiedot nimen ja reseptin omistavan käyttäjän perusteella.
     * @param name  haettavan reseptin nimi
     * @param username  sen käyttäjän käyttäjänimi, jolle haettava resepti kuuluu
     * @return haettava resepti {@code Recipe}-oliona, null jos reseptiä ei löydy 
     * @throws java.sql.SQLException 
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public Recipe selectOneRecipeByNameAndUser(String name, String username) throws SQLException {
        Recipe recipe = null;   
        PreparedStatement pstmt = connect().prepareStatement(SELECT_RECIPE_BY_NAME_AND_USER); 
        pstmt.setString(1, name);
        pstmt.setString(2, username);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), new User(username));               
        }
        endPreparedConnection(pstmt, rs);       
        
        return recipe;
    }     

    /**
     * Metodi hakee ja palauttaa tietokannasta kaikki tietylle käyttäjälle kuuluvat reseptit.
     * @param username  sen käyttäjän käyttäjätunnus, jolle kuuluvia reseptejä haetaan
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipesByUser(String username) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();  
        PreparedStatement pstmt = connect().prepareStatement(SELECT_RECIPES_BY_USER);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
        }
        endPreparedConnection(pstmt, rs); 
        return recipes;
    }   
        
    /**
     * Metodi hakee ja palauttaa tietokannasta kaikki tiettyä reseptityyppiä olevat reseptit, jotka kuuluvat tietylle käyttäjälle.
     * @param type  reseptityyppi, jota vastaavat reseptit halutaan löytää
     * @param user  sen käyttäjän käyttäjänimi, jonka reseptit halutaan löytää
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipesByTypeAndUser(String type, String user) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();        
        PreparedStatement pstmt = connect().prepareStatement(SELECT_RECIPES_BY_TYPE_AND_USER);
        pstmt.setString(1, type);
        pstmt.setString(2, user);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
        }
        endPreparedConnection(pstmt, rs);
        return recipes;
    }   
    
    /**
     * Metodi hakee ja palauttaa kaikki tietokantaan tallennetut reseptit.
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina 
     * @throws java.sql.SQLException 
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();   
        PreparedStatement pstmt = connect().prepareStatement(SELECT_ALL_RECIPES);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));            
        }
        endPreparedConnection(pstmt, rs);   
        return recipes;
    } 
    
    /**
     * Metodi päivittää reseptin tiedot syötteiden perusteella tietokantaan.
     * @param newName  haluttu uusi nimi
     * @param newPortion haluttu uusi annoskoko
     * @param newType   haluttu uusi tyyppi
     * @param recipeId  päivitettävän reseptin id-tunniste
     * @return true jos reseptin tietojen päivitys onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
        
    public boolean updateRecipe(String newName, int newPortion, String newType, int recipeId) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(UPDATE_RECIPE);
        pstmt.setString(1, newName);
        pstmt.setInt(2, newPortion);
        pstmt.setString(3, newType);
        pstmt.setInt(4, recipeId);
        completePreparedConnection(pstmt);
        return true;
    }    
    
    /**
     * Metodi poistaa tietokannasta reseptin annetun id-tunnisteen perusteella.
     * @param id    poistettavan reseptin id-tunniste
     * @return true jos reseptin tietojen poistaminen onnistuu, muuten false
     * @throws java.sql.SQLException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean deleteRecipe(int id) throws SQLException {
        PreparedStatement pstmt = connect().prepareStatement(DELETE_ALL_INGREDIENTS_BY_RECIPE);
        pstmt.setInt(1, id);
        completePreparedConnection(pstmt);    
        
        pstmt = super.connect().prepareStatement(DELETE_RECIPE_BY_ID);
        pstmt.setInt(1, id);
        completePreparedConnection(pstmt);            

        return true;            
    }       
}