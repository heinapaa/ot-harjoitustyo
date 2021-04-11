package generator.dao;

import generator.domain.Ingredient;

public interface IngredientDao {
    
    void create(Ingredient ingredient) throws Exception;
    
}
