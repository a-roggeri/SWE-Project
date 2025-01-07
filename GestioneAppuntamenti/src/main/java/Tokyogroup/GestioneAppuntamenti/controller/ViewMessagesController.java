package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.Message;
import Tokyogroup.GestioneAppuntamenti.model.MessageDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Controller per la visualizzazione dei messaggi.
 */
public class ViewMessagesController {

    private static final Logger logger = LogManager.getLogger(ViewMessagesController.class);

    private final MessageDAO messageDAO;
    private final int managerId;

    /**
     * Costruttore della classe ViewMessagesController.
     *
     * @param managerId l'ID del manager.
     */
    public ViewMessagesController(int managerId) {
        this.messageDAO = new MessageDAO();
        this.managerId = managerId;
        logger.info("Inizializzato ViewMessagesController con manager ID: {}", managerId);
    }

    /**
     * Recupera i messaggi non letti per il manager.
     *
     * @return una lista di messaggi non letti.
     */
    public List<Message> getUnreadMessages() {
        logger.info("Recupero dei messaggi non letti per il manager ID: {}", managerId);
        List<Message> messages = messageDAO.getUnreadMessages(managerId);
        logger.info("Recuperati {} messaggi non letti.", messages.size());
        return messages;
    }

    /**
     * Segna un messaggio come letto.
     *
     * @param messageId l'ID del messaggio da segnare come letto.
     * @return true se l'operazione ha avuto successo, false altrimenti.
     */
    public boolean markMessageAsRead(int messageId) {
        logger.info("Segnatura del messaggio ID {} come letto.", messageId);
        boolean success = messageDAO.updateMessageStatus(messageId, "LETTO");
        if (success) {
            logger.info("Messaggio ID {} segnato come letto con successo.", messageId);
        } else {
            logger.error("Fallimento nella segnatura del messaggio ID {} come letto.", messageId);
        }
        return success;
    }
}