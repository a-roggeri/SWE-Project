package Tokyogroup.GestioneAppuntamenti.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO per la gestione dei servizi.
 */
public class ServiceDAO {

	/**
	 * Costruttore predefinito della classe ServiceDAO.
	 * Inizializza un'istanza per la gestione delle operazioni sui servizi.
	 */
	public ServiceDAO() {
	    // Costruttore vuoto
	}
	
    /**
     * Recupera i servizi offerti da un parrucchiere come oggetti `Service`.
     *
     * @param hairdresserId ID del parrucchiere.
     * @return Lista di oggetti `Service` con nome e prezzo.
     */
    public List<Service> getServicesByHairdresser(int hairdresserId) {
        List<Service> services = new ArrayList<>();
        String query = """
                    SELECT s.id, s.name, s.price
                    FROM Services s
                    JOIN HairdresserServices hs ON s.id = hs.serviceId
                    WHERE hs.hairdresserId = ?;
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hairdresserId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(new Service(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Errore durante il recupero dei servizi per il parrucchiere con ID: " + hairdresserId, e);
        }
        return services;
    }

    /**
     * Cancella gli appuntamenti con un determinato servizio per un parrucchiere.
     *
     * @param serviceId     ID del servizio.
     * @param hairdresserId ID del parrucchiere.
     * @return true se almeno un appuntamento è stato aggiornato.
     */
    public boolean cancelAppointmentsWithService(int serviceId, int hairdresserId) {
        String query = """
                    UPDATE Appointments
                    SET status = 'CANCELLATA'
                    WHERE id IN (
                        SELECT DISTINCT a.id
                        FROM Appointments a
                        JOIN AppointmentServices asrv ON a.id = asrv.appointmentId
                        WHERE a.hairdresserId = ? AND asrv.serviceId = ? AND a.status = 'VALIDA' AND a.appointmentDate > NOW()
                    )
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hairdresserId);
            stmt.setInt(2, serviceId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Errore durante la cancellazione degli appuntamenti per il servizio ID: " + serviceId, e);
        }
    }

    /**
     * Recupera l'ID di un servizio dato il suo nome.
     *
     * @param serviceName Nome del servizio.
     * @return ID del servizio.
     */
    public int getServiceIdByName(String serviceName) {
        String query = "SELECT id FROM Services WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, serviceName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new RuntimeException("Servizio non trovato: " + serviceName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'ID per il servizio: " + serviceName, e);
        }
    }

    /**
     * Recupera tutti i servizi disponibili come oggetti `Service`.
     *
     * @return Lista di tutti i servizi (oggetti `Service`).
     */
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String query = "SELECT id, name, price FROM Services";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero di tutti i servizi.", e);
        }
        return services;
    }

    /**
     * Recupera i servizi disponibili per un parrucchiere.
     *
     * @param hairdresserId ID del parrucchiere.
     * @return Lista di servizi disponibili per l'aggiunta.
     */
    public List<Service> getAvailableServicesForHairdresser(int hairdresserId) {
        String query = """
                    SELECT s.id, s.name, s.price
                    FROM Services s
                    WHERE s.id NOT IN (
                        SELECT serviceId FROM HairdresserServices WHERE hairdresserId = ?
                    )
                """;

        List<Service> services = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hairdresserId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(new Service(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dei servizi disponibili per il parrucchiere.", e);
        }
        return services;
    }

    /**
     * Rimuove un servizio dal parrucchiere.
     *
     * @param hairdresserId ID del parrucchiere.
     * @param serviceId     ID del servizio da rimuovere.
     * @return true se l'operazione è stata completata con successo.
     */
    public boolean removeServiceFromHairdresser(int hairdresserId, int serviceId) {
        String query = "DELETE FROM HairdresserServices WHERE hairdresserId = ? AND serviceId = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hairdresserId);
            stmt.setInt(2, serviceId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la rimozione del servizio dal parrucchiere.", e);
        }
    }

    /**
     * Aggiunge un servizio esistente al parrucchiere.
     *
     * @param hairdresserId ID del parrucchiere.
     * @param serviceId     ID del servizio da aggiungere.
     * @return true se l'operazione è stata completata con successo.
     */
    public boolean addServiceToHairdresser(int hairdresserId, int serviceId) {
        String query = "INSERT INTO HairdresserServices (hairdresserId, serviceId) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hairdresserId);
            stmt.setInt(2, serviceId);

            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'associazione del servizio al parrucchiere.", e);
        }
    }

    /**
     * Aggiunge un nuovo servizio al database.
     *
     * @param name  Nome del servizio.
     * @param price Prezzo del servizio.
     * @return ID del servizio aggiunto.
     */
    public int addNewService(String name, double price) {
        String query = "INSERT INTO Services (name, price) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Errore durante l'aggiunta del servizio: nessuna riga aggiunta.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Errore durante l'aggiunta del servizio: ID non generato.");
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Il servizio esiste già: " + name, e);
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiunta del servizio: " + name, e);
        }
    }

    /**
     * Aggiunge un nuovo servizio al database.
     *
     * @param service Il servizio da aggiungere.
     * @return true se il servizio è stato aggiunto con successo.
     */
    public boolean addService(Service service) {
        String query = "INSERT INTO Services (name, price) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            stmt.setDouble(2, service.getPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiunta del servizio: " + service.getName(), e);
        }
    }
}