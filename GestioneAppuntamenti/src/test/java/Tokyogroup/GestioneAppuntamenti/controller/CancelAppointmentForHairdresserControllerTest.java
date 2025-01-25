package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;



class CancelAppointmentForHairdresserControllerTest {

    private CancelAppointmentForHairdresserController Hairdresser;
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
        Hairdresser = new CancelAppointmentForHairdresserController(testHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetValidAppointmentsForHairdresser() {
    	AppointmentController UserTemp;
        UserTemp =  new AppointmentController(testUser);
    	List<String> selectedServices = List.of("Taglio", "Piega");
        UserTemp.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        List<String[]> appointments = Hairdresser.getValidAppointmentsForHairdresser();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    @Test
    void testGetClientNames() {
        Map<Integer, String> clientNames = Hairdresser.getClientNames();
        assertNotNull(clientNames);
        assertFalse(clientNames.isEmpty());
    }

    @Test
    void testCancelAppointment() {
    	AppointmentController UserTemp;
        UserTemp =  new AppointmentController(testUser);
    	List<String> selectedServices = List.of("Taglio", "Piega");
        UserTemp.bookAppointment(2, "2025-10-10", "11:00", selectedServices);
        boolean success = Hairdresser.cancelAppointment(1);
        assertTrue(success);
    }
}