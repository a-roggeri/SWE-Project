package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per CancelAppointmentController.
 */
class CancelAppointmentControllerTest {

    private CancelAppointmentController User;
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
        User = new CancelAppointmentController(testUser);
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
     * Testa il metodo getValidAppointmentsForClient di CancelAppointmentController.
     */
    @Test
    void testGetValidAppointmentsForClient() {
        AppointmentController UserTemp;
        UserTemp = new AppointmentController(testUser);
        List<String> selectedServices = List.of("Taglio", "Piega");
        UserTemp.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        List<String[]> appointments = User.getValidAppointmentsForClient();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    /**
     * Testa il metodo getHairdresserNames di CancelAppointmentController.
     */
    @Test
    void testGetHairdresserNames() {
        Map<Integer, String> hairdresserNames = User.getHairdresserNames();
        assertNotNull(hairdresserNames);
        assertFalse(hairdresserNames.isEmpty());
    }

    /**
     * Testa il metodo cancelAppointment di CancelAppointmentController.
     */
    @Test
    void testCancelAppointment() {
        AppointmentController UserTemp;
        UserTemp = new AppointmentController(testUser);
        List<String> selectedServices = List.of("Taglio", "Piega");
        UserTemp.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        boolean success = User.cancelAppointment(1);
        assertTrue(success);
    }
}