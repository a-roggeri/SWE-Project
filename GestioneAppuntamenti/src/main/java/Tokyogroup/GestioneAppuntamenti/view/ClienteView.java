package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.AppointmentController;
import Tokyogroup.GestioneAppuntamenti.controller.CancelAppointmentController;
import Tokyogroup.GestioneAppuntamenti.controller.ChangePasswordController;
import Tokyogroup.GestioneAppuntamenti.controller.MessageController;
import Tokyogroup.GestioneAppuntamenti.model.AppointmentDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Classe ClienteView rappresenta la vista della dashboard del cliente.
 */
public class ClienteView {
    private JFrame frame;
    private final User user;

    /**
     * Costruttore della classe ClienteView.
     *
     * @param user l'utente corrente
     */
    public ClienteView(User user) {
        this.user = user;
        initialize();
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {

        frame = new JFrame("Cliente Dashboard");
        frame.setBounds(100, 100, 600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("./resources/images/logos.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(frame.getWidth(), 150, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(logoLabel, BorderLayout.NORTH);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setText(
                "<html>Benvenuto, <span style='color:green;'>" + user.getUsername() + "</span> (Cliente)</html>");
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        topPanel.add(welcomePanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        int uniformHeight = 40;

        addGroup(centerPanel, "Gestione Appuntamenti", new String[] {
                "Prenotare un appuntamento", "Disdire un appuntamento" },
                new Runnable[] { this::openAppointmentView, this::handleCancelAppointment }, uniformHeight);

        addGroup(centerPanel, "Comunicazione", new String[] { "Lasciare un messaggio" },
                new Runnable[] { this::handleLeaveMessage }, uniformHeight);

        addGroup(centerPanel, "Gestione Account", new String[] { "Modificare la password", "Eliminare l'account" },
                new Runnable[] { this::handleChangePassword, this::handleDeleteAccount }, uniformHeight);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel logoutButtonPanel = new JPanel();
        logoutButtonPanel.setLayout(new GridLayout(1, 1));
        logoutButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(logoutButtonPanel, BorderLayout.SOUTH);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(0, 40));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(
                    () -> new LoginView(new Tokyogroup.GestioneAppuntamenti.controller.LoginController())
                            .show());
        });
        logoutButtonPanel.add(logoutButton);
    }

    /**
     * Aggiunge un gruppo di pulsanti al pannello principale.
     *
     * @param parentPanel   il pannello principale
     * @param groupTitle    il titolo del gruppo
     * @param buttonLabels  le etichette dei pulsanti
     * @param actions       le azioni associate ai pulsanti
     * @param uniformHeight l'altezza uniforme dei pulsanti
     */
    private void addGroup(JPanel parentPanel, String groupTitle, String[] buttonLabels, Runnable[] actions,
            int uniformHeight) {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel groupLabel = new JLabel(groupTitle);
        groupLabel.setFont(new Font("Arial", Font.BOLD, 14));
        groupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupPanel.add(groupLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < buttonLabels.length; i++) {
            final int index = i;
            JButton button = new JButton(buttonLabels[i]);
            button.setPreferredSize(new Dimension(200, uniformHeight));
            button.setMinimumSize(new Dimension(200, uniformHeight));
            button.setMaximumSize(new Dimension(200, uniformHeight));
            button.addActionListener(e -> {
                actions[index].run();
            });
            buttonPanel.add(button);
        }

        groupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        groupPanel.add(buttonPanel);

        parentPanel.add(groupPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    /**
     * Mostra la finestra ClienteView.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Apre la vista degli appuntamenti.
     */
    private void openAppointmentView() {
        frame.dispose();
        AppointmentController controller = new AppointmentController(user);
        SwingUtilities.invokeLater(() -> new AppointmentView(controller, this::show).show());
    }

    /**
     * Gestisce la cancellazione di un appuntamento.
     */
    private void handleCancelAppointment() {
        frame.dispose();
        CancelAppointmentController controller = new CancelAppointmentController(user);
        SwingUtilities.invokeLater(() -> new CancelAppointmentView(controller, this::show).show());
    }

    /**
     * Gestisce l'azione di lasciare un messaggio.
     */
    private void handleLeaveMessage() {
        frame.dispose();
        MessageController messageController = new MessageController(user);
        SwingUtilities.invokeLater(() -> new MessageView(messageController, this::show).show());
    }

    /**
     * Gestisce la modifica della password.
     */
    private void handleChangePassword() {
        frame.dispose();
        ChangePasswordController changePasswordController = new ChangePasswordController(user);
        SwingUtilities.invokeLater(() -> new ChangePasswordView(changePasswordController, this::show).show());
    }

    /**
     * Gestisce l'eliminazione dell'account.
     */
    private void handleDeleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Sei sicuro di voler eliminare il tuo account? Questa operazione è irreversibile.",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UserDAO userDAO = UserDAO.getInstance();
                boolean successUserDAO = userDAO.deleteAccount(user.getId());
                if (successUserDAO) {
                    AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();
                    boolean successAppointmentDAO = appointmentDAO.cancelAppointmentsForUser(user.getId());
                    if (successAppointmentDAO) {
                        JOptionPane.showMessageDialog(frame,
                                "Account eliminato con successo. Tutti i tuoi appuntamenti sono stati disdetti.");
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Account eliminato con successo. Errore durante la disdetta degli appuntamenti relativi all'account.");
                    }
                    frame.dispose();
                    SwingUtilities.invokeLater(() -> new LoginView(
                            new Tokyogroup.GestioneAppuntamenti.controller.LoginController()).show());
                } else {
                    JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione dell'account. Riprova.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Errore durante la cancellazione dell'account: " + e.getMessage());
            }
        }
    }
}