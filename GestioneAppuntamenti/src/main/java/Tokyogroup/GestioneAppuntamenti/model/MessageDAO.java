package Tokyogroup.GestioneAppuntamenti.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO per la gestione dei messaggi.
 */
public class MessageDAO {

    // Istanza unica della classe MessageDAO
    private static MessageDAO instance;

    /**
     * Costruttore privato della classe MessageDAO.
     * Inizializza un'istanza per la gestione delle operazioni sui messaggi.
     */
    private MessageDAO() {
        // Costruttore vuoto
    }

    /**
     * Metodo per ottenere l'istanza unica della classe MessageDAO.
     *
     * @return l'istanza unica della classe MessageDAO.
     */
    public static synchronized MessageDAO getInstance() {
        if (instance == null) {
            instance = new MessageDAO();
        }
        return instance;
    }

    /**
     * Recupera la lista dei gestori attivi.
     *
     * @return una lista di oggetti User rappresentanti i gestori attivi.
     */
    public List<User> getManagers() {
        String query = "SELECT id, username, password FROM Users WHERE accountType = 'GESTORE' AND isActive = true";
        List<User> managers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                managers.add(
                        new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), "GESTORE", true));
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il caricamento dei gestori.", e);
        }

        return managers;
    }

    /**
     * Aggiunge un nuovo messaggio al database.
     *
     * @param senderId    ID del mittente.
     * @param receiverId  ID del destinatario.
     * @param messageText Testo del messaggio.
     * @return true se l'inserimento è avvenuto con successo, false altrimenti.
     * @throws Exception se si verifica un errore durante l'inserimento.
     */
    public boolean addMessage(int senderId, int receiverId, String messageText) throws Exception {
        String query = "INSERT INTO Messages (senderId, receiverId, messageText, status) VALUES (?, ?, ?, 'NON LETTO')";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, messageText);

            boolean result = stmt.executeUpdate() > 0;
            return result;
        }
    }

    /**
     * Recupera i messaggi non letti per un determinato gestore.
     *
     * @param managerId ID del gestore.
     * @return una lista di oggetti Message rappresentanti i messaggi non letti.
     */
    public List<Message> getUnreadMessages(int managerId) {
        String query = """
                SELECT m.id,
                       m.senderId,
                       u.username AS senderUsername,
                       m.sentDate,
                       m.messageText
                FROM Messages m
                JOIN Users u ON m.senderId = u.id
                WHERE m.receiverId = ? AND m.status = 'NON LETTO'
                """;

        List<Message> messages = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, managerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(
                            rs.getInt("id"), // ID Messaggio
                            rs.getInt("senderId"), // ID Mittente
                            rs.getString("senderUsername"), // Nome Utente Mittente
                            rs.getString("sentDate"), // Data Invio
                            rs.getString("messageText"), // Messaggio
                            "NON LETTO" // Stato Messaggio
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il caricamento dei messaggi non letti.", e);
        }

        return messages;
    }

    /**
     * Aggiorna lo stato di un messaggio.
     *
     * @param messageId ID del messaggio.
     * @param status    Nuovo stato del messaggio.
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
     */
    public boolean updateMessageStatus(int messageId, String status) {
        String query = "UPDATE Messages SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, messageId);
            boolean result = stmt.executeUpdate() > 0;
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiornamento dello stato del messaggio.", e);
        }
    }
}