import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class WeeklyAppointmentsControllerTest {

    private WeeklyAppointmentsController weeklyAppointmentsController;
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
        weeklyAppointmentsController = new WeeklyAppointmentsController(currentHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetWeeklyAppointments() {
        Map<Integer, List<String[]>> weeklyAppointments = weeklyAppointmentsController.getWeeklyAppointments();
        assertNotNull(weeklyAppointments);
        assertFalse(weeklyAppointments.isEmpty());
    }

    @Test
    void testCalculateWeeklyRevenue() {
        double revenue = weeklyAppointmentsController.calculateWeeklyRevenue();
        assertTrue(revenue >= 0);
    }
}