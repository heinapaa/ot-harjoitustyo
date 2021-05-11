package generator.dao.sql;

import generator.dao.sql.SQLUserConnection;

import java.sql.SQLException;

public class FakeSQLUserConnection extends SQLUserConnection{

    public FakeSQLUserConnection() throws ClassNotFoundException {
        super("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");  
    }

    public void closeConnection() throws SQLException {
        connect().createStatement().execute("DROP ALL OBJECTS");
    }
}
