package Tokyogroup.GestioneAppuntamenti.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe DAO per la gestione degli appuntamenti.
 */
public class AppointmentDAO {

	// Istanza singleton
	private static AppointmentDAO instance;

	/**
	 * Costruttore privato della classe AppointmentDAO.
	 * Inizializza un'istanza per la gestione degli appuntamenti.
	 */
	private AppointmentDAO() {
		// Costruttore vuoto
	}

	/**
	 * Metodo per ottenere l'istanza singleton di AppointmentDAO.
	 *
	 * @return Istanza singleton di AppointmentDAO.
	 */
	public static synchronized AppointmentDAO getInstance() {
		if (instance == null) {
			instance = new AppointmentDAO();
		}
		return instance;
	}

	/**
	 * Recupera gli appuntamenti validi per un cliente.
	 *
	 * @param clientId ID del cliente.
	 * @return Lista di appuntamenti validi.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<String[]> getValidAppointmentsForClient(int clientId) throws SQLException {
		List<String[]> appointments = new ArrayList<>();
		String query = """
				SELECT id, appointmentDate, status, hairdresserId
				FROM Appointments
				WHERE clientId = ? AND status = 'VALIDA'
				ORDER BY appointmentDate ASC;
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, clientId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					appointments.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("appointmentDate"),
							rs.getString("status"), rs.getString("hairdresserId") });
				}
			}
		}
		return appointments;
	}

	/**
	 * Recupera gli appuntamenti settimanali per un parrucchiere.
	 *
	 * @param hairdresserId ID del parrucchiere.
	 * @param startOfWeek   Data di inizio settimana.
	 * @param endOfWeek     Data di fine settimana.
	 * @return Mappa degli appuntamenti settimanali.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public Map<Integer, List<String[]>> getAppointmentsForWeek(int hairdresserId, LocalDate startOfWeek,
			LocalDate endOfWeek) throws SQLException {
		Map<Integer, List<String[]>> weeklyAppointments = new HashMap<>();

		String query = """
				SELECT
				HOUR(appointmentDate) AS "hour",
				((DAY_OF_WEEK(appointmentDate) + 6) % 7) AS dayOfWeek,
				c.username AS clientName,
				a.status,
				GROUP_CONCAT(s.name ORDER BY s.name SEPARATOR ', ') AS services
				FROM Appointments a
				JOIN Users c ON a.clientId = c.id
				JOIN AppointmentServices aps ON a.id = aps.appointmentId
				JOIN Services s ON aps.serviceId = s.id
				WHERE a.hairdresserId = ?
				AND CAST(appointmentDate AS DATE) BETWEEN ? AND ?
				GROUP BY "hour", dayOfWeek, a.id, c.username, a.status
				ORDER BY dayOfWeek, "hour";
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, hairdresserId);
			stmt.setDate(2, java.sql.Date.valueOf(startOfWeek));
			stmt.setDate(3, java.sql.Date.valueOf(endOfWeek));

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int dayOfWeek = rs.getInt("dayOfWeek");
					String hour = rs.getString("hour");
					String clientName = rs.getString("clientName");
					String status = rs.getString("status");
					String services = rs.getString("services");

					weeklyAppointments.computeIfAbsent(dayOfWeek, k -> new ArrayList<>())
							.add(new String[] { hour, clientName, status, services });
				}
			}
		} catch (SQLException e) {
			throw e;
		}

		return weeklyAppointments;
	}

	/**
	 * Calcola il ricavo settimanale per un parrucchiere.
	 *
	 * @param hairdresserId ID del parrucchiere.
	 * @return Ricavo settimanale.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public double calculateWeeklyRevenue(int hairdresserId) throws SQLException {
		String query = """
				SELECT SUM(s.price) AS totalRevenue
				FROM Appointments a
				JOIN AppointmentServices aps ON a.id = aps.appointmentId
				JOIN Services s ON aps.serviceId = s.id
				WHERE a.hairdresserId = ? AND a.status != 'CANCELLATA'
				AND WEEK(a.appointmentDate) = WEEK(CURRENT_DATE);
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, hairdresserId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("totalRevenue");
				}
			}
		}
		return 0.0;
	}

	/**
	 * Recupera gli appuntamenti validi per un parrucchiere.
	 *
	 * @param hairdresserId ID del parrucchiere.
	 * @return Lista di appuntamenti validi.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<String[]> getValidAppointmentsForHairdresser(int hairdresserId) throws SQLException {
		List<String[]> appointments = new ArrayList<>();
		String query = """
				SELECT id, appointmentDate, status, clientId
				FROM Appointments
				WHERE hairdresserId = ? AND status = 'VALIDA' AND appointmentDate > NOW()
				ORDER BY appointmentDate ASC;
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, hairdresserId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					appointments.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("appointmentDate"),
							rs.getString("status"), String.valueOf(rs.getInt("clientId")) });
				}
			}
		}
		return appointments;
	}

	/**
	 * Aggiorna lo stato di un appuntamento.
	 *
	 * @param appointmentId ID dell'appuntamento.
	 * @param status        Nuovo stato dell'appuntamento.
	 * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento del
	 *                      database.
	 */
	public boolean updateAppointmentStatus(int appointmentId, String status) throws SQLException {
		String query = "UPDATE Appointments SET status = ? WHERE id = ?";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, status);
			stmt.setInt(2, appointmentId);

