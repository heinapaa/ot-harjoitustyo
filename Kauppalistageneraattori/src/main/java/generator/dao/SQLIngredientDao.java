package generator.dao;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import java.util.List;

public class SQLIngredientDao implements IngredientDao {
    
    private SQLConnection connection;
    
    public SQLIngredientDao(SQLConnection connection) {
        this.connection = connection;
        connection.createIngredientTable();
    }

    @Override
    public boolean create(Ingredient ingredient) {
        return connection.insertIngredient(ingredient.getName(), ingredient.getAmount(), ingredient.getUnit().toString(), ingredient.getRecipe().getId());
    }

    @Override
    public boolean remove(Ingredient ingredient) {
        return connection.deleteIngredient(ingredient.getName(), ingredient.getRecipe().getId());
    }

    @Override
    public boolean removeByRecipe(Recipe recipe) {
        return connection.deleteAllIngredientsByRecipe(recipe.getId());
    }

    @Override
    public Ingredient findByNameAndRecipe(String name, Recipe recipe) {
        Ingredient ingredient = connection.selectOneIngredientByNameAndRecipe(name, recipe.getId());
        if (ingredient != null) {
            ingredient.setRecipe(recipe);
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> findByRecipe(Recipe recipe) {
        return connection.selectAllIngredientsByRecipe(recipe.getId());
    }

    @Override
    public List<Ingredient> findAll() {
        return connection.selectAllIngredients();
    }
    
}
