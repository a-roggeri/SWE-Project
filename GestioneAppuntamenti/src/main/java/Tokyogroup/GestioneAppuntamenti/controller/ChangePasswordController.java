package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Controller per la gestione del cambio password degli utenti.
 */
public class ChangePasswordController {
    private static final Logger logger = LogManager.getLogger(ChangePasswordController.class);

    private final User user;
    private final UserDAO userDAO;

    /**
     * Costruttore per il controller di cambio password.
     *
     * @param user L'utente attualmente loggato.
     */
    public ChangePasswordController(User user) {
        this.user = user;
        this.userDAO = UserDAO.getInstance();
        logger.info("ChangePasswordController creato per l'utente: {}", user.getUsername());
    }

    /**
     * Cambia la password dell'utente.
     *
     * @param oldPassword La vecchia password.
     * @param newPassword La nuova password.
     * @return true se il cambio della password ha avuto successo, false altrimenti.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        logger.debug("Tentativo di cambio password per l'utente: {}", user.getUsername());
        logger.debug("Vecchia password: {}", oldPassword);
        logger.debug("Nuova password: {}", newPassword);
        try {
            boolean success = userDAO.updatePassword(user.getId(), oldPassword, newPassword);
            if (success) {
                logger.info("Password cambiata con successo per l'utente: {}", user.getUsername());
            } else {
                logger.warn("Tentativo di cambio password fallito per l'utente: {}", user.getUsername());
            }
            return success;
        } catch (SQLException e) {
            logger.error("Errore durante il cambio della password per l'utente: {}", user.getUsername(), e);
            throw new RuntimeException("Errore durante il cambio della password. Riprovare più tardi.");
        }
    }
}
