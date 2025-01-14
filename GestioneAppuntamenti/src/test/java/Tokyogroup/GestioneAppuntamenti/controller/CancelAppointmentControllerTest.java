/**
 * Test suite per la classe CancelAppointmentController, 
 * che gestisce la cancellazione degli appuntamenti.
 */
package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento
 * dei metodi della classe CancelAppointmentController.
 */
public class CancelAppointmentControllerTest {

    private CancelAppointmentController cancelAppointmentController;
    private User testUser;

    /**
     * Crea un utente di test e inizializza il controller.
     */
    @BeforeEach
    public void setUp() {
        // Creazione di un utente di test
        testUser = new User();
        testUser.setId(1000);
        testUser.setUsername("test_user");

        // Inizializzazione del controller
        cancelAppointmentController = new CancelAppointmentController(testUser);
    }

    /**
     * Testa il metodo getValidAppointmentsForClient() per verificare
     * che restituisca una lista valida di appuntamenti per il cliente.
     */
    @Test
    public void testGetValidAppointmentsForClient() {
        try {
            List<String[]> appointments = cancelAppointmentController.getValidAppointmentsForClient();
            assertNotNull(appointments, "La lista degli appuntamenti non dovrebbe essere null.");
            // Verifica basata su un valore atteso (se noto)
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo getHairdresserNames() per verificare
     * che restituisca una mappa valida di nomi dei parrucchieri.
     */
    @Test
    public void testGetHairdresserNames() {
        try {
            Map<Integer, String> hairdresserNames = cancelAppointmentController.getHairdresserNames();
            assertNotNull(hairdresserNames, "La mappa dei parrucchieri non dovrebbe essere null.");
            // Verifica basata su un valore atteso (se noto)
            // assertTrue(hairdresserNames.containsKey(expectedId), "La mappa non contiene l'ID atteso.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}