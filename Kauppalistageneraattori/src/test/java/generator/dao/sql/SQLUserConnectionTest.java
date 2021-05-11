package generator.dao.sql;

import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SQLUserConnectionTest {

    private FakeSQLUserConnection conn;
    
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        this.conn = new FakeSQLUserConnection();
        conn.createUserTable();
    }
    
    @Test
    public void usersCanBeAdded() throws SQLException {
        conn.insertUser("tester");
        assertEquals(1, conn.selectAllUsers().size());
        assertEquals("tester", conn.selectAllUsers().get(0).getUsername());
    }
    
    @Test
    public void usersCanBeSelected() throws SQLException {
        conn.insertUser("tester1");
        conn.insertUser("tester2");
        assertEquals("tester1", conn.selectOneUser("tester1").getUsername());
        assertEquals("tester2", conn.selectOneUser("tester2").getUsername());
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.closeConnection();
    }  
}
