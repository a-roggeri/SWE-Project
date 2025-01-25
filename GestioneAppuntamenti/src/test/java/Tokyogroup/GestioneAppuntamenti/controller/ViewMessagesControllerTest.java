import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Message;
import Tokyogroup.GestioneAppuntamenti.model.MessageDAO;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class ViewMessagesControllerTest {

    private ViewMessagesController viewMessagesController;
    private int managerId;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();
        managerId = 1; // Assumendo che il manager con ID 1 esista nel database
        viewMessagesController = new ViewMessagesController(managerId);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetUnreadMessages() {
        List<Message> unreadMessages = viewMessagesController.getUnreadMessages();
        assertNotNull(unreadMessages);
        // Aggiungi ulteriori asserzioni se necessario, ad esempio controllando il numero di messaggi non letti
    }

    @Test
    void testMarkMessageAsRead() {
        // Assumendo che ci sia almeno un messaggio non letto per il manager con ID 1
        List<Message> unreadMessages = viewMessagesController.getUnreadMessages();
        assertFalse(unreadMessages.isEmpty());

        int messageId = unreadMessages.get(0).getId();
        boolean success = viewMessagesController.markMessageAsRead(messageId);
        assertTrue(success);

        // Verifica che il messaggio sia stato segnato come letto
        MessageDAO messageDAO = MessageDAO.getInstance();
        Message message = messageDAO.getMessageById(messageId);
        assertEquals("LETTO", message.getStatus());
    }
}