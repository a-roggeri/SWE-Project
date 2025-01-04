package Tokyogroup.GestioneAppuntamenti.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import Tokyogroup.GestioneAppuntamenti.view.ClienteView;
import Tokyogroup.GestioneAppuntamenti.view.GestoreView;
import Tokyogroup.GestioneAppuntamenti.view.RegisterView;

/**
 * Controller per la gestione del login.
 */
public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    private final UserDAO userDAO;

    /**
     * Costruttore che inizializza il controller con un'istanza di UserDAO.
     */
    public LoginController() {
        this.userDAO = UserDAO.getInstance();
        logger.info("LoginController inizializzato con UserDAO.");
    }

    /**
     * Autentica un utente con username e password.
     *
     * @param username il nome utente
     * @param password la password
     * @return l'utente autenticato, o null se le credenziali sono errate
     * @throws IllegalArgumentException se username o password sono vuoti
     * @throws RuntimeException         se si verifica un errore durante
     *                                  l'autenticazione
     */
    public User authenticate(String username, String password) {
        logger.debug("Tentativo di autenticazione per username: {}", username);

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            logger.warn("Autenticazione fallita: username o password vuoti.");
            throw new IllegalArgumentException("Username o password non possono essere vuoti.");
        }

        try {
            User user = userDAO.findUser(username, password);
            if (user != null) {
                logger.info("Autenticazione riuscita per l'utente: {}", username);
                return user;
            } else {
                logger.warn("Autenticazione fallita: credenziali non valide per l'utente: {}", username);
                return null;
            }
        } catch (Exception e) {
            logger.error("Errore durante l'autenticazione per l'utente: {}", username, e);
            throw new RuntimeException("Si è verificato un errore durante l'autenticazione. Riprova.", e);
        }
    }

    /**
     * Ripristina l'account di un utente.
     *
     * @param user l'utente di cui ripristinare l'account
     * @return true se il ripristino è riuscito, false altrimenti
     * @throws RuntimeException se si verifica un errore durante il ripristino
     */
    public boolean restoreAccount(User user) {
        logger.debug("Tentativo di ripristino account per l'utente: {}", user.getUsername());

        try {
            UserDAO userDAO = UserDAO.getInstance();
            boolean success = userDAO.restoreUserAccount(user.getId());
            if (success) {
                logger.info("Account ripristinato con successo per l'utente: {}", user.getUsername());
            } else {
                logger.warn("Errore durante il ripristino dell'account per l'utente: {}", user.getUsername());
            }
            return success;
        } catch (SQLException e) {
            logger.error("Errore durante il ripristino dell'account per l'utente: {}", user.getUsername(), e);
            throw new RuntimeException("Errore durante il ripristino dell'account. Riprova più tardi.", e);
        }
    }

    /**
     * Gestisce il login riuscito di un utente.
     *
     * @param user l'utente autenticato
     */
    public void handleSuccessfulLogin(User user) {
        logger.info("Gestione del login riuscito per l'utente: {}", user.getUsername());

        if ("GESTORE".equals(user.getAccountType())) {
            logger.info("L'utente è un GESTORE. Avvio di GestoreView...");
            new GestoreView(user).show();
        } else {
            logger.info("L'utente è un CLIENTE. Avvio di ClienteView...");
            new ClienteView(user).show();
        }
    }

    /**
     * Gestisce il login fallito di un utente.
     *
     * @param username il nome utente
     * @throws RuntimeException sempre, per indicare il fallimento del login
     */
    public void handleFailedLogin(String username) {
        logger.warn("Login fallito per l'utente: {}", username);
        throw new RuntimeException("Credenziali non valide. Riprova.");
    }

    /**
     * Apre la vista di registrazione.
     *
     * @throws RuntimeException se si verifica un errore durante l'apertura della
     *                          vista
     */
    public void openRegistration() {
        logger.info("Apertura della vista di registrazione...");
        try {
            new RegisterView().show();
        } catch (Exception e) {
            logger.error("Errore durante l'apertura della vista di registrazione.", e);
            throw new RuntimeException("Impossibile aprire la vista di registrazione. Riprova.", e);
        }
    }
}
