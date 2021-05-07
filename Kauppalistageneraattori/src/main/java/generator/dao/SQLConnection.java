package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {
    
    private String url;
    private String driver = "org.h2.Driver";   
    private String user = "sa"; 
    private String pw = "";     
    
    public SQLConnection(String fileName) {
        this.url = "jdbc:h2:./database";
    } 
    
    private Connection connect() {
        Connection conn = null; 
        try {
            Class.forName(driver); 
            conn = DriverManager.getConnection(url,user,pw); 
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    
    public void createUserTable() {
        Connection conn = null; 
        Statement stmt = null;              
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Users " + 
                "(name VARCHAR(255) not NULL)";                 
            System.out.println("Yhdistetään käyttäjätaulukkoa...");
            conn = connect();
            stmt = conn.createStatement();           
            stmt.executeUpdate(sql);
            System.out.println("Käyttäjätaulukko yhdistetty!\n");
            stmt.close(); 
            conn.close();
        } catch (SQLException e) {
            System.out.println("Käyttäjätaulukon luominen epäonnistui -> " + e.getMessage());
        }
    }
    
    public boolean insertUser(String name) {
        Connection conn = null; 
        PreparedStatement pstmt = null;            
        try {
            String sql = "INSERT INTO Users VALUES(?)";            
            conn = connect();
            pstmt = conn.prepareStatement(sql);           
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä " + name + " luotu");
            pstmt.close(); 
            conn.close();           
            return true;
        } catch (SQLException e) {
            System.out.println("Käyttäjän " + name + " luonti epäonnistui -> " + e.getMessage());
            return false;
        }
    }   
    
    public User selectOneUser(String username){
        User user = null;   
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect();
            String sql = "SELECT * FROM Users WHERE name = ?"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();    
            while (rs.next()) {
                user = new User(rs.getString("name"));
                System.out.println("Löytynyt käyttäjä: " + rs.getString("name"));                
            }
            rs.close();
            pstmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }      
    
    public List<User> selectAllUsers(){
        List<User> users = new ArrayList<>();    
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect();
            String sql = "SELECT * FROM Users";   
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString("name")));
                System.out.println(rs.getString("name"));
            }
            rs.close();
            pstmt.close(); 
            conn.close();            
        } catch (SQLException e) {            
            System.out.println(e.getMessage());
        }
        return users;
    }   
    
    public void createRecipeTable() {
        Connection conn = null; 
        Statement stmt = null;   
        try {
            String sql =  "CREATE TABLE IF NOT EXISTS Recipes " + 
                        "(id IDENTITY NOT NULL PRIMARY KEY, " + 
                        " name VARCHAR(255) not NULL, " +  
                        " portion INTEGER not NULL, " +  
                        " type VARCHAR(255) not NULL, " +  
                        " user VARCHAR(255) not NULL)";                  
            System.out.println("Yhdistetään reseptitaulukkoa...");            
            conn = connect();
            stmt = conn.createStatement();   
            stmt.executeUpdate(sql);
            System.out.println("Reseptitaulukko yhdistetty!\n");
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println("Reseptitaulukon yhdistäminen epäonnistui -> " + e.getMessage());
        }
    }    
    
    public boolean insertRecipe(String name, int portion, String type, String owner) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO Recipes (name, portion, type, user) VALUES(?,?,?,?)";    
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, portion);
            pstmt.setString(3, type);
            pstmt.setString(4, owner);
            pstmt.executeUpdate();
            System.out.println("Resepti " + name + " luotu!");
            pstmt.close(); 
            conn.close();            
            return true;
        } catch (SQLException e) {
            System.out.println("Reseptin " + name + " luonti epäonnistui!");
            return false;
        }
    }     
    
    public Recipe selectOneRecipeById(int id){
        Recipe recipe = null;   
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Recipes WHERE id = ?";      
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));   
                System.out.println("Löytynyt resepti: " + rs.getString("name"));
            }
            rs.close();
            pstmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    }    
    
    public Recipe selectOneRecipeByNameAndUser(String name, String username){
        Recipe recipe = null;   
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Recipes WHERE name = ? AND user = ?";            
            conn = connect();
            pstmt = conn.prepareStatement(sql); 
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), new User(username));
                System.out.println("Löytynyt resepti: " + rs.getString("name"));                
            }
            rs.close();
            pstmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    }     
    
    public List<Recipe> selectAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();   
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Recipes";
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));              
            }
            rs.close();
            pstmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    } 
    
    public List<Recipe> selectAllRecipesByTypeAndUser(String type, String user){
        List<Recipe> recipes = new ArrayList<>();        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Recipes WHERE type = ? AND user = ?";      
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            pstmt.setString(2, user);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(rs.getInt("id"), new Recipe(rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
            rs.close();
            pstmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }   
    
    public List<Recipe> selectAllRecipesByUser(String username){
        List<Recipe> recipes = new ArrayList<>();  
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Recipes WHERE user = ?";
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
            rs.close();
            pstmt.close(); 
            conn.close();          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }   
    
    public boolean updateRecipe(String newName, int newPortion, String newType, int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE Recipes SET name = ? , "
                    + "portion = ? ,"
                    + "type = ? "                
                    + "WHERE id = ?";
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPortion);
            pstmt.setString(3, newType);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();  
            System.out.println("Reseptin päivitys onnistui!");
            return true;
        } catch (SQLException e) {
            System.out.println("Reseptin päivitys epäonnistui!");
            return false;
        }
    }    
    
    public boolean deleteRecipe(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM Recipes WHERE id = ?";  
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();    
            System.out.println("Resepti poistettu!");
            return true;
        } catch (SQLException e) {
            System.out.println("Reseptin poistaminen epäonnistui -> " + e.getMessage());
            return false;
        }
    }

    public void createIngredientTable() {
        Connection conn = null; 
        Statement stmt = null;          
        try {
            String sql =  "CREATE TABLE IF NOT EXISTS Ingredients " + 
                        "(name VARCHAR(255) not NULL, " +  
                        " amount DOUBLE not NULL, " +  
                        " unit VARCHAR(255) not NULL, " +  
                        " recipe_id INTEGER not NULL)";              
            System.out.println("Yhdistetään ainesosataulukkoa...");            
            conn = connect();
            stmt = conn.createStatement();   
            stmt.execute(sql);
            System.out.println("Ainesosataulukko yhdistetty!\n");
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println("Ainesosataulukon yhdistäminen epäonnistui -> " + e.getMessage());
        }        
    }    
    
    public boolean insertIngredient(String name, double amount, String unit, int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO Ingredients(name, amount, unit, recipe_id) VALUES(?,?,?,?)";  
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setFloat(2, (float) amount);
            pstmt.setString(3, unit);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            System.out.println("Ainesosa " + name + " luotu!");
            pstmt.close(); 
            conn.close();           
            return true;
        } catch (SQLException e) {
            System.out.println("Ainesosan luominen epäonnistui -> " + e.getMessage());
            return false;
        }
    } 
    
    public Ingredient selectOneIngredientByNameAndRecipe(String name, int recipeId){
        Ingredient ingredient = null;   
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Ingredients WHERE name = ? AND recipe_id = ?"; 
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, recipeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredient = new Ingredient(name, (double) rs.getFloat("amount"), rs.getString("unit"), null);     
                System.out.println("Löytynyt ainesosa: " + rs.getString("name"));                
            }
            rs.close();
            pstmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredient;
    }     
    
    public List<Ingredient> selectAllIngredients(){
        List<Ingredient> ingredients = new ArrayList<>();
        Connection conn = null;     
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Ingredients";    
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), (double) rs.getFloat("amount"), rs.getString("unit"), selectOneRecipeById(rs.getInt("recipe_id"))));
                System.out.println(rs.getString("name"));             
            }
            rs.close();
            pstmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }  
    
    public List<Ingredient> selectAllIngredientsByRecipe(int recipeId){
        List<Ingredient> ingredients = new ArrayList<>();
        Connection conn = null;     
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM Ingredients WHERE recipe_id = ? JOIN Recipes "
                    +   "ON Ingredients.recipe_id = Recipe.id";    
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recipeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("Recipes.user"));
                Recipe recipe = new Recipe(rs.getInt("Recipes.id"), rs.getString("Recipes.name"), rs.getInt("Recipes.portion"), rs.getString("Recipes.type"), user);
                ingredients.add(new Ingredient(rs.getString("Ingredients.name"), rs.getDouble("Ingredients.amount"), rs.getString("Ingredients.unit"), recipe));            
            }
            rs.close();
            pstmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }    
    
    public boolean deleteIngredient(String name, int recipeId) {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM Ingredients WHERE name = ? AND recipe_id = ?";
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();;   
            System.out.println("Ainesosa poistettu");
            return true;
        } catch (SQLException e) {
            System.out.println("Ainesosan poistaminen epäonnistui -> " + e.getMessage());
            return false;
        }
    }  
    
    public boolean deleteAllIngredientsByRecipe(int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM Ingredients WHERE recipe_id = ?";
            conn = connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();  
            System.out.println("Resepti poistettu");
            return true;
        } catch (SQLException e) {
            System.out.println("Reseptin poistaminen epäonnistui -> " + e.getMessage());
            return false;
        }
    }     
}
