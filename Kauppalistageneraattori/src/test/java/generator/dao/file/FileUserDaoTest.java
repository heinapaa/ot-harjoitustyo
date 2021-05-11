package generator.dao.file;

import generator.dao.UserDao;
import generator.dao.file.FileUserDao;
import generator.models.User;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class FileUserDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();    
    
    File userFile;
    UserDao userDao;
    
    
    @Before
    public void setUp() throws Exception {
        userFile = testFolder.newFile("testfile_users.txt");  
        
        try (FileWriter file = new FileWriter(userFile.getAbsolutePath())) {
            file.write("eka\ntestaaja\ntestaaja2\nTeuvo Testaaja");
        }
        
        userDao = new FileUserDao(userFile.getAbsolutePath());          
    }
    
    @Test
    public void usersAreReadCorrectly() {
        List<User> users = userDao.findAll();
        assertEquals(4, users.size());
        User user= users.get(0);
        assertEquals("eka", user.getUsername());
    }    
    
    @Test
    public void usersAreCreatedCorrectly() {
        userDao.create(new User("uusi"));
        List<User> users = userDao.findAll();
        assertEquals(5, users.size());
        User user= users.get(4);
        assertEquals("uusi", user.getUsername());
    }     
    
    @After
    public void tearDown() {
        userFile.delete();
    }
    
}