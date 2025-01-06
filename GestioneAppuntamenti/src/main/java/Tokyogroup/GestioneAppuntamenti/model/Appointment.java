package Tokyogroup.GestioneAppuntamenti.model;

import java.util.List;

/**
 * Classe che rappresenta un appuntamento.
 */
public class Appointment {

    private int id;
    private int clientId;
    private int hairdresserId;
    private String date; // Formato: "yyyy-MM-dd"
    private String time; // Formato: "HH:00"
    private String status; // "VALIDA", "CANCELLATA", "ESEGUITA"
    private List<Service> services;

    /**
     * Costruttore completo.
     *
     * @param id            l'ID dell'appuntamento
     * @param clientId      l'ID del cliente
     * @param hairdresserId l'ID del parrucchiere
     * @param date          la data dell'appuntamento
     * @param time          l'ora dell'appuntamento
     * @param status        lo stato dell'appuntamento
     * @param services      la lista dei servizi dell'appuntamento
     */
    public Appointment(int id, int clientId, int hairdresserId, String date, String time, String status,
            List<Service> services) {
        this.id = id;
        this.clientId = clientId;
        this.hairdresserId = hairdresserId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.services = services;
    }

    /**
     * Restituisce l'ID dell'appuntamento.
     *
     * @return l'ID dell'appuntamento
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID dell'appuntamento.
     *
     * @param id l'ID dell'appuntamento
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'ID del cliente.
     *
     * @return l'ID del cliente
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Imposta l'ID del cliente.
     *
     * @param clientId l'ID del cliente
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Restituisce l'ID del parrucchiere.
     *
     * @return l'ID del parrucchiere
     */
    public int getHairdresserId() {
        return hairdresserId;
    }

    /**
     * Imposta l'ID del parrucchiere.
     *
     * @param hairdresserId l'ID del parrucchiere
     */
    public void setHairdresserId(int hairdresserId) {
        this.hairdresserId = hairdresserId;
    }

    /**
     * Restituisce la data dell'appuntamento.
     *
     * @return la data dell'appuntamento
     */
    public String getDate() {
        return date;
    }

    /**
     * Imposta la data dell'appuntamento.
     *
     * @param date la data dell'appuntamento
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Restituisce l'ora dell'appuntamento.
     *
     * @return l'ora dell'appuntamento
     */
    public String getTime() {
        return time;
    }

    /**
     * Imposta l'ora dell'appuntamento.
     *
     * @param time l'ora dell'appuntamento
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Restituisce lo stato dell'appuntamento.
     *
     * @return lo stato dell'appuntamento
     */
    public String getStatus() {
        return status;
    }

    /**
     * Imposta lo stato dell'appuntamento.
     *
     * @param status lo stato dell'appuntamento
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Restituisce la lista dei servizi dell'appuntamento.
     *
     * @return la lista dei servizi dell'appuntamento
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Imposta la lista dei servizi dell'appuntamento.
     *
     * @param services la lista dei servizi dell'appuntamento
     */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'appuntamento.
     *
     * @return una stringa che rappresenta l'appuntamento
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", hairdresserId=" + hairdresserId +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", services=" + services +
                '}';
    }
}