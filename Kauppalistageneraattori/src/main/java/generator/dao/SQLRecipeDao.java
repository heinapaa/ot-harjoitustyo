package generator.dao;

import generator.domain.Recipe;
import generator.domain.User;
import java.util.List;

public class SQLRecipeDao implements RecipeDao {
    
    private SQLConnection connection;
    
    public SQLRecipeDao(SQLConnection conn) {
        this.connection = conn;
        connection.createRecipeTable();
    }

    @Override
    public boolean create(Recipe recipe) {
        return connection.insertRecipe(recipe.getName(), recipe.getPortion(), recipe.getType(), recipe.getOwner().getUsername());
    }
    
    /*
    @Override
    public Recipe findById(int id) {
        return connection.selectOneRecipeById(id);
    }
    */

    @Override
    public Recipe findByNameAndUser(String name, User user) {
        Recipe recipe = connection.selectOneRecipeByNameAndUser(name, user.getUsername());
        if (recipe != null) {
            System.out.println("Resepti " + name + " haettu");            
        } else {
            System.out.println("Reseptiä " + name + " ei ole olemassa");
        }

        return recipe;
    }

    @Override
    public List<Recipe> findByType(String type) {
        return connection.selectAllRecipesByType(type);
    }

    @Override
    public List<Recipe> findByUser(User user) {
        return connection.selectAllRecipesByUser(user.getUsername());
    }

    @Override
    public List<Recipe> findAll() {
        return connection.selectAllRecipes();
    }

    @Override
    public boolean update(String newName, int newPortion, String newType, Recipe recipe) {
        return connection.updateRecipe(newName, newPortion, newType, recipe.getId());
    }

    @Override
    public boolean remove(Recipe recipe) {
        return connection.deleteRecipe(recipe.getId());
    }
    
}
