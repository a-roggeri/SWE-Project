package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.MessageDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Controller per la gestione dei messaggi.
 */
public class MessageController {

    private static final Logger logger = LogManager.getLogger(MessageController.class);

    private final MessageDAO messageDAO;
    private final User user;

    /**
     * Costruttore per inizializzare il controller dei messaggi.
     *
     * @param user L'utente corrente.
     */
    public MessageController(User user) {
        this.user = user;
        this.messageDAO = new MessageDAO();
        logger.info("MessageController inizializzato per l'utente: {}", user.getUsername());
    }

    /**
     * Recupera tutti i gestori attivi.
     *
     * @return Una lista di utenti che sono gestori.
     */
    public List<User> getAllManagers() {
        logger.info("Recupero di tutti i gestori attivi...");
        try {
            List<User> managers = messageDAO.getManagers();
            logger.info("Recuperati {} gestori.", managers.size());
            return managers;
        } catch (Exception e) {
            logger.error("Errore durante il recupero dei gestori.", e);
            throw new RuntimeException("Errore durante il caricamento dei gestori.", e);
        }
    }

    /**
     * Invia un messaggio a un gestore.
     *
     * @param receiverId  L'ID del gestore destinatario.
     * @param messageText Il testo del messaggio.
     * @return true se il messaggio è stato inviato con successo, false altrimenti.
     */
    public boolean sendMessage(int receiverId, String messageText) {
        logger.info("Invio del messaggio dall'utente ID {} al gestore ID {}.", user.getId(), receiverId);
        try {
            boolean result = messageDAO.addMessage(user.getId(), receiverId, messageText);
            if (result) {
                logger.info("Messaggio inviato con successo.");
            } else {
                logger.warn("Invio del messaggio fallito.");
            }
            return result;
        } catch (Exception e) {
            logger.error("Errore durante l'invio del messaggio.", e);
            throw new RuntimeException("Errore durante l'invio del messaggio.", e);
        }
    }
}