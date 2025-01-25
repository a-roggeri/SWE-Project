package Tokyogroup.GestioneAppuntamenti;

import com.formdev.flatlaf.FlatDarculaLaf;

import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Tokyogroup.GestioneAppuntamenti.controller.LoginController;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.view.LoginView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe principale dell'applicazione di gestione appuntamenti per barbieri.
 * Questa classe contiene il metodo main che avvia l'applicazione e gestisce
 * l'inizializzazione del database, la pulizia degli appuntamenti obsoleti,
 * l'impostazione del look and feel e l'avvio della vista di login.
 * 
 * <p>
 * Il flusso principale dell'applicazione è il seguente:
 * <ol>
 * <li>Inizializzazione del database</li>
 * <li>Pulizia degli appuntamenti obsoleti</li>
 * <li>Impostazione del look and feel</li>
 * <li>Avvio della vista di login</li>
 * </ol>
 * 
 * <p>
 * In caso di errori durante l'inizializzazione del database o la pulizia
 * degli appuntamenti obsoleti, l'applicazione verrà terminata con un messaggio
 * di errore appropriato.
 * 
 * <p>
 * In caso di errore durante l'impostazione del look and feel, verrà utilizzato
 * il look and feel predefinito.
 * 
 * <p>
 * In caso di errore durante l'avvio della vista di login, l'applicazione verrà
 * terminata con un messaggio di errore appropriato.
 * 
 * <p>
 * La classe utilizza un logger per registrare informazioni e messaggi di errore
 * durante l'esecuzione.
 * 
 * @see DatabaseManager
 * @see LoginController
 * @see LoginView
 * @see FlatDarculaLaf
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * Costruttore predefinito della classe App.
     * Inizializza un'istanza della classe principale dell'applicazione.
     */
    public App() {
        // Costruttore vuoto
    }
    
    /**
     * Metodo principale che avvia l'applicazione.
     *
     * @param args Argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        logger.info("Avvio dell'applicazione...");
        try {
            DatabaseManager.initializeDatabase();
            logger.info("Database inizializzato con successo.");
        } catch (SQLException e) {
            logger.error("Inizializzazione del database fallita a causa di un'eccezione SQL.", e);
            terminateApplication("Inizializzazione del database fallita. L'applicazione verrà chiusa.");
        } catch (Exception e) {
            logger.error("Errore imprevisto durante l'inizializzazione del database.", e);
            terminateApplication("Errore imprevisto. L'applicazione verrà chiusa.");
        }

        try {
            logger.info("Pulizia degli appuntamenti obsoleti...");
            DatabaseManager.updatePastAppointments();
            logger.info("Appuntamenti obsoleti puliti con successo.");
        } catch (SQLException e) {
            logger.error("Aggiornamento degli appuntamenti obsoleti fallito.", e);
            terminateApplication("Pulizia degli appuntamenti obsoleti fallita. L'applicazione verrà chiusa.");
        }

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
            logger.info("Look and Feel impostato su FlatDarculaLaf.");
        } catch (Exception e) {
            logger.warn("Impostazione del Look and Feel fallita. Utilizzo del look and feel predefinito.", e);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                logger.info("Avvio della LoginView...");
                LoginController loginController = new LoginController();
                LoginView loginView = new LoginView(loginController);
                loginView.show();
                logger.info("LoginView avviata con successo.");
            } catch (Exception e) {
                logger.error("Avvio della LoginView fallito.", e);
                terminateApplication("Avvio della LoginView fallito. L'applicazione verrà chiusa.");
            }
        });

        logger.info("Applicazione avviata.");
    }

    /**
     * Termina l'applicazione con un messaggio di log e un'uscita pulita.
     *
     * @param message Il messaggio da loggare prima di uscire.
     */
    private static void terminateApplication(String message) {
        logger.error(message);
        System.exit(1);
    }
}
