package generator.dao.sql;

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
 * Resepteihin liittyvistä tietokantakyselyistä vastaava luokka, joka laajentaa {@Code SQLConnection}-luokkaa.
 *
 */
public class SQLRecipeConnection extends SQLConnection {
    
    /**
     * Konstruktori
     * @param fileName  tietokannan tiedostonimi
     * @param username  tietokannan käyttäjänimi
     * @param password  tietokannan salasana
     * @see generator.dao.sql.connection.SQLConnection#SQLConnection(java.lang.String, java.lang.String, java.lang.String) 
     */
    
    public SQLRecipeConnection(String fileName, String username, String password) {
        super(fileName, username, password);
    }
    
    /**
     * Metodi luo taulun reseptien tietojen tallennusta varten (kenttinä id-tunniste, nimi, annoskoko, tyyppi ja reseptin omistajan käyttäjänimi), jos sellaista ei ole jo olemassa.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endConnection(java.sql.Connection, java.sql.Statement)
     */
    
    public void createRecipeTable() throws SQLException, ClassNotFoundException {
        String sql =  "CREATE TABLE IF NOT EXISTS Recipes (id INT NOT NULL, name VARCHAR(255) NOT NULL, portion INTEGER NOT NULL, type VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL)";                  
        
        System.out.println("Yhdistetään reseptitaulukkoa...");            
        Connection conn = super.connect();
        Statement stmt = conn.createStatement();   
        stmt.executeUpdate(sql);
        System.out.println("Reseptitaulukko yhdistetty!\n");
        
        super.endConnection(conn, stmt);
    }   
    
    /**
     * Metodi lisää syötteenä annetut reseptin tiedot tietokantaan.
     * @param name  tallennettavan reseptin nimi
     * @param portion   tallennettavan reseptin annoskoko
     * @param type  tallennettavan reseptin tyyppi
     * @param owner sen käyttäjän käyttäjänimi, jolle tallennettava resepti kuuluu
     * @return true jos reseptin tietojen tallennus onnistuu, muuten false
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean insertRecipe(String name, int portion, String type, String owner) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Recipes (name, portion, type, user) VALUES(?,?,?,?)";
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setInt(2, portion);
        pstmt.setString(3, type);
        pstmt.setString(4, owner);
        super.completePreparedConnection(conn, pstmt);
        
        return true;
    }    
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden reseptin tiedot id-tunnisteen perusteella.
     * @param id    haettavan reseptin id
     * @return haettava resepti {@code Recipe}-oliona, null jos reseptiä ei löydy
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public Recipe selectOneRecipeById(int id) throws SQLException, ClassNotFoundException {
        Recipe recipe = null;   
        String sql = "SELECT (id, name, portion, type, user) FROM Recipes WHERE id = ?";   
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));   
        }
        super.endPreparedConnection(conn, pstmt, rs);  
        
        return recipe;
    }    
    
    /**
     * Metodi hakee ja palauttaa tietokannasta yhden reseptin tiedot nimen ja reseptin omistavan käyttäjän perusteella.
     * @param name  haettavan reseptin nimi
     * @param username  sen käyttäjän käyttäjänimi, jolle haettava resepti kuuluu
     * @return haettava resepti {@code Recipe}-oliona, null jos reseptiä ei löydy 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public Recipe selectOneRecipeByNameAndUser(String name, String username) throws SQLException, ClassNotFoundException {
        Recipe recipe = null;   
        String sql = "SELECT (id, name, portion, type, user) FROM Recipes WHERE name = ? AND user = ?";   
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql); 
        pstmt.setString(1, name);
        pstmt.setString(2, username);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), new User(username));               
        }
        super.endPreparedConnection(conn, pstmt, rs);       
        
        return recipe;
    }     

    /**
     * Metodi hakee ja palauttaa tietokannasta kaikki tietylle käyttäjälle kuuluvat reseptit.
     * @param username  sen käyttäjän käyttäjätunnus, jolle kuuluvia reseptejä haetaan
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipesByUser(String username) throws SQLException, ClassNotFoundException {
        List<Recipe> recipes = new ArrayList<>();  
        String sql = "SELECT (id, name , portion, type, user) FROM Recipes WHERE user = ?";
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
            System.out.println(rs.getString("name"));
        }
        super.endPreparedConnection(conn, pstmt, rs); 
        
        return recipes;
    }   
        
    
    /**
     * Metodi hakee ja palauttaa tietokannasta kaikki tiettyä reseptityyppiä olevat reseptit, jotka kuuluvat tietylle käyttäjälle.
     * @param type  reseptityyppi, jota vastaavat reseptit halutaan löytää
     * @param user  sen käyttäjän käyttäjänimi, jonka reseptit halutaan löytää
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipesByTypeAndUser(String type, String user) throws SQLException, ClassNotFoundException {
        List<Recipe> recipes = new ArrayList<>();        
        String sql = "SELECT (id, name, portion, type, user) FROM Recipes WHERE type = ? AND user = ?"; 
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, type);
        pstmt.setString(2, user);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(rs.getInt("id"), new Recipe(rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
        }
        super.endPreparedConnection(conn, pstmt, rs);
        
        return recipes;
    }   
    
    /**
     * Metodi hakee ja palauttaa kaikki tietokantaan tallennetut reseptit.
     * @return lista, joka sisältää haetut reseptit {@code Recipe}-olioina 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#endPreparedConnection(java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet) 
     */
    
    public List<Recipe> selectAllRecipes() throws SQLException, ClassNotFoundException {
        List<Recipe> recipes = new ArrayList<>();   
        String sql = "SELECT (id, name, portion, type, user) FROM Recipes";
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
            System.out.println(rs.getString("name"));              
        }
        super.endPreparedConnection(conn, pstmt, rs);   
        
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
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
        
    public boolean updateRecipe(String newName, int newPortion, String newType, int recipeId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE (id, name, portion, type, user) Recipes SET name = ?, portion = ?, type = ? WHERE id = ?";
        
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newName);
        pstmt.setInt(2, newPortion);
        pstmt.setString(3, newType);
        pstmt.setInt(4, recipeId);
        super.completePreparedConnection(conn, pstmt);
        
        return true;
    }    
    
    /**
     * Metodi poistaa tietokannasta reseptin annetun id-tunnisteen perusteella.
     * @param id    poistettavan reseptin id-tunniste
     * @return true jos reseptin tietojen poistaminen onnistuu, muuten false
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLConnection#connect() 
     * @see generator.dao.sql.connection.SQLConnection#completePreparedConnection(java.sql.Connection, java.sql.PreparedStatement) 
     */
    
    public boolean deleteRecipe(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Ingredients WHERE recipe_id = ?"; 
        Connection conn = super.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        super.completePreparedConnection(conn, pstmt);

        sql = "DELETE FROM Recipes WHERE id = ?";  
        conn = super.connect();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        super.completePreparedConnection(conn, pstmt);
        
        return true;            
    }       
}