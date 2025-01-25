package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per ModifyServicesController.
 */
class ModifyServicesControllerTest {

    private ModifyServicesController Hairdresser;
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
        Hairdresser = new ModifyServicesController(testHairdresser);
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
     * Testa il metodo getServicesForHairdresser di ModifyServicesController.
     */
    @Test
    void testGetServicesForHairdresser() {
        List<Service> services = Hairdresser.getServicesForHairdresser();
        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    /**
     * Testa il metodo removeServiceFromHairdresser di ModifyServicesController.
     */
    @Test
    void testRemoveServiceFromHairdresser() {
        boolean result = Hairdresser.removeServiceFromHairdresser(1);
        assertTrue(result);
    }

    /**
     * Testa il metodo getAvailableServicesForHairdresser di ModifyServicesController.
     */
    @Test
    void testGetAvailableServicesForHairdresser() {
        List<Service> availableServices = Hairdresser.getAvailableServicesForHairdresser();
        assertNotNull(availableServices);
        assertFalse(availableServices.isEmpty());
    }

    /**
     * Testa il metodo addServiceToHairdresser di ModifyServicesController.
     */
    @Test
    void testAddServiceToHairdresser() {
        boolean result = Hairdresser.addServiceToHairdresser(2);
        assertTrue(result);
    }

    /**
     * Testa il metodo addNewService di ModifyServicesController.
     */
    @Test
    void testAddNewService() {
        int newServiceId = Hairdresser.addNewService("New Service", 50.0);
        assertTrue(newServiceId > 0);
    }

    /**
     * Testa il metodo removeServiceAndCancelAppointments di ModifyServicesController.
     */
    @Test
    void testRemoveServiceAndCancelAppointments() {
        boolean result = Hairdresser.removeServiceAndCancelAppointments(1);
        assertTrue(result);
    }
}