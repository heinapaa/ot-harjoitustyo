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
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        this.userDao = new SQLUserDao("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        userDao.create(new User("eka"));
        userDao.create(new User("testaaja"));
        userDao.create(new User("testaaja2"));
        userDao.create(new User("Teuvo Testaaja"));
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
        userDao.closeConnection();
    }
    
}
