package generator.dao;

import generator.dao.sql.SQLUserDao;
import generator.models.User;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SQLUserDaoTest {

    private SQLUserDao userDao;
    private FakeSQLUserConnection conn;
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        this.conn = new FakeSQLUserConnection();
        this.userDao = new SQLUserDao(conn);
        conn.insertUser("eka");
        conn.insertUser("testaaja");
        conn.insertUser("testaaja2");
        conn.insertUser("Teuvo Testaaja");
    }

    
    @Test
    public void usersAreReadCorrectly() {
        List<User> users = userDao.findAll();
        assertEquals(4, users.size());
        assertTrue(users.contains(new User("eka")));
        assertTrue(users.contains(new User("testaaja")));
        assertTrue(users.contains(new User("testaaja2")));
        assertTrue(users.contains(new User("Teuvo Testaaja")));         
    }    
    
    @Test
    public void usersAreCreatedCorrectly() {
        User u1 = new User("uusi");
        userDao.create(u1);
        List<User> users = userDao.findAll();
        assertEquals(5, users.size());
        assertTrue(users.contains(u1));     
    }  
    
    @Test
    public void canFindUserByUsername() {
        User u1 = new User("tester");
        userDao.create(u1);
        assertEquals(u1, userDao.findByUsername("tester"));
        assertNull(userDao.findByUsername("fake user"));
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.closeConnection();
    }
    
}