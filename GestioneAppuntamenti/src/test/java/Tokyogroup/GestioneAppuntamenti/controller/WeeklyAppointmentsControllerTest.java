package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;



class WeeklyAppointmentsControllerTest {

	private AppointmentController User;
    private WeeklyAppointmentsController Hairdresser;
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
        Hairdresser = new WeeklyAppointmentsController(testHairdresser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testGetWeeklyAppointments() {
    	List<String> selectedServices = List.of("Taglio", "Piega");
        User.bookAppointment(2, LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + (LocalDate.now().getDayOfMonth() + 1), "11:00", selectedServices);
        Map<Integer, List<String[]>> weeklyAppointments = Hairdresser.getWeeklyAppointments();
        assertNotNull(weeklyAppointments);
        assertFalse(weeklyAppointments.isEmpty());
    }

    @Test
    void testCalculateWeeklyRevenue() {
        double revenue = Hairdresser.calculateWeeklyRevenue();
        assertTrue(revenue >= 0);
    }
}