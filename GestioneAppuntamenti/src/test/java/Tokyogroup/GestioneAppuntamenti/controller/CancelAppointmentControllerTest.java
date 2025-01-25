package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;





class CancelAppointmentControllerTest {

    private CancelAppointmentController cancelAppointmentController;
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
        cancelAppointmentController = new CancelAppointmentController(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetValidAppointmentsForClient() {
        List<String[]> appointments = cancelAppointmentController.getValidAppointmentsForClient();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    @Test
    void testGetHairdresserNames() {
        Map<Integer, String> hairdresserNames = cancelAppointmentController.getHairdresserNames();
        assertNotNull(hairdresserNames);
        assertFalse(hairdresserNames.isEmpty());
    }

    @Test
    void testCancelAppointment() {
        boolean success = cancelAppointmentController.cancelAppointment(1);
        assertTrue(success);
    }
}