package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;



class CancelAppointmentForHairdresserControllerTest {

    private CancelAppointmentForHairdresserController cancelAppointmentForHairdresserController;
    private User currentHairdresser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        currentHairdresser = new User(2, "hairdresser", "password", "GESTORE", true);
        cancelAppointmentForHairdresserController = new CancelAppointmentForHairdresserController(currentHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetValidAppointmentsForHairdresser() {
        List<String[]> appointments = cancelAppointmentForHairdresserController.getValidAppointmentsForHairdresser();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    @Test
    void testGetClientNames() {
        Map<Integer, String> clientNames = cancelAppointmentForHairdresserController.getClientNames();
        assertNotNull(clientNames);
        assertFalse(clientNames.isEmpty());
    }

    @Test
    void testCancelAppointment() {
        boolean success = cancelAppointmentForHairdresserController.cancelAppointment(1);
        assertTrue(success);
    }
}