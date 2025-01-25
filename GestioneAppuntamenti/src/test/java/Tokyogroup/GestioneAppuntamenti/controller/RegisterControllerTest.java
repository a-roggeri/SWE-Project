package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;




class RegisterControllerTest {

	private RegisterController User;
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
        User = new RegisterController();
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testRegisterUserSuccess() {
        boolean result = User.registerUser("newuser", "password", "CLIENTE");
        assertTrue(result);
    }

    @Test
    void testRegisterUserWithEmptyFields() {
        assertThrows(IllegalArgumentException.class, () -> {
        	User.registerUser("", "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
        	User.registerUser("newuser", "", "CLIENTE");
        });
    }

    @Test
    void testRegisterUserWithNullFields() {
        assertThrows(IllegalArgumentException.class, () -> {
        	User.registerUser(null, "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
        	User.registerUser("newuser", null, "CLIENTE");
        });
    }

    @Test
    void testRegisterUserDatabaseError() throws IOException {
        DatabaseManager.deleteDatabaseFiles();
        assertThrows(RuntimeException.class, () -> {
        	User.registerUser("newuser", "password", "CLIENTE");
        });
    }
}