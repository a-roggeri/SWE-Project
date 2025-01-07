package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Controller per la registrazione degli utenti.
 */
public class RegisterController {
    private static final Logger logger = LogManager.getLogger(RegisterController.class);
    private final UserDAO userDAO;

    /**
     * Inizializza il controller di registrazione con un'istanza di UserDAO.
     */
    public RegisterController() {
        this.userDAO = UserDAO.getInstance();
        logger.info("RegisterController inizializzato con UserDAO.");
    }

    /**
     * Registra un nuovo utente con i dettagli forniti.
     *
     * @param username    il nome utente dell'utente
     * @param password    la password dell'utente
     * @param accountType il tipo di account (CLIENTE o GESTORE)
     * @return true se la registrazione ha successo, false altrimenti
     * @throws IllegalArgumentException se uno dei campi è vuoto
     * @throws RuntimeException         se si verifica un errore durante la
     *                                  registrazione nel database
     */
    public boolean registerUser(String username, String password, String accountType) {
        logger.info("Tentativo di registrazione per username: {}", username);

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            logger.warn("Registrazione fallita: campi vuoti.");
            throw new IllegalArgumentException("Tutti i campi sono obbligatori.");
        }

        try {
            User user = new User(username, password, accountType);
            logger.debug("Utente creato: {}", user);
            boolean result = userDAO.addUser(user);

            if (result) {
                logger.info("Utente registrato con successo: {}", user);
            } else {
                logger.warn("Registrazione utente fallita: {}", user);
            }
            return result;
        } catch (SQLException e) {
            logger.error("Errore durante la registrazione dell'utente nel database: {}", username, e);
            throw new RuntimeException("Errore durante la registrazione.", e);
        }
    }
}