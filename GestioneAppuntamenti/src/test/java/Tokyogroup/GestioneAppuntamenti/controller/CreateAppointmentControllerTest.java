package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class CreateAppointmentControllerTest {

    private CreateAppointmentController Hairdresser;
    private User testUser;
    private User testHairdresser;
    private UserDAO userDAO;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

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

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetClients() {
        List<User> clients = Hairdresser.getClients();
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    void testGetServicesForCurrentHairdresser() {
        List<Service> services = Hairdresser.getServicesForCurrentHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testGetAvailableHoursForDate() {
        List<String> availableHours = Hairdresser.getAvailableHoursForDate("2025-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    @Test
    void testCreateAppointmentSuccess() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        boolean success = Hairdresser.createAppointment(1, "2025-10-10", "10:00", selectedServices);
        assertTrue(success);
    }

    @Test
    void testCreateAppointmentInvalidClientId() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        assertThrows(IllegalArgumentException.class, () -> {
        	Hairdresser.createAppointment(-1, "2025-10-10", "10:00", selectedServices);
        });
    }

    @Test
    void testCreateAppointmentInvalidDateTime() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        assertThrows(IllegalArgumentException.class, () -> {
        	Hairdresser.createAppointment(1, "2020-10-10", "10:00", selectedServices);
        });
    }

    @Test
    void testIsDateTimeValid() {
        assertTrue(Hairdresser.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(Hairdresser.isDateTimeValid("2020-10-10", "10:00"));
    }
}