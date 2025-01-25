import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;



class RegisterControllerTest {

    private RegisterController registerController;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();
        registerController = new RegisterController();
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testRegisterUserSuccess() {
        boolean result = registerController.registerUser("newuser", "password", "CLIENTE");
        assertTrue(result);
    }

    @Test
    void testRegisterUserWithEmptyFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser("", "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser("newuser", "", "CLIENTE");
        });
    }

    @Test
    void testRegisterUserWithNullFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser(null, "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser("newuser", null, "CLIENTE");
        });
    }

    @Test
    void testRegisterUserDatabaseError() {
        UserDAO userDAO = UserDAO.getInstance();
        userDAO.closeConnection(); // Simulate database error
        assertThrows(RuntimeException.class, () -> {
            registerController.registerUser("newuser", "password", "CLIENTE");
        });
    }
}