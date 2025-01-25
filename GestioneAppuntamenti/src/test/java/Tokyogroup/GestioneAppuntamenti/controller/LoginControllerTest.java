package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.awt.GraphicsEnvironment;
import static org.junit.jupiter.api.Assertions.*;



class LoginControllerTest {

	private LoginController User;
    private User testUser;
    private User testHairdresser;
    private UserDAO userDAO;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        testUser = new User(1, "testUser", "password", "CLIENTE", true);
        testHairdresser = new User(2, "hairdresser", "password", "GESTORE", true);
        userDAO = UserDAO.getInstance();
		userDAO.addUser(testUser);
		userDAO.addUser(testHairdresser);
        ServiceDAO sDAO = new ServiceDAO();  
        sDAO.addService(new Service(1, "Taglio", 10));
        sDAO.addService(new Service(2, "Piega", 12));
        sDAO.addServiceToHairdresser(2, 1);
        sDAO.addServiceToHairdresser(2, 2);
        User = new LoginController();
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testAuthenticateSuccess() {
        User user = User.authenticate("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testAuthenticateFailure() {
        User user = User.authenticate("testUser", "wrongPassword");
        assertNull(user);
    }

    @Test
    void testAuthenticateEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
        	User.authenticate("", "password");
        });
    }

    @Test
    void testAuthenticateEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
        	User.authenticate("testUser", "");
        });
    }

    @Test
    void testRestoreAccountSuccess() throws SQLException {
        boolean success = User.restoreAccount(testUser);
        assertTrue(success);
    }

    @Test
    void testRestoreAccountFailure() throws SQLException {
        User nonExistentUser = new User(999, "nonExistentUser", "password", "CLIENTE", true);
        boolean success = User.restoreAccount(nonExistentUser);
        assertFalse(success);
    }

    @Test
    void testHandleSuccessfulLogin() {
    	if (GraphicsEnvironment.isHeadless()) {
    	    System.out.println("Test ignorato in ambiente headless");
    	    return;
    	}
        assertDoesNotThrow(() -> {
        	User.handleSuccessfulLogin(testUser);
        });
    }

    @Test
    void testHandleFailedLogin() {
        assertThrows(RuntimeException.class, () -> {
        	User.handleFailedLogin("testUser");
        });
    }

    @Test
    void testOpenRegistration() {
    	if (GraphicsEnvironment.isHeadless()) {
    	    System.out.println("Test ignorato in ambiente headless");
    	    return;
    	}
        assertDoesNotThrow(() -> {
        	User.openRegistration();
        });
    }
}