package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per AppointmentController.
 */
class AppointmentControllerTest {

    /**
     * Oggetto AppointmentController per l'utente.
     */
    private AppointmentController User;

    /**
     * Oggetto AppointmentController per il parrucchiere.
     */
    private AppointmentController Hairdresser;

    /**
     * Oggetto User di test.
     */
    private User testUser;

    /**
     * Oggetto User per il parrucchiere di test.
     */
    private User testHairdresser;

    /**
     * Oggetto UserDAO per la gestione degli utenti.
     */
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
        User = new AppointmentController(testUser);
        Hairdresser = new AppointmentController(testHairdresser);
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
     * Testa il metodo getCurrentUser di AppointmentController.
     */
    @Test
    void testGetCurrentUser() {
        User currentUser = User.getCurrentUser();
        assertEquals(testUser, currentUser);
    }

    /**
     * Testa il metodo getAvailableHairdressers di AppointmentController.
     */
    @Test
    void testGetAvailableHairdressers() {
        List<User> hairdressers = User.getAvailableHairdressers();
        assertNotNull(hairdressers);
        assertFalse(hairdressers.isEmpty());
    }

    /**
     * Testa il metodo getServicesForHairdresser di AppointmentController.
     */
    @Test
    void testGetServicesForHairdresser() {
        List<Service> services = Hairdresser.getServicesForHairdresser(testHairdresser);
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    /**
     * Testa il metodo getAvailableHours di AppointmentController.
     */
    @Test
    void testGetAvailableHours() {
        List<String> availableHours = User.getAvailableHours(2, "2025-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    /**
     * Testa il metodo bookAppointment di AppointmentController.
     */
    @Test
    void testBookAppointment() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        boolean success = User.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        assertTrue(success);
    }

    /**
     * Testa il metodo isDateTimeValid di AppointmentController.
     */
    @Test
    void testIsDateTimeValid() {
        assertTrue(User.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(User.isDateTimeValid("2020-10-10", "10:00"));
    }
}