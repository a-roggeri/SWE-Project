package Tokyogroup.GestioneAppuntamenti.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DAO per la gestione degli utenti.
 */
public class UserDAO {

	// Istanza Singleton
	private static UserDAO instance;

	/**
	 * Costruttore privato della classe UserDAO.
	 * Inizializza un'istanza per la gestione delle operazioni sugli utenti.
	 */
	private UserDAO() {
		// Costruttore vuoto
	}

	/**
	 * Metodo per ottenere l'istanza Singleton di UserDAO.
	 *
	 * @return l'istanza Singleton di UserDAO
	 */
	public static synchronized UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	/**
	 * Trova un utente nel database con le credenziali specificate.
	 *
	 * @param username il nome utente da cercare
	 * @param password la password da verificare
	 * @return un oggetto User se trovato, altrimenti null
	 * @throws SQLException in caso di errori durante l'accesso al database
	 */
	public User findUser(String username, String password) throws SQLException {
		String query = "SELECT id, username, password, accountType, isActive FROM Users WHERE username = ? AND password = ?";
		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			stmt.setString(2, password);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getInt("id"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("accountType"),
							rs.getBoolean("isActive"));
				}
			}
		}
		return null;
	}

	/**
	 * Elimina un account utente disattivandolo.
	 *
	 * @param userId l'ID dell'utente da disattivare
	 * @return true se l'utente è stato disattivato correttamente, false altrimenti
	 * @throws SQLException in caso di errori durante l'accesso al database
	 */
	public boolean deleteAccount(int userId) throws SQLException {
		boolean userDeactivated = deactivateUser(userId);
		return userDeactivated;
	}

	/**
	 * Aggiorna la password di un utente.
	 *
	 * @param userId      l'ID dell'utente
	 * @param oldPassword la vecchia password
	 * @param newPassword la nuova password
	 * @return true se la password è stata aggiornata correttamente, false
	 *         altrimenti
	 * @throws SQLException in caso di errori durante l'accesso al database
	 */
	public boolean updatePassword(int userId, String oldPassword, String newPassword) throws SQLException {
		String query = "UPDATE Users SET password = ? WHERE id = ? AND password = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, newPassword);
			stmt.setInt(2, userId);
			stmt.setString(3, oldPassword);

			int rowsUpdated = stmt.executeUpdate();
			boolean success = rowsUpdated > 0;
			return success;
		} catch (SQLException e) {
			throw new SQLException("Errore durante l'aggiornamento della password per l'utente ID: " + userId, e);
		}
	}

	/**
	 * Ripristina un account utente disattivato.
	 *
	 * @param userId l'ID dell'utente da ripristinare
	 * @return true se l'account è stato ripristinato correttamente, false
	 *         altrimenti
	 * @throws SQLException in caso di errori durante l'accesso al database
	 */
	public boolean restoreUserAccount(int userId) throws SQLException {
		String query = "UPDATE Users SET isActive = true WHERE id = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userId);

			int rowsUpdated = stmt.executeUpdate();
			boolean success = rowsUpdated > 0;
			return success;
		} catch (SQLException e) {
			throw new SQLException("Errore durante il ripristino dell'account per l'utente con ID: " + userId, e);
		}
	}

	/**
	 * Aggiunge un nuovo utente al database.
	 *
	 * @param user l'utente da aggiungere
	 * @return true se l'utente è stato aggiunto correttamente, false altrimenti
	 * @throws SQLException in caso di errori durante l'inserimento nel database
	 */
	public boolean addUser(User user) throws SQLException {
		String query = "INSERT INTO Users (username, password, accountType) VALUES (?, ?, ?)";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getAccountType());

			int rowsInserted = stmt.executeUpdate();
			boolean success = rowsInserted > 0;
			return success;
		} catch (SQLException e) {
			throw new SQLException("Errore durante l'aggiunta dell'utente: " + user.getUsername(), e);
		}
	}

	/**
	 * Disattiva un utente nel database.
	 *
	 * @param userId l'ID dell'utente da disattivare
	 * @return true se l'utente è stato disattivato correttamente, false altrimenti
	 * @throws SQLException in caso di errori durante l'accesso al database
	 */
	public boolean deactivateUser(int userId) throws SQLException {
		String query = "UPDATE Users SET isActive = false WHERE id = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userId);

			int rowsUpdated = stmt.executeUpdate();
			boolean success = rowsUpdated > 0;
			return success;
		} catch (SQLException e) {
			throw new SQLException("Errore durante la disattivazione dell'utente con ID: " + userId, e);
		}
	}
}