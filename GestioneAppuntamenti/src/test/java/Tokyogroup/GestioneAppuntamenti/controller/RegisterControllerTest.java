package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

/**
 * Classe di test per RegisterController.
 */
class RegisterControllerTest {

    private RegisterController User;
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
        User = new RegisterController();
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
     * Testa il metodo registerUser di RegisterController per un caso di successo.
     */
    @Test
    void testRegisterUserSuccess() {
        boolean result = User.registerUser("newuser", "password", "CLIENTE");
        assertTrue(result);
    }

    /**
     * Testa il metodo registerUser di RegisterController con campi vuoti.
     */
    @Test
    void testRegisterUserWithEmptyFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.registerUser("", "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            User.registerUser("newuser", "", "CLIENTE");
        });
    }

    /**
     * Testa il metodo registerUser di RegisterController con campi null.
     */
    @Test
    void testRegisterUserWithNullFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.registerUser(null, "password", "CLIENTE");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            User.registerUser("newuser", null, "CLIENTE");
        });
    }

    /**
     * Testa il metodo registerUser di RegisterController in caso di errore del database.
     *
     * @throws IOException se si verifica un errore durante l'eliminazione dei file del database.
     */
    @Test
    void testRegisterUserDatabaseError() throws IOException {
        DatabaseManager.deleteDatabaseFiles();
        assertThrows(RuntimeException.class, () -> {
            User.registerUser("newuser", "password", "CLIENTE");
        });
    }
}