import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class LoginControllerTest {

    private LoginController loginController;
    private User testUser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        loginController = new LoginController();
        testUser = new User(1, "testUser", "password", "CLIENTE", true);
        UserDAO.getInstance().createUser(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testAuthenticateSuccess() {
        User user = loginController.authenticate("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testAuthenticateFailure() {
        User user = loginController.authenticate("testUser", "wrongPassword");
        assertNull(user);
    }

    @Test
    void testAuthenticateEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            loginController.authenticate("", "password");
        });
    }

    @Test
    void testAuthenticateEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            loginController.authenticate("testUser", "");
        });
    }

    @Test
    void testRestoreAccountSuccess() throws SQLException {
        boolean success = loginController.restoreAccount(testUser);
        assertTrue(success);
    }

    @Test
    void testRestoreAccountFailure() throws SQLException {
        User nonExistentUser = new User(999, "nonExistentUser", "password", "CLIENTE", true);
        boolean success = loginController.restoreAccount(nonExistentUser);
        assertFalse(success);
    }

    @Test
    void testHandleSuccessfulLogin() {
        assertDoesNotThrow(() -> {
            loginController.handleSuccessfulLogin(testUser);
        });
    }

    @Test
    void testHandleFailedLogin() {
        assertThrows(RuntimeException.class, () -> {
            loginController.handleFailedLogin("testUser");
        });
    }

    @Test
    void testOpenRegistration() {
        assertDoesNotThrow(() -> {
            loginController.openRegistration();
        });
    }
}