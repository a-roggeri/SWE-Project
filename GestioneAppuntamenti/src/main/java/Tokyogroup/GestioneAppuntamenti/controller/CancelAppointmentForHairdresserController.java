package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller per la gestione della cancellazione degli appuntamenti da parte
 * dei parrucchieri.
 */
public class CancelAppointmentForHairdresserController {
    private static final Logger logger = LogManager.getLogger(CancelAppointmentForHairdresserController.class);

    private final AppointmentDAO appointmentDAO;
    private final User currentHairdresser;

    /**
     * Costruttore per il controller di cancellazione degli appuntamenti per
     * parrucchieri.
     *
     * @param currentHairdresser Il parrucchiere attualmente loggato.
     */
    public CancelAppointmentForHairdresserController(User currentHairdresser) {
        this.appointmentDAO = new AppointmentDAO();
        this.currentHairdresser = currentHairdresser;
        logger.info("CancelAppointmentForHairdresserController inizializzato per il parrucchiere ID: {}",
                currentHairdresser.getId());
    }

    /**
     * Ottiene tutti gli appuntamenti validi per il parrucchiere.
     *
     * @return Lista di appuntamenti in formato leggibile.
     */
    public List<String[]> getValidAppointmentsForHairdresser() {
        try {
            List<String[]> appointments = appointmentDAO.getValidAppointmentsForHairdresser(currentHairdresser.getId());
            logger.info("Appuntamenti validi recuperati per il parrucchiere ID: {}", currentHairdresser.getId());
            return appointments;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero degli appuntamenti per il parrucchiere ID: {}",
                    currentHairdresser.getId(), e);
            throw new RuntimeException("Errore durante il recupero degli appuntamenti.", e);
        }
    }

    /**
     * Ottiene i nomi dei clienti.
     *
     * @return Mappa con ID come chiave e nome come valore.
     */
    public Map<Integer, String> getClientNames() {
        try {
            List<User> clients = appointmentDAO.getClients();
            Map<Integer, String> clientNames = new HashMap<>();
            for (User client : clients) {
                clientNames.put(client.getId(), client.getUsername());
                logger.info("Cliente ID: {}, Nome: {}", client.getId(), client.getUsername());
            }
            logger.info("Nomi dei clienti recuperati con successo.");
            return clientNames;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero dei nomi dei clienti.", e);
            throw new RuntimeException("Errore durante il recupero dei nomi dei clienti.", e);
        }
    }

    /**
     * Annulla un appuntamento impostando lo stato a "CANCELLATA".
     *
     * @param appointmentId ID dell'appuntamento da annullare.
     * @return true se l'operazione ha avuto successo, false altrimenti.
     */
    public boolean cancelAppointment(int appointmentId) {
        try {
            boolean success = appointmentDAO.updateAppointmentStatus(appointmentId, "CANCELLATA");
            if (success) {
                logger.info("Appuntamento ID {} disdetto con successo.", appointmentId);
            } else {
                logger.warn("Errore durante la disdetta dell'appuntamento ID {}.", appointmentId);
            }
            return success;
        } catch (SQLException e) {
            logger.error("Errore durante la disdetta dell'appuntamento ID: {}", appointmentId, e);
            throw new RuntimeException("Errore durante la disdetta dell'appuntamento.", e);
        }
    }
}
