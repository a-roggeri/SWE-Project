package Tokyogroup.GestioneAppuntamentiBarbiere.model;

/**
 * Classe che rappresenta un Messaggio.
 */
public class Message {

    private int id;
    private int senderId;
    private String senderUsername;
    private String sentDate;
    private String messageText;
    private String status;

    /**
     * Costruttore completo per la classe Message.
     *
     * @param id             ID del Messaggio
     * @param senderId       ID del Mittente
     * @param senderUsername Nome Utente del Mittente
     * @param sentDate       Data di Invio
     * @param messageText    Contenuto del Messaggio
     * @param status         Stato del Messaggio (LETTO o NON LETTO)
     */
    public Message(int id, int senderId, String senderUsername, String sentDate, String messageText, String status) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.sentDate = sentDate;
        this.messageText = messageText;
        this.status = status;
    }

    /**
     * Restituisce l'ID del Messaggio.
     *
     * @return ID del Messaggio
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID del Messaggio.
     *
     * @param id ID del Messaggio
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'ID del Mittente.
     *
     * @return ID del Mittente
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * Imposta l'ID del Mittente.
     *
     * @param senderId ID del Mittente
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    /**
     * Restituisce il Nome Utente del Mittente.
     *
     * @return Nome Utente del Mittente
     */
    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Imposta il Nome Utente del Mittente.
     *
     * @param senderUsername Nome Utente del Mittente
     */
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    /**
     * Restituisce la Data di Invio del Messaggio.
     *
     * @return Data di Invio del Messaggio
     */
    public String getSentDate() {
        return sentDate;
    }

    /**
     * Imposta la Data di Invio del Messaggio.
     *
     * @param sentDate Data di Invio del Messaggio
     */
    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    /**
     * Restituisce il Contenuto del Messaggio.
     *
     * @return Contenuto del Messaggio
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Imposta il Contenuto del Messaggio.
     *
     * @param messageText Contenuto del Messaggio
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Restituisce lo Stato del Messaggio.
     *
     * @return Stato del Messaggio
     */
    public String getStatus() {
        return status;
    }

    /**
     * Imposta lo Stato del Messaggio.
     *
     * @param status Stato del Messaggio
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del Messaggio.
     *
     * @return Rappresentazione in formato stringa del Messaggio
     */
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", senderUsername='" + senderUsername + '\'' +
                ", sentDate='" + sentDate + '\'' +
                ", messageText='" + messageText + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}