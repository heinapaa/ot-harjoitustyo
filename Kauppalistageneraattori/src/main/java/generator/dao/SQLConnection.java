package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {
    
    private String url;
    
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:./database";    
    static final String USER = "sa"; 
    static final String PASS = "";     
    
    public SQLConnection(String fileName) {
        this.url = "jdbc:sqlite:database.db";
    } 
    
    private Connection connect() {
        Connection conn = null; 
        System.out.println("1");
        try {
            // STEP 1: Register JDBC driver 
            System.out.println("2");
            Class.forName(JDBC_DRIVER); 
            System.out.println("3");
            //STEP 2: Open a connection 
            System.out.println("Connecting to database..."); 
            conn = DriverManager.getConnection(DB_URL,USER,PASS); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    
    public void createUserTable() {
        Connection conn = null; 
        Statement stmt = null;         
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	name text PRIMARY KEY"
                + ");";
        
        sql =  "CREATE TABLE   users " + 
            "(name VARCHAR(255) not NULL, " + 
            " PRIMARY KEY ( name ))";          
        try {
            conn = connect();
            stmt = conn.createStatement();           
            stmt.execute(sql);
            System.out.println("Käyttäjätaulukko yhdistetty");
            stmt.close(); 
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean insertUser(String name) {
        Connection conn = null; 
        PreparedStatement pstmt = null;            
        String sql = "INSERT INTO users(name) VALUES(?)";
        try {
            conn = connect();
            pstmt = conn.prepareStatement(sql);           
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä " + name + " luotu!");
            pstmt.close(); 
            conn.close();           
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }   
    
    public User selectOneUser(String username){
        String sql = "SELECT name FROM users WHERE name='" + username + "'";
        User user = null;   
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connect();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);         
            user = new User(rs.getString("name"));
            System.out.println("Löytynyt käyttäjä: " + rs.getString("name"));
            stmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }      
    
    public List<User> selectAllUsers(){
        String sql = "SELECT name FROM users";
        List<User> users = new ArrayList<>();        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getString("name")));
                System.out.println(rs.getString("name"));
            }
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {            
            System.out.println(e.getMessage());
        }
        return users;
    }   
    
    public void createRecipeTable() {
        Connection conn = null; 
        Statement stmt = null;          
        String sql = "CREATE TABLE IF NOT EXISTS recipes (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	name TEXT NOT NULL,\n"
                + "	portion INTEGER NOT NULL,\n"
                + "	type TEXT NOT NULL, \n"
                + "	user TEXT NOT NULL"
                + ");";
        try {
            conn = connect();
            stmt = conn.createStatement();        
            stmt.execute(sql);
            System.out.println("Reseptitaulukko yhdistetty");
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public boolean insertRecipe(String name, int portion, String type, String owner) {
        String sql = "INSERT INTO recipes(name, portion, type, user) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
            System.out.println(e.getMessage());
            return false;
        }
    }   
    
    public Recipe selectOneRecipeById(int id){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE id ='" + id + "'";
        Recipe recipe = null;   
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));
            System.out.println("Löytynyt resepti: " + rs.getString("name"));
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    } 
    
    public Recipe selectOneRecipeByNameAndUser(String name, String username){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE name ='" + name + "' AND user = '" + username + "'";
        Recipe recipe = null;   
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), new User(username));
            System.out.println("Löytynyt resepti: " + rs.getString("name"));
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    }     
    
    public List<Recipe> selectAllRecipes(){
        String sql = "SELECT id, name, portion, type, user FROM recipes";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));              
            }
            stmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    } 
    
    public List<Recipe> selectAllRecipesByType(String type){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE type = '" + type + "'";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(rs.getInt("id"), new Recipe(rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
            stmt.close(); 
            conn.close();            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }   
    
    public List<Recipe> selectAllRecipesByUser(String username){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE user = '" + username + "'";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
            stmt.close(); 
            conn.close();          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }   
    
    public boolean updateRecipe(String newName, int newPortion, String newType, int recipeId) {
        String sql = "UPDATE recipes SET name = ? , "
                + "portion = ? ,"
                + "type = ? "                
                + "WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPortion);
            pstmt.setString(3, newType);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();            
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }    
    
    public boolean deleteRecipe(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();            
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void createIngredientTable() {
        Connection conn = null;
        Statement stmt = null;
        String sql = "CREATE TABLE IF NOT EXISTS ingredients (\n"
                + "	name TEXT NOT NULL,\n"
                + "	amount REAL NOT NULL,\n"
                + "	unit TEXT NOT NULL, \n"
                + "	recipe_id INTEGER NOT NULL"
                + ");";
        try {
            conn = connect();
            stmt = conn.createStatement();             
            stmt.execute(sql);
            System.out.println("Ainesosataulukko yhdistetty");
            stmt.close(); 
            conn.close();          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public boolean insertIngredient(String name, double amount, String unit, int recipeId) {
        String sql = "INSERT INTO ingredients(name, amount, unit, recipe_id) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
            System.out.println(e.getMessage());
            return false;
        }
    } 
    
    public Ingredient selectOneIngredientByNameAndRecipe(String name, int recipeId){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients WHERE name ='" + name + "' AND recipe_id = '" + recipeId + "'";
        Ingredient ingredient = null;   
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ingredient = new Ingredient(name, (double) rs.getFloat("amount"), rs.getString("unit"), null);
            System.out.println("Löytynyt ainesosa: " + rs.getString("name"));
            stmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredient;
    }     
    
    public List<Ingredient> selectAllIngredients(){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients";
        List<Ingredient> ingredients = new ArrayList<>();        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), (double) rs.getFloat("amount"), rs.getString("unit"), selectOneRecipeById(rs.getInt("recipe_id"))));
                System.out.println(rs.getString("name"));             
            }
            stmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }  
    
    public List<Ingredient> selectAllIngredientsByRecipe(int recipeId){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients WHERE recipe_id = '" + recipeId + "'";
        List<Ingredient> ingredients = new ArrayList<>();   
        Recipe recipe = selectOneRecipeById(recipeId);
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), (double) rs.getFloat("amount"), rs.getString("unit"), recipe));
                System.out.println(rs.getString("name"));
            }
            stmt.close(); 
            conn.close();           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }    
    
    public boolean deleteIngredient(String name, int recipeId) {
        String sql = "DELETE FROM ingredients WHERE name = ? AND recipe_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();;            
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }  
    
    public boolean deleteAllIngredientsByRecipe(int recipeId) {
        String sql = "DELETE FROM ingredients WHERE recipe_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
            pstmt.close(); 
            conn.close();           
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }     
}
