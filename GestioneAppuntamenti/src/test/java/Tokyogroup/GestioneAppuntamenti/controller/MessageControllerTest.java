package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class MessageControllerTest {

	private MessageController User;
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
        User = new MessageController(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetAllManagers() {
        List<User> managers = User.getAllManagers();
        assertNotNull(managers);
        assertFalse(managers.isEmpty());
    }

    @Test
    void testSendMessageSuccess() {
        boolean result = User.sendMessage(2, "Test message");
        assertTrue(result);
    }

    @Test
    void testSendMessageFailure() {
    	assertThrows(Exception.class, () -> {
            User.sendMessage(999, "Test message"); // Assuming 999 is an invalid ID
        });
    }
}