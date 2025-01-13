package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento del controller
 * ViewMessagesController.
 */
public class ViewMessagesControllerTest {

    private ViewMessagesController viewMessagesController;
    private int managerId;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Inizializza un manager ID e un'istanza del controller ViewMessagesController.
     */
    @BeforeEach
    public void setUp() {
        // Inizializzazione del manager ID
        managerId = 1;
        // Inizializzazione del controller
        viewMessagesController = new ViewMessagesController(managerId);
    }

    /**
     * Test per verificare il metodo getUnreadMessages().
     * Controlla che il metodo restituisca una lista valida di messaggi non letti 
     * e che la lista non sia null.
     */
    @Test
    public void testGetUnreadMessages() {
        try {
            List<Message> messages = viewMessagesController.getUnreadMessages();
            assertNotNull(messages, "La lista dei messaggi non dovrebbe essere null.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Test per verificare il metodo markMessageAsRead() quando viene fornito un ID valido.
     * Controlla che il metodo restituisca true indicando che il messaggio è stato
     * correttamente segnato come letto.
     */
    @Test
    public void testMarkMessageAsRead_Successful() {
        // Test del metodo markMessageAsRead() con un ID valido
        int messageId = 20; // ID di esempio
        try {
            boolean result = viewMessagesController.markMessageAsRead(messageId);
            assertTrue(result, "Il messaggio dovrebbe essere segnato come letto con successo.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Test per verificare il metodo markMessageAsRead() quando viene fornito un ID non valido.
     * Controlla che il metodo restituisca false indicando che il messaggio non può essere
     * segnato come letto.
     */
    @Test
    public void testMarkMessageAsRead_Failure() {
        // Test del metodo markMessageAsRead() con un ID non valido
        int messageId = -1; // ID non valido
        try {
            boolean result = viewMessagesController.markMessageAsRead(messageId);
            assertFalse(result, "Il messaggio non dovrebbe essere segnato come letto con un ID non valido.");
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}