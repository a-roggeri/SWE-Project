package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;





class ChangePasswordControllerTest {

	private ChangePasswordController User;
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
        User = new ChangePasswordController(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testChangePasswordSuccess() throws SQLException {
        boolean success = User.changePassword("password", "newPassword");
        assertTrue(success);
        assertNotNull(userDAO.findUser("testUser", "newPassword"));
    }

    @Test
    void testChangePasswordFailure() throws SQLException {
        boolean success = User.changePassword("wrongOldPassword", "newPassword");
        assertFalse(success);
        assertNull(userDAO.findUser("testUser", "newPassword"));
    }

    @Test
    void testChangePasswordSQLException() throws SQLException, IOException {
        DatabaseManager.deleteDatabaseFiles();
        assertThrows(RuntimeException.class, () -> {
        	User.changePassword("password", "newPassword");
        });
    }
}