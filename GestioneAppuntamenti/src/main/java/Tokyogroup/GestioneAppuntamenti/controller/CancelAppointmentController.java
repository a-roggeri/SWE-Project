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
 * Controller per la gestione della cancellazione degli appuntamenti.
 */
public class CancelAppointmentController {
    private static final Logger logger = LogManager.getLogger(CancelAppointmentController.class);

    private final AppointmentDAO appointmentDAO;
    private final User currentUser;

    /**
     * Costruttore per il controller di cancellazione degli appuntamenti.
     *
     * @param currentUser L'utente attualmente loggato.
     */
    public CancelAppointmentController(User currentUser) {
        this.appointmentDAO = AppointmentDAO.getInstance();
        this.currentUser = currentUser;

        logger.info("CancelAppointmentController inizializzato per l'utente ID: {}", currentUser.getId());
    }

    /**
     * Ottiene tutte le prenotazioni valide per il cliente.
     *
     * @return Lista di appuntamenti in formato leggibile.
     */
    public List<String[]> getValidAppointmentsForClient() {
        try {
            List<String[]> appointments = appointmentDAO.getValidAppointmentsForClient(currentUser.getId());
            logger.info("Recuperate {} prenotazioni valide per l'utente ID: {}", appointments.size(),
                    currentUser.getId());
            return appointments;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero delle prenotazioni per l'utente ID: {}", currentUser.getId(), e);
            throw new RuntimeException("Errore durante il recupero delle prenotazioni.", e);
        }
    }

    /**
     * Ottiene i nomi dei parrucchieri disponibili.
     *
     * @return Mappa con ID come chiave e nome come valore.
     */
    public Map<Integer, String> getHairdresserNames() {
        try {
            List<User> hairdressers = appointmentDAO.getAvailableHairdressers();
            Map<Integer, String> hairdresserNames = new HashMap<>();
            for (User hairdresser : hairdressers) {
                hairdresserNames.put(hairdresser.getId(), hairdresser.getUsername());
            }
            logger.info("Recuperati {} parrucchieri disponibili.", hairdresserNames.size());
            return hairdresserNames;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero dei nomi dei parrucchieri.", e);
            throw new RuntimeException("Errore durante il recupero dei nomi dei parrucchieri.", e);
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
