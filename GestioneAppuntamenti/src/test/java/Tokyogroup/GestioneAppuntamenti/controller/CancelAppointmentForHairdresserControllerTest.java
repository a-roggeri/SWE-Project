/**
 * Test suite per la classe CancelAppointmentForHairdresserController,
 * che gestisce la cancellazione degli appuntamenti da parte di un parrucchiere.
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
 * dei metodi della classe CancelAppointmentForHairdresserController.
 */
public class CancelAppointmentForHairdresserControllerTest {

    private CancelAppointmentForHairdresserController cancelAppointmentForHairdresserController;
    private User testHairdresser;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Crea un parrucchiere di test e inizializza il controller.
     */
    @BeforeEach
    public void setUp() {
        // Creazione di un parrucchiere di test
        testHairdresser = new User();
        testHairdresser.setId(2);
        testHairdresser.setUsername("test_hairdresser");

        // Inizializzazione del controller
        cancelAppointmentForHairdresserController = new CancelAppointmentForHairdresserController(testHairdresser);
    }

    /**
     * Testa il metodo getValidAppointmentsForHairdresser() per verificare
     * che restituisca una lista valida di appuntamenti per il parrucchiere.
     */
    @Test
    public void testGetValidAppointmentsForHairdresser() {
        try {
            List<String[]> appointments = cancelAppointmentForHairdresserController.getValidAppointmentsForHairdresser();
            assertNotNull(appointments, "La lista degli appuntamenti non dovrebbe essere null.");
            // Verifica basata su un valore atteso (se noto)
            // assertEquals(expectedSize, appointments.size(), "La dimensione della lista non è corretta.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo getClientNames() per verificare
     * che restituisca una mappa valida di nomi dei clienti.
     */
    @Test
    public void testGetClientNames() {
        try {
            Map<Integer, String> clientNames = cancelAppointmentForHairdresserController.getClientNames();
            assertNotNull(clientNames, "La mappa dei clienti non dovrebbe essere null.");
            // Verifica basata su un valore atteso (se noto)
            // assertTrue(clientNames.containsKey(expectedId), "La mappa non contiene l'ID atteso.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}