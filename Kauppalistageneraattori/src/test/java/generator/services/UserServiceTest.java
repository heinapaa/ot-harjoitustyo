package generator.services;

import generator.services.UserService;
import generator.services.InputValidator;
import generator.models.User;
import generator.dao.UserDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserServiceTest {
    
    private UserDao userDao;
    private UserService userService;
    private User user;
    
    @Before
    public void setUp() {
        this.user = new User("tester");        
        this.userDao = new FakeUserDao();
        userDao.create(user);
        this.userService = new UserService(userDao, new InputValidator(new ArrayList<String>()));
    }
    
    @Test
    public void existingUsercanLogIn() {
        assertTrue(userService.login("tester"));
        assertEquals(user, userService.getLoggedIn());
    }
    
    @Test
    public void nonExistingUsercantLogIn() {
        assertFalse(userService.login("fakeTester"));
    }
    
    @Test
    public void canLogOut() {
        userService.login("tester");
        userService.logout();
        assertEquals(null, userService.getLoggedIn());
    }
    
    @Test
    public void canAddNewUser() {
        assertTrue(userService.addNewUser("newTester"));
        User newUser = userDao.findByUsername("newTester");
        assertTrue(newUser != null);
    }
    
    @Test
    public void cantAddExistingUser() {
        assertFalse(userService.addNewUser("tester"));
    }
    
    @Test
    public void cantChooseBadUsername() {
        assertFalse(userService.addNewUser("tes;;ter"));
        assertFalse(userService.addNewUser(""));
        assertFalse(userService.addNewUser("   "));     
    }
}
