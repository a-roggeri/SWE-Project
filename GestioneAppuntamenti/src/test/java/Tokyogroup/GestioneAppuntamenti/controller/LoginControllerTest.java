/**
 * Test suite per la classe LoginController, che gestisce
 * le operazioni relative all'autenticazione e gestione degli account utente.
 */
package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per verificare il corretto funzionamento
 * dei metodi della classe LoginController.
 */
public class LoginControllerTest {

    private LoginController loginController;

    /**
     * Metodo eseguito prima di ogni test per configurare l'ambiente di test.
     * Inizializza un'istanza di LoginController.
     */
    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
    }

    /**
     * Testa il metodo authenticate() per verificare
     * che l'autenticazione avvenga correttamente con credenziali valide.
     */
    @Test
    public void testAuthenticate_ValidCredentials() {
        String username = "a"; // Nome utente di esempio
        String password = "a"; // Password di esempio

        try {
            User user = loginController.authenticate(username, password);
            assertNotNull(user, "L'utente autenticato non dovrebbe essere null.");
            // Aggiungi ulteriori verifiche basate sui dati attesi
            // assertEquals(expectedUsername, user.getUsername(), "Il nome utente non corrisponde.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo restoreAccount() per verificare
     * che un account utente venga ripristinato con successo.
     */
    @Test
    public void testRestoreAccount_Successful() {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser"); // Utente di test

        try {
            boolean result = loginController.restoreAccount(user);
            assertTrue(result, "L'account dovrebbe essere ripristinato con successo.");
        } catch (RuntimeException e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo handleSuccessfulLogin() per verificare
     * che un login valido venga gestito correttamente.
     */
    @Test
    public void testHandleSuccessfulLogin() {
        User user = new User();
        user.setId(1);
        user.setUsername("Ere"); // Nome utente di test
        user.setAccountType("GESTORE"); // Tipo account di test

        try {
            loginController.handleSuccessfulLogin(user);
            // Nessuna eccezione dovrebbe essere generata
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }

    /**
     * Testa il metodo handleFailedLogin() per verificare
     * che un login fallito venga gestito correttamente lanciando un'eccezione.
     */
    @Test
    public void testHandleFailedLogin() {
        String username = "testUser"; // Nome utente di esempio

        assertThrows(RuntimeException.class, () -> {
            loginController.handleFailedLogin(username);
        }, "Dovrebbe essere lanciata un'eccezione per login fallito.");
    }

    /**
     * Testa il metodo openRegistration() per verificare
     * che l'operazione di apertura della registrazione non generi eccezioni.
     */
    @Test
    public void testOpenRegistration() {
        try {
            loginController.openRegistration();
            // Nessuna eccezione dovrebbe essere generata
        } catch (Exception e) {
            fail("Non dovrebbe essere generata un'eccezione: " + e.getMessage());
        }
    }
}