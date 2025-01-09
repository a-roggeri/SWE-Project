package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller per la creazione di appuntamenti.
 */
public class CreateAppointmentController {

    private static final Logger logger = LogManager.getLogger(CreateAppointmentController.class);

    private final AppointmentDAO appointmentDAO;
    private final ServiceDAO serviceDAO;
    private final User currentHairdresser;

    /**
     * Costruttore della classe CreateAppointmentController.
     *
     * @param currentHairdresser L'attuale parrucchiere.
     */
    public CreateAppointmentController(User currentHairdresser) {
        this.appointmentDAO = AppointmentDAO.getInstance();
        this.serviceDAO = new ServiceDAO();
        this.currentHairdresser = currentHairdresser;

        logger.info("CreateAppointmentController inizializzato per parrucchiere: {}", currentHairdresser.getUsername());
    }

    /**
     * Ottiene la lista di tutti i clienti disponibili.
     *
     * @return Lista di oggetti User con accountType "CLIENTE".
     */
    public List<User> getClients() {
        try {
            List<User> clients = appointmentDAO.getClients();
            logger.info("Recuperati {} clienti.", clients.size());
            return clients;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero dei clienti.", e);
            throw new RuntimeException("Errore durante il recupero dei clienti.", e);
        }
    }

    /**
     * Ottiene i servizi offerti dall'attuale parrucchiere.
     *
     * @return Lista di servizi offerti.
     */
    public List<Service> getServicesForCurrentHairdresser() {
        try {
            List<Service> services = serviceDAO.getServicesByHairdresser(currentHairdresser.getId());
            logger.info("Recuperati {} servizi per parrucchiere: {}", services.size(),
                    currentHairdresser.getUsername());
            return services;
        } catch (RuntimeException e) {
            logger.error("Errore durante il recupero dei servizi per il parrucchiere con ID: {}",
                    currentHairdresser.getId(), e);
            throw new RuntimeException("Errore durante il recupero dei servizi.", e);
        }
    }

    /**
     * Ottiene le ore disponibili per una data specifica.
     *
     * @param selectedDate Data selezionata in formato "yyyy-MM-dd".
     * @return Lista di orari disponibili.
     */
    public List<String> getAvailableHoursForDate(String selectedDate) {
        try {
            List<String> bookedHours = appointmentDAO.getBookedHours(currentHairdresser.getId(), selectedDate);

            List<String> allHours = new ArrayList<>();
            for (int hour = 9; hour < 18; hour++) {
                String formattedHour = String.format("%02d:00", hour);
                if (!bookedHours.contains(formattedHour)) {
                    allHours.add(formattedHour);
                }
            }
            logger.info("Recuperate {} ore disponibili per parrucchiere ID: {} nella data: {}", allHours.size(),
                    currentHairdresser.getId(), selectedDate);
            return allHours;
        } catch (SQLException e) {
            logger.error("Errore durante il recupero delle ore disponibili per il parrucchiere con ID: {}.",
                    currentHairdresser.getId(), e);
            throw new RuntimeException("Errore durante il recupero delle ore disponibili.", e);
        }
    }

    /**
     * Crea un nuovo appuntamento per il cliente selezionato.
     *
     * @param clientId         ID del cliente.
     * @param date             Data dell'appuntamento (formato "yyyy-MM-dd").
     * @param hour             Ora dell'appuntamento (formato "HH:mm").
     * @param selectedServices Lista di servizi selezionati.
     * @return true se l'appuntamento è stato creato con successo, false altrimenti.
     */
    public boolean createAppointment(int clientId, String date, String hour, List<String> selectedServices) {
        try {
            if (clientId <= 0) {
                logger.warn("Tentativo di creazione di un appuntamento con client ID non valido: {}", clientId);
                throw new IllegalArgumentException("ID cliente non valido.");
            }

            if (!isDateTimeValid(date, hour)) {
                logger.warn("Tentativo di creazione di un appuntamento con data/ora non valida: {} {}", date, hour);
                throw new IllegalArgumentException("La data e l'ora selezionate sono nel passato.");
            }

            String dateTime = date + " " + hour + ":00";

            List<Integer> serviceIds = new ArrayList<>();
            for (String serviceName : selectedServices) {
                int serviceId = serviceDAO.getServiceIdByName(serviceName);
                serviceIds.add(serviceId);
            }

            boolean success = appointmentDAO.bookAppointment(clientId, currentHairdresser.getId(), dateTime,
                    serviceIds);
            if (success) {
                logger.info("Appuntamento creato con successo per cliente ID: {} e parrucchiere ID: {}", clientId,
                        currentHairdresser.getId());
            } else {
                logger.warn("Creazione appuntamento fallita per cliente ID: {} e parrucchiere ID: {}", clientId,
                        currentHairdresser.getId());
            }
            return success;
        } catch (SQLException e) {
            logger.error("Errore SQL durante la creazione dell'appuntamento per il parrucchiere con ID: {}.",
                    currentHairdresser.getId(), e);
            throw new RuntimeException("Errore durante la creazione dell'appuntamento.", e);
        }
    }

    /**
     * Verifica che la data e l'ora selezionate siano valide (non nel passato).
     *
     * @param date Data selezionata (formato "yyyy-MM-dd").
     * @param hour Ora selezionata (formato "HH:mm").
     * @return true se la data e l'ora sono valide, false altrimenti.
     */
    private boolean isDateTimeValid(String date, String hour) {
        try {
            String dateTimeString = date + " " + hour;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date selectedDateTime = formatter.parse(dateTimeString);

            Date now = new Date();
            return !selectedDateTime.before(now);
        } catch (Exception e) {
            logger.error("Errore durante il parsing della data e ora: {} {}", date, hour, e);
            throw new IllegalArgumentException("Data o ora non valida.");
        }
    }
}
