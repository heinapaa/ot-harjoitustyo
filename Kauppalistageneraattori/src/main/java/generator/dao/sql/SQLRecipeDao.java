package generator.dao.sql;

import generator.dao.RecipeDao;
import generator.models.Recipe;
import generator.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Reseptejä tietokantaan tallentava luokka, joka toteuttaa RecipeDao-rajapinnan.
 * 
 */

public class SQLRecipeDao extends SQLDao implements RecipeDao {
    
    private final String createRecipeTable = "CREATE TABLE IF NOT EXISTS Recipes (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255) NOT NULL, portion INT NOT NULL, type VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL)";
    private final String insertRecipe = "INSERT INTO Recipes (name, portion, type, user) VALUES(?,?,?,?)";
    private final String selectRecipeById = "SELECT * FROM Recipes WHERE id = ?";
    private final String selectRecipeByNameAndUser = "SELECT * FROM Recipes WHERE name = ? AND user = ?";
    private final String selectRecipesByUser =  "SELECT * FROM Recipes WHERE user = ?";
    private final String selectRecipesByTypeAndUser = "SELECT * FROM Recipes WHERE type = ? AND user = ?";
    private final String selectAllRecipes = "SELECT * FROM Recipes";    
    private final String updateRecipe = "UPDATE Recipes SET name = ?, portion = ?, type = ? WHERE id = ?";
    private final String deleteRecipeById =  "DELETE FROM Recipes WHERE id = ?";
    private final String deleteIngredientsByRecipe = "DELETE FROM Ingredients WHERE recipe_id = ?"; 
    
    /**
     * Konstruktori, joka luo yhteyden reseptit sisältävään tietokantatauluun.
     * @param fileName
     * @param username
     * @param password
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLRecipeConnection#createRecipeTable() 
     */    
    
    public SQLRecipeDao(String fileName, String username, String password) throws SQLException, ClassNotFoundException {
        super(fileName, username, password);
        Statement stmt = connect().createStatement();   
        stmt.executeUpdate(createRecipeTable);
        endConnection(stmt);        
    }
    
    /**
     * Metodi tallentaa uuden ainesosan tiedot tietokantaan.
     * @param recipe    resepti, joka halutaan tallentaa
     * @return true jos reseptin tietojen tallentaminen onnistuu, muuten false
     * @see generator.dao.sql.connection.SQLRecipeConnection#insertRecipe(java.lang.String, int, java.lang.String, java.lang.String) 
     */    

