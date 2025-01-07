
package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller per la gestione degli appuntamenti.
 */
public class AppointmentController {
    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    AppointmentDAO appointmentDAO;
    ServiceDAO serviceDAO;
    private final User currentUser;

    /**
     * Costruttore del controller degli appuntamenti.
     *
     * @param currentUser l'utente corrente
     */
    public AppointmentController(User currentUser) {
        this.appointmentDAO = AppointmentDAO.getInstance();
        this.serviceDAO = new ServiceDAO();
        this.currentUser = currentUser;

        logger.info("AppointmentController inizializzato per l'utente: {}", currentUser.getUsername());
    }

    /**
     * Restituisce l'utente corrente.
     *
     * @return l'utente corrente
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Restituisce la lista dei parrucchieri disponibili.
     *
     * @return lista dei parrucchieri disponibili
     */
    public List<User> getAvailableHairdressers() {
        try {
            List<User> hairdressers = appointmentDAO.getAvailableHairdressers();
            logger.info("Recuperati {} parrucchieri disponibili.", hairdressers.size());
            return hairdressers;
        } catch (SQLException e) {
            logger.error("Errore nel recupero dei parrucchieri disponibili.", e);
            throw new RuntimeException("Errore nel recupero dei parrucchieri.", e);
        }
    }

    /**
     * Restituisce la lista dei servizi offerti da un parrucchiere.
     *
     * @param hairdresser il parrucchiere
     * @return lista dei servizi offerti
     */
    public List<Service> getServicesForHairdresser(User hairdresser) {
        try {
            List<Service> services = serviceDAO.getServicesByHairdresser(hairdresser.getId());
            logger.info("Recuperati {} servizi per il parrucchiere: {}", services.size(), hairdresser.getUsername());
            return services;
        } catch (RuntimeException e) {
            logger.error("Errore nel recupero dei servizi per il parrucchiere con ID: {}", hairdresser.getId(), e);
            throw new RuntimeException("Errore nel recupero dei servizi.", e);
        }
    }

    /**
     * Restituisce la lista delle ore disponibili per un parrucchiere in una data
     * specifica.
     *
     * @param hairdresserId l'ID del parrucchiere
     * @param selectedDate  la data selezionata
     * @return lista delle ore disponibili
     */
    public List<String> getAvailableHours(int hairdresserId, String selectedDate) {
        try {
            List<String> bookedHours = appointmentDAO.getBookedHours(hairdresserId, selectedDate);

            List<String> allHours = new ArrayList<>();
            for (int hour = 9; hour < 18; hour++) {
                String formattedHour = String.format("%02d:00", hour);
                if (!bookedHours.contains(formattedHour)) {
                    allHours.add(formattedHour);
                }
            }
            logger.info("Recuperate {} ore disponibili per il parrucchiere ID: {} nella data: {}", allHours.size(),
                    hairdresserId, selectedDate);
            return allHours;
        } catch (SQLException e) {
            logger.error("Errore nel recupero delle ore disponibili per il parrucchiere con ID: {}.", hairdresserId, e);
            throw new RuntimeException("Errore nel recupero delle ore disponibili.", e);
        }
    }

    /**
     * Prenota un appuntamento per un parrucchiere in una data e ora specifica con i
     * servizi selezionati.
     *
     * @param hairdresserId    l'ID del parrucchiere
     * @param date             la data dell'appuntamento
     * @param hour             l'ora dell'appuntamento
     * @param selectedServices lista dei servizi selezionati
     * @return true se la prenotazione è avvenuta con successo, false altrimenti
     */
    public boolean bookAppointment(int hairdresserId, String date, String hour, List<String> selectedServices) {
        try {
            int clientId = getCurrentUser().getId();
            if (clientId <= 0) {
                logger.warn("Tentativo di prenotazione con ID cliente non valido: {}", clientId);
                throw new IllegalArgumentException("ID cliente non valido: " + clientId);
            }

            if (!isDateTimeValid(date, hour)) {
                logger.warn("Tentativo di prenotazione per data/ora passata: {} {}", date, hour);
                throw new IllegalArgumentException("La data e l'ora selezionate sono nel passato.");
            }

            String dateTime = date + " " + hour + ":00";

            List<Integer> serviceIds = new ArrayList<>();
            for (String serviceName : selectedServices) {
                int serviceId = serviceDAO.getServiceIdByName(serviceName);
                serviceIds.add(serviceId);
            }

            boolean success = appointmentDAO.bookAppointment(clientId, hairdresserId, dateTime, serviceIds);
            logger.info("Appuntamento prenotato con successo per il parrucchiere ID: {} e cliente ID: {}",
                    hairdresserId, clientId);
            return success;
        } catch (SQLException e) {
            logger.error("Errore SQL nella prenotazione dell'appuntamento per il parrucchiere con ID: {}.",
                    hairdresserId, e);
            throw new RuntimeException("Errore nella prenotazione dell'appuntamento.", e);
        } catch (IllegalArgumentException e) {
            logger.warn("Prenotazione non valida: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica se la data e l'ora selezionate sono valide (non nel passato).
     *
     * @param date la data selezionata
     * @param hour l'ora selezionata
     * @return true se la data e l'ora sono valide, false altrimenti
     */
    public boolean isDateTimeValid(String date, String hour) {
        try {
            String dateTimeString = date + " " + hour;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date selectedDateTime = formatter.parse(dateTimeString);

            Date now = new Date();
            return !selectedDateTime.before(now);
        } catch (ParseException e) {
            logger.error("Errore nel parsing della data e ora: {} {}", date, hour, e);
            throw new IllegalArgumentException("Data o ora non valida.");
        }
    }
}
