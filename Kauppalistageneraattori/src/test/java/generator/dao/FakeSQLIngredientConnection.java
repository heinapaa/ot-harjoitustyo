package generator.dao;

import generator.dao.sql.SQLIngredientConnection;
import java.sql.SQLException;

public class FakeSQLIngredientConnection extends SQLIngredientConnection {        
    
    public FakeSQLIngredientConnection() throws ClassNotFoundException {
        super("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");  
    }

    public void closeConnection() throws SQLException {
        connect().createStatement().execute("DROP ALL OBJECTS");
    }
    
}
