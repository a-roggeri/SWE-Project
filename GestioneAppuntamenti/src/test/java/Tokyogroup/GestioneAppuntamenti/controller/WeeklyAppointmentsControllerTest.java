package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento del controller 
 * WeeklyAppointmentsController.
 */
public class WeeklyAppointmentsControllerTest {

    private WeeklyAppointmentsController weeklyAppointmentsController;
    private User testHairdresser;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Inizializza un oggetto User come parrucchiere di test e un'istanza del controller.
     */
    @BeforeEach
    public void setUp() {
        // creo un parrucchiere di test
        testHairdresser = new User();
        testHairdresser.setId(1);
        testHairdresser.setUsername("test_hairdresser");

        // inizializzo controller
        weeklyAppointmentsController = new WeeklyAppointmentsController(testHairdresser);
    }
    
    /**
     * Test per verificare il metodo getWeeklyAppointments().
     * Controlla che la mappa degli appuntamenti settimanali venga restituita 
     * correttamente e che non sia null.
     */
    @Test
    public void testGetWeeklyAppointments() {
        try {
            Map<Integer, List<String[]>> weeklyAppointments = weeklyAppointmentsController.getWeeklyAppointments();
            assertNotNull(weeklyAppointments, "La mappa degli appuntamenti settimanali non dovrebbe essere null.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
    
    /**
     * Test per verificare il metodo calculateWeeklyRevenue().
     * Controlla che il calcolo del fatturato settimanale restituisca un valore 
     * maggiore o uguale a zero e che non vengano generate eccezioni.
     */
    @Test
    public void testCalculateWeeklyRevenue() {
        try {
            double revenue = weeklyAppointmentsController.calculateWeeklyRevenue();
            assertTrue(revenue >= 0, "Il fatturato settimanale dovrebbe essere maggiore o uguale a zero.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}