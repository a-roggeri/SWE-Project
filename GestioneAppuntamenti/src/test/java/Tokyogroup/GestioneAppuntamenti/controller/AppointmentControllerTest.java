package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;





class AppointmentControllerTest {

    private AppointmentController appointmentController;
    private User testUser;
    private User hairdresser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        testUser = new User(1, "testUser", "password", "CLIENTE", true);
        hairdresser = new User(2, "hairdresser", "password", "GESTORE", true);

        appointmentController = new AppointmentController(testUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetCurrentUser() {
        User currentUser = appointmentController.getCurrentUser();
        assertEquals(testUser, currentUser);
    }

    @Test
    void testGetAvailableHairdressers() {
        List<User> hairdressers = appointmentController.getAvailableHairdressers();
        assertNotNull(hairdressers);
        assertFalse(hairdressers.isEmpty());
    }

    @Test
    void testGetServicesForHairdresser() {
        ServiceDAO sDAO = new ServiceDAO();  
        sDAO.addService(new Service(4, "Taglio", 10));
        sDAO.addServiceToHairdresser(2, 4);
        List<Service> services = appointmentController.getServicesForHairdresser(hairdresser);
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testGetAvailableHours() {
        List<String> availableHours = appointmentController.getAvailableHours(2, "2023-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    @Test
    void testBookAppointment() {
        List<String> selectedServices = List.of("Taglio", "Colore");
        boolean success = appointmentController.bookAppointment(2, "2023-10-10", "10:00", selectedServices);
        assertTrue(success);
    }

    @Test
    void testIsDateTimeValid() {
        assertTrue(appointmentController.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(appointmentController.isDateTimeValid("2020-10-10", "10:00"));
    }
}