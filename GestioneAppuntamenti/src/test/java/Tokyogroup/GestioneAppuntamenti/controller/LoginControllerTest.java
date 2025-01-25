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

/**
 * Classe di test per LoginController.
 */
class LoginControllerTest {

    private LoginController User;
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
        User = new LoginController();
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
     * Testa l'autenticazione con successo.
     */
    @Test
    void testAuthenticateSuccess() {
        User user = User.authenticate("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    /**
     * Testa l'autenticazione con fallimento.
     */
    @Test
    void testAuthenticateFailure() {
        User user = User.authenticate("testUser", "wrongPassword");
        assertNull(user);
    }

    /**
     * Testa l'autenticazione con username vuoto.
     */
    @Test
    void testAuthenticateEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.authenticate("", "password");
        });
    }

    /**
     * Testa l'autenticazione con password vuota.
     */
    @Test
    void testAuthenticateEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.authenticate("testUser", "");
        });
    }

    /**
     * Testa il ripristino dell'account con successo.
     *
     * @throws SQLException se si verifica un errore durante il ripristino dell'account.
     */
    @Test
    void testRestoreAccountSuccess() throws SQLException {
        boolean success = User.restoreAccount(testUser);
        assertTrue(success);
    }

    /**
     * Testa il ripristino dell'account con fallimento.
     *
     * @throws SQLException se si verifica un errore durante il ripristino dell'account.
     */
    @Test
    void testRestoreAccountFailure() throws SQLException {
        User nonExistentUser = new User(999, "nonExistentUser", "password", "CLIENTE", true);
        boolean success = User.restoreAccount(nonExistentUser);
        assertFalse(success);
    }

    /**
     * Testa la gestione del login con successo.
     */
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

    /**
     * Testa la gestione del login fallito.
     */
    @Test
    void testHandleFailedLogin() {
        assertThrows(RuntimeException.class, () -> {
            User.handleFailedLogin("testUser");
        });
    }

    /**
     * Testa l'apertura della registrazione.
     */
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