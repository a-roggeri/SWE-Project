/**
 * Test suite per la classe AppointmentController, che gestisce
 * le operazioni relative agli appuntamenti per un barbiere.
 */
package Tokyogroup.GestioneAppuntamenti.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;

/**
 * Classe di test per verificare il corretto funzionamento
 * dei metodi della classe AppointmentController.
 */
public class AppointmentControllerTest {

    private AppointmentController controller;
    private User currentUser;

    /**
     * Classe stub per simulare le operazioni del DAO per gli appuntamenti.
     */
    private class StubAppointmentDAO extends AppointmentDAO {
        /**
         * Restituisce una lista simulata di parrucchieri disponibili.
         * 
         * @return Lista di oggetti User rappresentanti parrucchieri.
         */
        @Override
        public List<User> getAvailableHairdressers() {
            return Arrays.asList(
                new User(100, "Hairdresser1", "aa", "CLIENTE", true),
                new User(200, "Hairdresser2", "bb", "GESTORE", true)
            );
        }

        /**
         * Restituisce una lista simulata di ore già prenotate per un parrucchiere
         * in una determinata data.
         * 
         * @param hairdresserId ID del parrucchiere.
         * @param selectedDate Data selezionata.
         * @return Lista di ore prenotate.
         */
        @Override
        public List<String> getBookedHours(int hairdresserId, String selectedDate) {
            return Arrays.asList("10:00", "11:00");
        }

        /**
         * Simula la prenotazione di un appuntamento.
         * 
         * @param clientId ID del cliente.
         * @param hairdresserId ID del parrucchiere.
         * @param dateTime Data e ora dell'appuntamento.
         * @param serviceIds ID dei servizi selezionati.
         * @return true se la prenotazione è avvenuta con successo.
         */
        @Override
        public boolean bookAppointment(int clientId, int hairdresserId, String dateTime, List<Integer> serviceIds) {
            return true; // Simula sempre un successo
        }
    }

    /**
     * Classe stub per simulare le operazioni del DAO per i servizi.
     */
    private class StubServiceDAO extends ServiceDAO {
        /**
         * Restituisce una lista simulata di servizi offerti da un parrucchiere.
         * 
         * @param hairdresserId ID del parrucchiere.
         * @return Lista di servizi offerti.
         */
        @Override
        public List<Service> getServicesByHairdresser(int hairdresserId) {
            return Arrays.asList(
                new Service(1, "Haircut", 20.0),
                new Service(2, "Shave", 15.0)
            );
        }

        /**
         * Restituisce l'ID del servizio corrispondente al nome specificato.
         * 
         * @param serviceName Nome del servizio.
         * @return ID del servizio, o -1 se non trovato.
         */
        @Override
        public int getServiceIdByName(String serviceName) {
            if (serviceName.equals("Haircut")) return 1;
            if (serviceName.equals("Shave")) return 2;
            return -1;
        }
    }

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     */
    @BeforeEach
    void setUp() {
        currentUser = new User(99, "TestUser", "ciao", "CLIENTE", true);
        controller = new AppointmentController(currentUser);
        controller.appointmentDAO = new StubAppointmentDAO(); // Sostituisce con lo stub
        controller.serviceDAO = new StubServiceDAO(); // Sostituisce con lo stub
    }

    /**
     * Testa il metodo getCurrentUser per verificare
     * che restituisca l'utente attuale.
     */
    @Test
    void testGetCurrentUser() {
        User user = controller.getCurrentUser();
        assertNotNull(user);
        assertEquals(99, user.getId());
        assertEquals("TestUser", user.getUsername());
    }

    /**
     * Testa il metodo getAvailableHairdressers per verificare
     * che restituisca una lista di parrucchieri disponibili.
     */
    @Test
    void testGetAvailableHairdressers() {
        List<User> hairdressers = controller.getAvailableHairdressers();
        assertNotNull(hairdressers);
        assertEquals(2, hairdressers.size());
        assertEquals("Hairdresser1", hairdressers.get(0).getUsername());
    }

    /**
     * Testa il metodo getServicesForHairdresser per verificare
     * che restituisca i servizi di un parrucchiere.
     */
    @Test
    void testGetServicesForHairdresser() {
        User hairdresser = new User(400, "Hairdresser1", "A", "GESTORE", true);
        List<Service> services = controller.getServicesForHairdresser(hairdresser);
        assertNotNull(services);
        assertEquals(2, services.size());
        assertEquals("Haircut", services.get(0).getName());
    }

    /**
     * Testa il metodo getAvailableHours per verificare
     * che restituisca le ore disponibili per un parrucchiere.
     */
    @Test
    void testGetAvailableHours() {
        List<String> availableHours = controller.getAvailableHours(1, "2024-01-01");
        assertNotNull(availableHours);
        assertTrue(availableHours.contains("09:00"));
        assertFalse(availableHours.contains("10:00")); // "10:00" è già prenotato
    }

    /**
     * Testa il metodo bookAppointment per verificare
     * che un appuntamento venga prenotato con successo.
     */
    @Test
    void testBookAppointmentSuccess() {
        List<String> selectedServices = Arrays.asList("Haircut", "Shave");
        boolean success = controller.bookAppointment(9000, "2026-01-01", "12:00", selectedServices);
        assertTrue(success);
    }

    /**
     * Testa il metodo bookAppointment per gestire una data/ora non valida.
     */
    @Test
    void testBookAppointmentInvalidDateTime() {
        List<String> selectedServices = Arrays.asList("Haircut");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.bookAppointment(1, "2020-01-01", "12:00", selectedServices); // Data nel passato
        });
        assertEquals("La data e l'ora selezionate sono nel passato.", exception.getMessage());
    }

    /**
     * Testa il metodo isDateTimeValid per verificare
     * la validità di una data e ora futura.
     */
    @Test
    void testIsDateTimeValid() {
        assertTrue(controller.isDateTimeValid("2026-01-01", "12:00")); // Data futura
        assertFalse(controller.isDateTimeValid("2020-01-01", "12:00")); // Data passata
    }

    /**
     * Testa il metodo isDateTimeValid per gestire
     * formati di data/ora non validi.
     */
    @Test
    void testIsDateTimeValidInvalidFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.isDateTimeValid("invalid-date", "invalid-hour");
        });
        assertEquals("Data o ora non valida.", exception.getMessage());
    }
}