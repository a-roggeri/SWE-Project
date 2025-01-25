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
 * Classe di test per CreateAppointmentController.
 */
class CreateAppointmentControllerTest {

    private CreateAppointmentController Hairdresser;
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
        Hairdresser = new CreateAppointmentController(testHairdresser);
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
     * Testa il metodo getClients di CreateAppointmentController.
     */
    @Test
    void testGetClients() {
        List<User> clients = Hairdresser.getClients();
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    /**
     * Testa il metodo getServicesForCurrentHairdresser di CreateAppointmentController.
     */
    @Test
    void testGetServicesForCurrentHairdresser() {
        List<Service> services = Hairdresser.getServicesForCurrentHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    /**
     * Testa il metodo getAvailableHoursForDate di CreateAppointmentController.
     */
    @Test
    void testGetAvailableHoursForDate() {
        List<String> availableHours = Hairdresser.getAvailableHoursForDate("2025-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    /**
     * Testa il metodo createAppointment di CreateAppointmentController per un caso di successo.
     */
    @Test
    void testCreateAppointmentSuccess() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        boolean success = Hairdresser.createAppointment(1, "2025-10-10", "10:00", selectedServices);
        assertTrue(success);
    }

    /**
     * Testa il metodo createAppointment di CreateAppointmentController con un ID cliente non valido.
     */
    @Test
    void testCreateAppointmentInvalidClientId() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        assertThrows(IllegalArgumentException.class, () -> {
            Hairdresser.createAppointment(-1, "2025-10-10", "10:00", selectedServices);
        });
    }

    /**
     * Testa il metodo createAppointment di CreateAppointmentController con una data e ora non valide.
     */
    @Test
    void testCreateAppointmentInvalidDateTime() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        assertThrows(IllegalArgumentException.class, () -> {
            Hairdresser.createAppointment(1, "2020-10-10", "10:00", selectedServices);
        });
    }

    /**
     * Testa il metodo isDateTimeValid di CreateAppointmentController.
     */
    @Test
    void testIsDateTimeValid() {
        assertTrue(Hairdresser.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(Hairdresser.isDateTimeValid("2020-10-10", "10:00"));
    }
}