			return stmt.executeUpdate() > 0;
		}
	}

	/**
	 * Recupera tutti i clienti (utenti con accountType "CLIENTE") dal database.
	 *
	 * @return Una lista di oggetti `User` che rappresentano i clienti.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<User> getClients() throws SQLException {
		List<User> clients = new ArrayList<>();
		String query = """
				SELECT id, username, password, accountType, isActive
				FROM Users
				WHERE accountType = 'CLIENTE' AND isActive = TRUE;
				""";

		try (Connection conn = DatabaseManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				clients.add(new User(rs.getInt("id"), // ID utente
						rs.getString("username"), // Nome utente
						rs.getString("password"), // Password
						rs.getString("accountType"), // Tipo di account
						rs.getBoolean("isActive") // Stato dell'account
				));
			}
		} catch (SQLException e) {
			throw new SQLException("Errore durante il recupero dei clienti dal database.", e);
		}

		return clients;
	}

	/**
	 * Cancella tutti gli appuntamenti validi per un utente.
	 *
	 * @param userId ID dell'utente.
	 * @return true se almeno un appuntamento è stato cancellato, false altrimenti.
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento del
	 *                      database.
	 */
	public boolean cancelAppointmentsForUser(int userId) throws SQLException {
		String query = "UPDATE Appointments SET status = 'CANCELLATA' WHERE clientId = ? AND status = 'VALIDA'";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0; // Ritorna true se almeno un appuntamento è stato aggiornato
		} catch (SQLException e) {
			throw new SQLException("Errore durante la cancellazione degli appuntamenti per l'utente ID: " + userId, e);
		}
	}

	/**
	 * Prenota un nuovo appuntamento.
	 *
	 * @param clientId      ID del cliente.
	 * @param hairdresserId ID del parrucchiere.
	 * @param dateTime      Data e ora dell'appuntamento.
	 * @param serviceIds    Lista di ID dei servizi selezionati.
	 * @return true se l'appuntamento è stato prenotato con successo, false
	 *         altrimenti.
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel
	 *                      database.
	 */
	public boolean bookAppointment(int clientId, int hairdresserId, String dateTime, List<Integer> serviceIds)
			throws SQLException {
		String appointmentQuery = """
				INSERT INTO Appointments (clientId, hairdresserId, appointmentDate, status)
				VALUES (?, ?, ?, 'VALIDA');
				""";

		String serviceQuery = """
				INSERT INTO AppointmentServices (appointmentId, serviceId)
				VALUES (?, ?);
				""";

		try (Connection conn = DatabaseManager.getConnection()) {
			conn.setAutoCommit(false); // Inizio transazione

			int appointmentId;
			// Inserisce l'appuntamento
			try (PreparedStatement appointmentStmt = conn.prepareStatement(appointmentQuery,
					Statement.RETURN_GENERATED_KEYS)) {
				appointmentStmt.setInt(1, clientId);
				appointmentStmt.setInt(2, hairdresserId);
				appointmentStmt.setString(3, dateTime);
				appointmentStmt.executeUpdate();

				try (ResultSet generatedKeys = appointmentStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						appointmentId = generatedKeys.getInt(1); // Recupera l'ID dell'appuntamento generato
					} else {
						throw new SQLException("Impossibile recuperare l'ID dell'appuntamento generato.");
					}
				}
			}

			// Inserisce i servizi associati all'appuntamento
			try (PreparedStatement serviceStmt = conn.prepareStatement(serviceQuery)) {
				for (int serviceId : serviceIds) {
					serviceStmt.setInt(1, appointmentId);
					serviceStmt.setInt(2, serviceId);
					serviceStmt.addBatch();
				}
				serviceStmt.executeBatch();
			}

			conn.commit(); // Conferma la transazione
			return true;
		} catch (SQLException e) {
			throw new SQLException("Errore durante la prenotazione dell'appuntamento.", e);
		}
	}

	/**
	 * Recupera gli orari disponibili per un parrucchiere in una data specifica.
	 *
	 * @param hairdresserId ID del parrucchiere.
	 * @param selectedDate           Data richiesta (formato "yyyy-MM-dd").
	 * @return Lista di orari disponibili in formato "HH:00".
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<String> getAvailableHours(int hairdresserId, String selectedDate) throws SQLException {
		String query = """
				SELECT appointmentDate
				FROM Appointments
				WHERE hairdresserId = ?
				AND CAST(appointmentDate AS DATE) = ?
				AND status = 'VALIDA';
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, hairdresserId);
			stmt.setDate(2, java.sql.Date.valueOf(selectedDate)); // Converte la stringa in `java.sql.Date`

			List<String> bookedHours = new ArrayList<>();
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Timestamp appointmentTimestamp = rs.getTimestamp("appointmentDate");
					String hour = appointmentTimestamp.toLocalDateTime().toLocalTime().toString().substring(0, 5);
					bookedHours.add(hour); // Aggiunge solo l'ora (formato HH:mm)
				}
			}
			return bookedHours;
		}
	}

	/**
	 * Recupera i parrucchieri disponibili.
	 *
	 * @return Lista di parrucchieri disponibili.
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<User> getAvailableHairdressers() throws SQLException {
		String query = """
				SELECT id, username, accountType
				FROM Users
				WHERE accountType = 'GESTORE' AND isActive = TRUE;
				""";

		try (Connection conn = DatabaseManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			List<User> hairdressers = new ArrayList<>();
			while (rs.next()) {
				User hairdresser = new User();
				hairdresser.setId(rs.getInt("id"));
				hairdresser.setUsername(rs.getString("username"));
				hairdresser.setAccountType(rs.getString("accountType"));
				hairdressers.add(hairdresser);
			}
			return hairdressers;
		}
	}

	/**
	 * Recupera gli orari prenotati per un parrucchiere in una data specifica.
	 *
	 * @param hairdresserId ID del parrucchiere.
	 * @param selectedDate  Data richiesta (formato "yyyy-MM-dd").
	 * @return Lista di orari prenotati in formato "HH:mm".
	 * @throws SQLException Se si verifica un errore durante l'interrogazione del
	 *                      database.
	 */
	public List<String> getBookedHours(int hairdresserId, String selectedDate) throws SQLException {
		String query = """
				SELECT FORMATDATETIME(appointmentDate, 'HH:mm') AS bookedHour
				FROM Appointments
				WHERE hairdresserId = ?
				AND FORMATDATETIME(appointmentDate, 'yyyy-MM-dd') = ?
				AND status = 'VALIDA';
				""";

		try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, hairdresserId);
			stmt.setString(2, selectedDate); // Passa la data come stringa formattata (yyyy-MM-dd)

			List<String> bookedHours = new ArrayList<>();
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookedHours.add(rs.getString("bookedHour"));
				}
			}
			return bookedHours;
		}
	}
}