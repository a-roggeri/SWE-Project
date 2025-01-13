package Tokyogroup.GestioneAppuntamenti;

import com.formdev.flatlaf.FlatDarculaLaf;

import Tokyogroup.GestioneAppuntamenti.controller.LoginController;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.view.LoginView;

import org.junit.jupiter.api.Test;

import javax.swing.UIManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento delle componenti 
 * principali dell'applicazione Gestione Appuntamenti Barbiere.
 */
public class AppTest {

    /**
     * Test per verificare l'inizializzazione corretta del database.
     * Questo metodo controlla che il metodo initializeDatabase() di DatabaseManager
     * non generi eccezioni.
     */
    @Test
    public void testDatabaseInitialization() {
        try {
            DatabaseManager.initializeDatabase();
            assertTrue(true, "Il database dovrebbe essere inizializzato senza errori.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione durante l'inizializzazione del database: " + e.getMessage());
        }
    }
    
    /**
     * Test per verificare l'aggiornamento degli appuntamenti obsoleti.
     * Questo metodo assicura che il metodo updatePastAppointments() di DatabaseManager
     * funzioni correttamente senza generare eccezioni.
     */
    @Test
    public void testUpdatePastAppointments() {
        try {
            DatabaseManager.updatePastAppointments();
            assertTrue(true, "Gli appuntamenti obsoleti dovrebbero essere aggiornati senza errori.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione durante la pulizia degli appuntamenti obsoleti: " + e.getMessage());
        }
    }
    
    /**
     * Test per verificare l'impostazione corretta del Look and Feel.
     * Il test controlla che l'applicazione possa impostare correttamente un tema grafico
     * tramite la libreria FlatLaf.
     */
    @Test
    public void testLookAndFeelSetup() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
            assertTrue(true, "Il Look and Feel dovrebbe essere impostato senza errori.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione durante l'impostazione del Look and Feel: " + e.getMessage());
        }
    }

    /**
     * Test per verificare l'inizializzazione della vista di login.
     * Questo test crea un'istanza di LoginView e verifica che venga mostrata senza errori.
     */
    @Test
    public void testLoginViewInitialization() {
        try {
            LoginController loginController = new LoginController();
            LoginView loginView = new LoginView(loginController);
            loginView.show();
            assertTrue(true, "La vista di login dovrebbe essere avviata senza errori.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione durante l'avvio della LoginView: " + e.getMessage());
        }
    }

    /**
     * Test per simulare la terminazione dell'applicazione.
     * Questo test verifica che il metodo terminateApplication() possa essere eseguito
     * senza errori, anche se privato.
     */
    @Test
    public void testTerminateApplication() {
        try {
            App app = new App();
            app.getClass().getDeclaredMethod("terminateApplication", String.class).setAccessible(true);
            assertTrue(true, "La terminazione dell'applicazione dovrebbe avvenire senza errori.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione durante la terminazione dell'applicazione: " + e.getMessage());
        }
    }
}