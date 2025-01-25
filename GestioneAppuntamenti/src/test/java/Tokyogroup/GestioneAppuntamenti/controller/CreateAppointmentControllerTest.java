import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class CreateAppointmentControllerTest {

    private CreateAppointmentController createAppointmentController;
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
        createAppointmentController = new CreateAppointmentController(currentHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetClients() {
        List<User> clients = createAppointmentController.getClients();
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    void testGetServicesForCurrentHairdresser() {
        List<Service> services = createAppointmentController.getServicesForCurrentHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testGetAvailableHoursForDate() {
        List<String> availableHours = createAppointmentController.getAvailableHoursForDate("2023-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    @Test
    void testCreateAppointmentSuccess() {
        List<String> selectedServices = List.of("Taglio", "Colore");
        boolean success = createAppointmentController.createAppointment(1, "2023-10-10", "10:00", selectedServices);
        assertTrue(success);
    }

    @Test
    void testCreateAppointmentInvalidClientId() {
        List<String> selectedServices = List.of("Taglio", "Colore");
        assertThrows(IllegalArgumentException.class, () -> {
            createAppointmentController.createAppointment(-1, "2023-10-10", "10:00", selectedServices);
        });
    }

    @Test
    void testCreateAppointmentInvalidDateTime() {
        List<String> selectedServices = List.of("Taglio", "Colore");
        assertThrows(IllegalArgumentException.class, () -> {
            createAppointmentController.createAppointment(1, "2020-10-10", "10:00", selectedServices);
        });
    }

    @Test
    void testIsDateTimeValid() {
        assertTrue(createAppointmentController.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(createAppointmentController.isDateTimeValid("2020-10-10", "10:00"));
    }
}