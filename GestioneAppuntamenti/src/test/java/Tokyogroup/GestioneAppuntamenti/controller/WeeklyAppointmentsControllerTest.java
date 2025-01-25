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

/**
 * Classe di test per il controller degli appuntamenti settimanali.
 */
class WeeklyAppointmentsControllerTest {

    private AppointmentController User;
    private WeeklyAppointmentsController Hairdresser;
    private User testUser;
    private User testHairdresser;
    private UserDAO userDAO;

    /**
     * Effettua il backup del database prima di eseguire tutti i test.
     * @throws Exception se si verifica un errore durante il backup del database.
     */
    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    /**
     * Configura l'ambiente di test prima di ogni test.
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
        sDAO.addServiceToHairdresser(2, 2);
        User = new AppointmentController(testUser);
        Hairdresser = new WeeklyAppointmentsController(testHairdresser);
    }

    /**
     * Ripristina il database dopo ogni test.
     * @throws Exception se si verifica un errore durante il ripristino del database.
     */
    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    /**
     * Testa il metodo getWeeklyAppointments del controller degli appuntamenti settimanali.
     */
    @Test
    void testGetWeeklyAppointments() {
        List<String> selectedServices = List.of("Taglio", "Piega");
        User.bookAppointment(2, LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + (LocalDate.now().getDayOfMonth() + 1), "11:00", selectedServices);
        Map<Integer, List<String[]>> weeklyAppointments = Hairdresser.getWeeklyAppointments();
        assertNotNull(weeklyAppointments);
        assertFalse(weeklyAppointments.isEmpty());
    }

    /**
     * Testa il metodo calculateWeeklyRevenue del controller degli appuntamenti settimanali.
     */
    @Test
    void testCalculateWeeklyRevenue() {
        double revenue = Hairdresser.calculateWeeklyRevenue();
        assertTrue(revenue >= 0);
    }
}