    @Override
    public boolean create(Recipe recipe) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(insertRecipe);
            pstmt.setString(1, recipe.getName());
            pstmt.setInt(2, recipe.getPortion());
            pstmt.setString(3, recipe.getType());
            pstmt.setString(4, recipe.getOwner().getUsername());
            completePreparedConnection(pstmt);
            return true;
        } catch (SQLException e) {
            return false;
        } 
        
    }
    
    /**
     * Metodi hakee ja palauttaa reseptin id-tunnisteen avulla.
     * @param id sen reseptin uniikki id-tunniste, jonka tiedot halutaan hakea
     * @return Haettua reseptiä vastaava {@code Recipe}-olio, null jos reseptiä ei löydy
     * @see generator.dao.sql.connection.SQLRecipeConnection#selectOneRecipeById(int) 
     */
     
    @Override
    public Recipe findById(int id) {
        try {
            Recipe recipe = null;   
            PreparedStatement pstmt = connect().prepareStatement(selectRecipeById);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));   
            }
            endPreparedConnection(pstmt, rs);  
            return recipe;           
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa reseptin nimen ja käyttäjän avulla.
     * @param name haettavan reseptin nimi
     * @param user käyttäjä, jolle haettava resepti kuuluu
     * @return Haettua reseptiä vastaava {@code Recipe}-olio, null jos reseptiä ei löydy
     * @see generator.dao.sql.connection.SQLRecipeConnection#selectOneRecipeByNameAndUser(java.lang.String, java.lang.String) 
     */    

    @Override
    public Recipe findByNameAndUser(String name, User user) {
        try {
            Recipe recipe = null;   
            PreparedStatement pstmt = connect().prepareStatement(selectRecipeByNameAndUser); 
            pstmt.setString(1, name);
            pstmt.setString(2, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), user);               
            }
            endPreparedConnection(pstmt, rs);       
            return recipe;            
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tietylle käyttäjälle kuuluvat tietyntyyppiset reseptit.
     * @param type haettu reseptityyppi
     * @param user käyttäjä, jonka reseptejä haetaan
     * @return Lista, joka sisältää kaikki hakukriteerit täyttävät reseptit {@code Recipe}-olioina
     * @see generator.dao.sql.connection.SQLRecipeConnection#selectAllRecipesByTypeAndUser(java.lang.String, java.lang.String)  
     */    

    @Override
    public List<Recipe> findByTypeAndUser(String type, User user) {        
        try {
            List<Recipe> recipes = new ArrayList<>(); 
            PreparedStatement pstmt = connect().prepareStatement(selectRecipesByTypeAndUser);
            pstmt.setString(1, type);
            pstmt.setString(2, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), user));
            }
            endPreparedConnection(pstmt, rs);
            return recipes;          
        } catch (SQLException e) {
            return new ArrayList<>();
        }        
    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tietylle käyttäjälle kuuluvat reseptit.
     * @param user käyttäjä, jonka reseptejä haetaan
     * @return Lista, joka sisältää kaikki hakukriteerit täyttävät reseptit {@code Recipe}-olioina
     * @see generator.dao.sql.connection.SQLRecipeConnection#selectAllRecipesByUser(java.lang.String) 
     */      

    @Override
    public List<Recipe> findByUser(User user) {         
        try {
            List<Recipe> recipes = new ArrayList<>(); 
            PreparedStatement pstmt = connect().prepareStatement(selectRecipesByUser);
            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), user));
            }
            endPreparedConnection(pstmt, rs); 
            return recipes;         
        } catch (SQLException e) {
            return new ArrayList<>();
        }          
    }
    
    /**
     * Metodi hakee ja palauttaa kaikki tallennetut reseptit.
     * @return Lista, joka sisältää kaikki tietokantaan tallennetut reseptit {@code Recipe}-olioina
     * @see generator.dao.sql.connection.SQLRecipeConnection#selectAllRecipes() 
     */      

    @Override
    public List<Recipe> findAll() {
        try {
            List<Recipe> recipes = new ArrayList<>();   
            PreparedStatement pstmt = connect().prepareStatement(selectAllRecipes);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));            
            }
            endPreparedConnection(pstmt, rs);   
            return recipes;     
        } catch (SQLException e) {
            return new ArrayList<>();
        }         
    }
    
    /**
     * Metodi muokkaa valitun reseptin tietoja annettujen syötteiden perusteella.
     * @param newName reseptin uusi niim
     * @param newPortion reseptin uusi annoskoko
     * @param newType reseptin uusi tyyppi
     * @param recipe muokattava resepti
     * @return true jos reseptin uusien tietojen tallennus onnistuu, muuten false
     */

    @Override
    public boolean update(String newName, int newPortion, String newType, Recipe recipe) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(updateRecipe);
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPortion);
            pstmt.setString(3, newType);
            pstmt.setInt(4, recipe.getId());
            completePreparedConnection(pstmt);
            return true;           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }         
    }
    
    /**
     * Metodi poistaa valitun reseptin.
     * @param recipe resepti joka halutaan poistaa
     * @return true jos reseptin poistaminen onnistuu, muuten false
     */

    @Override
    public boolean remove(Recipe recipe) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(deleteIngredientsByRecipe);
            pstmt.setInt(1, recipe.getId());
            completePreparedConnection(pstmt);                        
        } catch (SQLException e) {
            System.out.println("Ainesosien poistaminen epäonnistui. Jatketaan reseptin poistamiseen.");;
        }    
        try {
            PreparedStatement pstmt = super.connect().prepareStatement(deleteRecipeById);
            pstmt.setInt(1, recipe.getId());
            completePreparedConnection(pstmt);  
            return true;
        } catch (SQLException e) {
            return false;
        } 
    }
    
}
