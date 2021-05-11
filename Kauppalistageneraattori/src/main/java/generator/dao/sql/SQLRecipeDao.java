package generator.dao.sql;

import generator.dao.RecipeDao;
import generator.models.Recipe;
import generator.models.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reseptejä tietokantaan tallentava luokka, joka toteuttaa RecipeDao-rajapinnan.
 * 
 */

public class SQLRecipeDao implements RecipeDao {
    
    private final SQLRecipeConnection connection;
    
    /**
     * Konstruktori, joka luo yhteyden reseptit sisältävään tietokantatauluun.
     * @param conn {@code SQLConnection}-olio, jota käytetään tietokantapyyntöjen toteuttamiseen
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @see generator.dao.sql.connection.SQLRecipeConnection#createRecipeTable() 
     */    
    
    public SQLRecipeDao(SQLRecipeConnection conn) throws SQLException, ClassNotFoundException {
        this.connection = conn;
        connection.createRecipeTable();
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
            return connection.insertRecipe(recipe.getName(), recipe.getPortion(), recipe.getType(), recipe.getOwner().getUsername());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.selectOneRecipeById(id);            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.selectOneRecipeByNameAndUser(name, user.getUsername());            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.selectAllRecipesByTypeAndUser(type, user.getUsername());            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.selectAllRecipesByUser(user.getUsername());            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.selectAllRecipes();      
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            return connection.updateRecipe(newName, newPortion, newType, recipe.getId());            
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
            return connection.deleteRecipe(recipe.getId());            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }         
    }
    
}
