import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.MessageDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class MessageControllerTest {

    private MessageController messageController;
    private User testUser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        testUser = new User(1, "testUser", "password", "CLIENTE", true);
        messageController = new MessageController(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetAllManagers() {
        List<User> managers = messageController.getAllManagers();
        assertNotNull(managers);
        assertFalse(managers.isEmpty());
    }

    @Test
    void testSendMessageSuccess() {
        boolean result = messageController.sendMessage(2, "Test message");
        assertTrue(result);
    }

    @Test
    void testSendMessageFailure() {
        boolean result = messageController.sendMessage(999, "Test message"); // Assuming 999 is an invalid ID
        assertFalse(result);
    }
}