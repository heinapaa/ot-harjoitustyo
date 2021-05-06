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
    
    public SQLConnection(String fileName) {
        this.url = "jdbc:sqlite:database.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Yhteys tietokantaan luotu");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }   
    }
    
    private Connection connect() {
        String url = "jdbc:sqlite:test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }    
    
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	name text PRIMARY KEY"
                + ");";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Käyttäjätaulukko yhdistetty");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean insertUser(String name) {
        String sql = "INSERT INTO users(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä " + name + " luotu!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }   
    
    public User selectOneUser(String username){
        String sql = "SELECT name FROM users WHERE name='" + username + "'";
        User user = null;   
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            user = new User(rs.getString("name"));
            System.out.println("Löytynyt käyttäjä: " + rs.getString("name"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }      
    
    public List<User> selectAllUsers(){
        String sql = "SELECT name FROM users";
        List<User> users = new ArrayList<>();        
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getString("name")));
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }   
    
    public void createRecipeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS recipes (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	name TEXT NOT NULL,\n"
                + "	portion INTEGER NOT NULL,\n"
                + "	type TEXT NOT NULL, \n"
                + "	user TEXT NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Reseptitaulukko yhdistetty");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public boolean insertRecipe(String name, int portion, String type, String owner) {
        String sql = "INSERT INTO recipes(name, portion, type, user) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, portion);
            pstmt.setString(3, type);
            pstmt.setString(4, owner);
            pstmt.executeUpdate();
            System.out.println("Resepti " + name + " luotu!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }   
    
    public Recipe selectOneRecipeById(int id){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE id ='" + id + "'";
        Recipe recipe = null;   
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            recipe = new Recipe(id, rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user")));
            System.out.println("Löytynyt resepti: " + rs.getString("name"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    } 
    
    public Recipe selectOneRecipeByNameAndUser(String name, String username){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE name ='" + name + "' AND user = '" + username + "'";
        Recipe recipe = null;   
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            recipe = new Recipe(rs.getInt("id"), name, rs.getInt("portion"), rs.getString("type"), new User(username));
            System.out.println("Löytynyt resepti: " + rs.getString("name"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipe;
    }     
    
    public List<Recipe> selectAllRecipes(){
        String sql = "SELECT id, name, portion, type, user FROM recipes";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    } 
    
    public List<Recipe> selectAllRecipesByType(String type){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE type = '" + type + "'";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(rs.getInt("id"), new Recipe(rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }   
    
    public List<Recipe> selectAllRecipesByUser(String username){
        String sql = "SELECT id, name, portion, type, user FROM recipes WHERE user = '" + username + "'";
        List<Recipe> recipes = new ArrayList<>();        
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new Recipe(rs.getInt("id"), rs.getString("name"), rs.getInt("portion"), rs.getString("type"), new User(rs.getString("user"))));
                System.out.println(rs.getString("name"));
            }
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
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPortion);
            pstmt.setString(3, newType);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }    
    
    public boolean deleteRecipe(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void createIngredientTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ingredients (\n"
                + "	name TEXT NOT NULL,\n"
                + "	amount REAL NOT NULL,\n"
                + "	unit TEXT NOT NULL, \n"
                + "	recipe_id INTEGER NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Ainesosataulukko yhdistetty");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public boolean insertIngredient(String name, double amount, String unit, int recipeId) {
        String sql = "INSERT INTO ingredients(name, amount, unit, recipe_id) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setFloat(2, (float) amount);
            pstmt.setString(3, unit);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            System.out.println("Ainesosa " + name + " luotu!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    } 
    
    public Ingredient selectOneIngredientByNameAndRecipe(String name, int recipeId){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients WHERE name ='" + name + "' AND recipe_id = '" + recipeId + "'";
        Ingredient ingredient = null;   
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ingredient = new Ingredient(name, (double) rs.getFloat("amount"), rs.getString("unit"), null);
            System.out.println("Löytynyt ainesosa: " + rs.getString("name"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredient;
    }     
    
    public List<Ingredient> selectAllIngredients(){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients";
        List<Ingredient> ingredients = new ArrayList<>();        
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), (double) rs.getFloat("amount"), rs.getString("unit"), selectOneRecipeById(rs.getInt("recipe_id"))));
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }  
    
    public List<Ingredient> selectAllIngredientsByRecipe(int recipeId){
        String sql = "SELECT name, amount, unit, recipe_id FROM ingredients WHERE recipe_id = '" + recipeId + "'";
        List<Ingredient> ingredients = new ArrayList<>();   
        Recipe recipe = selectOneRecipeById(recipeId);
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getString("name"), (double) rs.getFloat("amount"), rs.getString("unit"), recipe));
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }    
    
    public boolean deleteIngredient(String name, int recipeId) {
        String sql = "DELETE FROM ingredients WHERE name = ? AND recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }  
    
    public boolean deleteAllIngredientsByRecipe(int recipeId) {
        String sql = "DELETE FROM ingredients WHERE recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }     
}
