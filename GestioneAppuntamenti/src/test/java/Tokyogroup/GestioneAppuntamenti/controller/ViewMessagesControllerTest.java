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



class ViewMessagesControllerTest {

    private ViewMessagesController Hairdresser;
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
        Hairdresser = new ViewMessagesController(2);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetUnreadMessages() {
    	MessageController userTmp = new MessageController(testUser);
    	userTmp.sendMessage(2, "Test message");
        List<Message> unreadMessages = Hairdresser.getUnreadMessages();
        assertNotNull(unreadMessages);
        // Aggiungi ulteriori asserzioni se necessario, ad esempio controllando il numero di messaggi non letti
    }

    @Test
    void testMarkMessageAsRead() {
    	MessageController userTmp = new MessageController(testUser);
    	userTmp.sendMessage(2, "Test message");
        List<Message> unreadMessages = Hairdresser.getUnreadMessages();
        assertFalse(unreadMessages.isEmpty());

        int messageId = unreadMessages.get(0).getId();
        boolean success = Hairdresser.markMessageAsRead(messageId);
        assertTrue(success);

        // Verifica che il messaggio sia stato segnato come letto
        MessageDAO messageDAO = MessageDAO.getInstance();
        List<Message> message = messageDAO.getUnreadMessages(2);
        assertTrue(message.isEmpty());
    }
}