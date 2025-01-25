package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Message;
import Tokyogroup.GestioneAppuntamenti.model.MessageDAO;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per ViewMessagesController.
 */
class ViewMessagesControllerTest {

    private ViewMessagesController Hairdresser;
    private User testUser;
    private User testHairdresser;
    private UserDAO userDAO;

    /**
     * Effettua il backup del database prima di eseguire tutti i test.
     *
     * @throws Exception se si verifica un errore durante il backup del database.
     */
    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    /**
     * Configura l'ambiente di test prima di ogni test.
     *
     * @throws Exception se si verifica un errore durante la configurazione.
     */
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
        Hairdresser = new ViewMessagesController(2);
    }

    /**
     * Ripristina il database dopo ogni test.
     *
     * @throws Exception se si verifica un errore durante il ripristino del database.
     */
    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    /**
     * Testa il metodo getUnreadMessages di ViewMessagesController.
     */
    @Test
    void testGetUnreadMessages() {
        MessageController userTmp = new MessageController(testUser);
        userTmp.sendMessage(2, "Test message");
        List<Message> unreadMessages = Hairdresser.getUnreadMessages();
        assertNotNull(unreadMessages);
    }

    /**
     * Testa il metodo markMessageAsRead di ViewMessagesController.
     */
    @Test
    void testMarkMessageAsRead() {
        MessageController userTmp = new MessageController(testUser);
        userTmp.sendMessage(2, "Test message");
        List<Message> unreadMessages = Hairdresser.getUnreadMessages();
        assertFalse(unreadMessages.isEmpty());

        int messageId = unreadMessages.get(0).getId();
        boolean success = Hairdresser.markMessageAsRead(messageId);
        assertTrue(success);

        MessageDAO messageDAO = MessageDAO.getInstance();
        List<Message> message = messageDAO.getUnreadMessages(2);
        assertTrue(message.isEmpty());
    }
}