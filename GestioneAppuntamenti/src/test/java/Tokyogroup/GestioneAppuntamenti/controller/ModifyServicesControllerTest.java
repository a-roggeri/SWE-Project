package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class ModifyServicesControllerTest {

    private ModifyServicesController Hairdresser;
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
        Hairdresser = new ModifyServicesController(testHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetServicesForHairdresser() {
        List<Service> services = Hairdresser.getServicesForHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testRemoveServiceFromHairdresser() {
        boolean result = Hairdresser.removeServiceFromHairdresser(1);
        assertTrue(result);
    }

    @Test
    void testGetAvailableServicesForHairdresser() {
        List<Service> availableServices = Hairdresser.getAvailableServicesForHairdresser();
        assertNotNull(availableServices);
        assertFalse(availableServices.isEmpty());
    }

    @Test
    void testAddServiceToHairdresser() {
        boolean result = Hairdresser.addServiceToHairdresser(2);
        assertTrue(result);
    }

    @Test
    void testAddNewService() {
        int newServiceId = Hairdresser.addNewService("New Service", 50.0);
        assertTrue(newServiceId > 0);
    }

    @Test
    void testRemoveServiceAndCancelAppointments() {
        boolean result = Hairdresser.removeServiceAndCancelAppointments(1);
        assertTrue(result);
    }
}