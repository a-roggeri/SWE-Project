/**
 * Test suite per la classe MessageController, che gestisce
 * le operazioni relative all'invio e alla gestione dei messaggi tra gli utenti.
 */
package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento
 * dei metodi della classe MessageController.
 */
public class MessageControllerTest {

    private MessageController messageController;
    private User testUser;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Crea un utente di test e inizializza il MessageController.
     */
    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("test_user");

        messageController = new MessageController(testUser);
    }

    /**
     * Testa il metodo getAllManagers() per verificare
     * che restituisca una lista valida di gestori.
     */
    @Test
    public void testGetAllManagers() {
        try {
            List<User> managers = messageController.getAllManagers();
            assertNotNull(managers, "La lista dei gestori non dovrebbe essere null.");
            // Puoi aggiungere ulteriori verifiche, ad esempio:
            // assertEquals(expectedSize, managers.size(), "La dimensione della lista non è corretta.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo sendMessage() per verificare
     * che un messaggio venga inviato con successo.
     * 
     * receiverId ID del destinatario.
     * messageText Testo del messaggio.
     */
    @Test
    public void testSendMessage_Successful() {
        int receiverId = 1; // ID del destinatario di esempio
        String messageText = "Ciao, questo è un messaggio di prova.";

        try {
            boolean result = messageController.sendMessage(receiverId, messageText);
            assertTrue(result, "Il messaggio dovrebbe essere inviato con successo.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}