import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

package Tokyogroup.GestioneAppuntamenti.controller;


class ModifyServicesControllerTest {

    private ModifyServicesController modifyServicesController;
    private User hairdresser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        hairdresser = new User(2, "hairdresser", "password", "GESTORE", true);
        modifyServicesController = new ModifyServicesController(hairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetServicesForHairdresser() {
        List<Service> services = modifyServicesController.getServicesForHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void testRemoveServiceFromHairdresser() {
        boolean result = modifyServicesController.removeServiceFromHairdresser(1);
        assertTrue(result);
    }

    @Test
    void testGetAvailableServicesForHairdresser() {
        List<Service> availableServices = modifyServicesController.getAvailableServicesForHairdresser();
        assertNotNull(availableServices);
        assertFalse(availableServices.isEmpty());
    }

    @Test
    void testAddServiceToHairdresser() {
        boolean result = modifyServicesController.addServiceToHairdresser(1);
        assertTrue(result);
    }

    @Test
    void testAddNewService() {
        int newServiceId = modifyServicesController.addNewService("New Service", 50.0);
        assertTrue(newServiceId > 0);
    }

    @Test
    void testRemoveServiceAndCancelAppointments() {
        boolean result = modifyServicesController.removeServiceAndCancelAppointments(1);
        assertTrue(result);
    }
}