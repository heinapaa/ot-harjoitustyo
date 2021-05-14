package generator.dao;

import generator.dao.sql.SQLRecipeConnection;
import java.sql.SQLException;

public class FakeSQLRecipeConnection extends SQLRecipeConnection {
    
    public FakeSQLRecipeConnection() throws ClassNotFoundException {
        super("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");  
    }

    public void closeConnection() throws SQLException {
        connect().createStatement().execute("DROP ALL OBJECTS");
    }    
    
}
