package Tokyogroup.GestioneAppuntamenti.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Classe di test per verificare il corretto funzionamento del controller
 * RegisterController, responsabile della registrazione di nuovi utenti.
 */
public class RegisterControllerTest {

    private RegisterController registerController;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Inizializza un'istanza del RegisterController.
     */
    @BeforeEach
    public void setUp() {
        // Inizializzazione del controller
        registerController = new RegisterController();
    }

    /**
     * Test per verificare la registrazione di un utente con credenziali valide.
     * Controlla che il metodo registerUser() restituisca true quando tutti i campi
     * richiesti sono forniti correttamente.
     */
    @Test
    public void testRegisterUser_Successful() {
        // Parametri di esempio
    	// cambiare credenziali ogni test 
    	byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String username = new String(array, Charset.forName("UTF-8"));
        new Random().nextBytes(array);
        String password = new String(array, Charset.forName("UTF-8"));
        String accountType = "CLIENTE";
        

        try {
            boolean result = registerController.registerUser(username, password, accountType);
            assertTrue(result, "L'utente dovrebbe essere registrato con successo.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
    
    /**
     * Test per verificare il comportamento del metodo registerUser() quando
     * vengono forniti campi vuoti. Si aspetta che venga lanciata un'eccezione
     * IllegalArgumentException.
     */
    @Test
    public void testRegisterUser_EmptyFields() {
        // Parametri con campi vuoti
        String username = "";
        String password = "";
        String accountType = "CLIENTE";

        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser(username, password, accountType);
        }, "Dovrebbe essere lanciata un'eccezione per campi vuoti.");
    }

    /**
     * Test per verificare il comportamento del metodo registerUser() quando
     * vengono forniti campi null. Si aspetta che venga lanciata un'eccezione
     * IllegalArgumentException.
     */
    @Test
    public void testRegisterUser_NullFields() {
        // Parametri null
        String username = null;
        String password = null;
        String accountType = "CLIENTE";

        assertThrows(IllegalArgumentException.class, () -> {
            registerController.registerUser(username, password, accountType);
        }, "Dovrebbe essere lanciata un'eccezione per campi null.");
    }





}