package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller per la gestione degli appuntamenti settimanali.
 */
public class WeeklyAppointmentsController {

    private final AppointmentDAO appointmentDAO;
    private final User currentHairdresser;

    /**
     * Costruttore della classe WeeklyAppointmentsController.
     *
     * @param currentHairdresser Il parrucchiere corrente.
     */
    public WeeklyAppointmentsController(User currentHairdresser) {
        this.appointmentDAO = new AppointmentDAO();
        this.currentHairdresser = currentHairdresser;
    }

    /**
     * Recupera gli appuntamenti settimanali per il parrucchiere corrente.
     *
     * @return Una mappa contenente gli appuntamenti settimanali.
     */
    public Map<Integer, List<String[]>> getWeeklyAppointments() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            return appointmentDAO.getAppointmentsForWeek(currentHairdresser.getId(), startOfWeek, endOfWeek);
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero degli appuntamenti settimanali.", e);
        }
    }

    /**
     * Calcola il fatturato settimanale per il parrucchiere corrente.
     *
     * @return Il fatturato settimanale.
     */
    public double calculateWeeklyRevenue() {
        try {
            return appointmentDAO.calculateWeeklyRevenue(currentHairdresser.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il calcolo del fatturato settimanale.", e);
        }
    }
}