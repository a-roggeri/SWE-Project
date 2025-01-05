package Tokyogroup.GestioneAppuntamenti.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe per la gestione del database.
 */
public class DatabaseManager {

    private static final String DB_PATH = "./resources/data/appointments"; // Percorso del database
    private static final String DB_URL = "jdbc:h2:file:" + DB_PATH; // Prefisso corretto
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Costruttore predefinito della classe DatabaseManager.
     * Inizializza un'istanza per la gestione delle operazioni sul database.
     */
    public DatabaseManager() {
        // Costruttore vuoto
    }
    
    /**
     * Ottiene una connessione al database.
     *
     * @return la connessione al database
     * @throws SQLException se si verifica un errore durante la connessione
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 non trovato", e);
        }

        createDatabaseDirectory();

        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * Crea la directory del database se non esiste.
     */
    private static void createDatabaseDirectory() {
        File directory = new File("./resources/data");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException(
                    "Impossibile creare la directory del database in " + directory.getAbsolutePath());
        }
    }

    /**
     * Inizializza il database creando le tabelle necessarie.
     *
     * @throws SQLException se si verifica un errore durante l'inizializzazione
     */
    public static void initializeDatabase() throws SQLException {
        String[] tableCreationQueries = {
                """
                        CREATE TABLE IF NOT EXISTS Users (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(255) UNIQUE NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            accountType ENUM('CLIENTE', 'GESTORE') NOT NULL,
                            isActive BOOLEAN DEFAULT TRUE
                        );
                        """,
                """
                        CREATE TABLE IF NOT EXISTS Appointments (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            clientId INT NOT NULL,
                            hairdresserId INT NOT NULL,
                            appointmentDate DATETIME NOT NULL,
                            status ENUM('VALIDA', 'CANCELLATA', 'ESEGUITA') NOT NULL DEFAULT 'VALIDA',
                            FOREIGN KEY (clientId) REFERENCES Users(id) ON DELETE CASCADE,
                            FOREIGN KEY (hairdresserId) REFERENCES Users(id) ON DELETE CASCADE
                        );
                        """,
                """
                        CREATE TABLE IF NOT EXISTS Services (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) UNIQUE NOT NULL,
                            price DECIMAL(10, 2) NOT NULL
                        );
                        """,
                """
                        CREATE TABLE IF NOT EXISTS HairdresserServices (
                            hairdresserId INT NOT NULL,
                            serviceId INT NOT NULL,
                            PRIMARY KEY (hairdresserId, serviceId),
                            FOREIGN KEY (hairdresserId) REFERENCES Users(id) ON DELETE CASCADE,
                            FOREIGN KEY (serviceId) REFERENCES Services(id)
                        );
                        """,
                """
                        CREATE TABLE IF NOT EXISTS AppointmentServices (
                            appointmentId INT NOT NULL,
                            serviceId INT NOT NULL,
                            PRIMARY KEY (appointmentId, serviceId),
                            FOREIGN KEY (appointmentId) REFERENCES Appointments(id) ON DELETE CASCADE,
                            FOREIGN KEY (serviceId) REFERENCES Services(id)
                        );
                        """,
                """
                        CREATE TABLE IF NOT EXISTS Messages (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            senderId INT NOT NULL,
                            receiverId INT NOT NULL,
                            messageText TEXT NOT NULL,
                            status ENUM('LETTO', 'NON LETTO') NOT NULL DEFAULT 'NON LETTO',
                            sentDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (senderId) REFERENCES Users(id) ON DELETE CASCADE,
                            FOREIGN KEY (receiverId) REFERENCES Users(id)
                        );
                        """
        };

        try (Connection connection = getConnection();
                Statement stmt = connection.createStatement()) {
            for (String query : tableCreationQueries) {
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'inizializzazione del database", e);
        }
    }

    /**
     * Aggiorna gli appuntamenti passati impostando lo stato a 'ESEGUITA'.
     *
     * @throws SQLException se si verifica un errore durante l'aggiornamento
     */
    public static void updatePastAppointments() throws SQLException {
        String query = """
                    UPDATE Appointments
                    SET status = 'ESEGUITA'
                    WHERE appointmentDate < NOW() AND status NOT IN ('CANCELLATA', 'ESEGUITA');
                """;

        try (Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento degli appuntamenti passati", e);
        }
    }
}
