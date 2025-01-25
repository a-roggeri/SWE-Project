package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;





class AppointmentControllerTest {

    private AppointmentController User;
    private AppointmentController Hairdresser;
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
        User = new AppointmentController(testUser);
        Hairdresser = new AppointmentController(testHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetCurrentUser() {
        User currentUser = User.getCurrentUser();
        assertEquals(testUser, currentUser);
    }

    @Test
    void testGetAvailableHairdressers() {
        List<User> hairdressers = User.getAvailableHairdressers();
        assertNotNull(hairdressers);
        assertFalse(hairdressers.isEmpty());
    }

    @Test
    void testGetServicesForHairdresser() {
        
        List<Service> services = Hairdresser.getServicesForHairdresser(testHairdresser);
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testGetAvailableHours() {
        List<String> availableHours = User.getAvailableHours(2, "2025-10-10");
        assertNotNull(availableHours);
        assertFalse(availableHours.isEmpty());
    }

    @Test
    void testBookAppointment() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        boolean success = User.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        assertTrue(success);
    }

    @Test
    void testIsDateTimeValid() {
        assertTrue(User.isDateTimeValid("2025-10-10", "10:00"));
        assertFalse(User.isDateTimeValid("2020-10-10", "10:00"));
    }
}