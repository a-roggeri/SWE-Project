package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.CancelAppointmentForHairdresserController;
import Tokyogroup.GestioneAppuntamenti.controller.ChangePasswordController;
import Tokyogroup.GestioneAppuntamenti.controller.CreateAppointmentController;
import Tokyogroup.GestioneAppuntamenti.controller.ModifyServicesController;
import Tokyogroup.GestioneAppuntamenti.controller.ViewMessagesController;
import Tokyogroup.GestioneAppuntamenti.controller.WeeklyAppointmentsController;
import Tokyogroup.GestioneAppuntamenti.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Classe che rappresenta la vista del gestore.
 */
public class GestoreView {

    private JFrame frame;
    private final User user;

    /**
     * Costruttore della classe GestoreView.
     *
     * @param user l'utente gestore
     */
    public GestoreView(User user) {
        this.user = user;
        initialize();
    }

    /**
     * Inizializza la vista del gestore.
     */
    private void initialize() {

        frame = new JFrame("Gestore Dashboard");
        frame.setBounds(100, 100, 600, 700);
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
                "<html>Benvenuto, <span style='color:green;'>" + user.getUsername() + "</span> (Gestore)</html>");
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        topPanel.add(welcomePanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        int uniformHeight = 40;

        addGroup(centerPanel, "Gestione Appuntamenti", new String[] {
                "Visualizza appuntamenti settimanali", "Creare un appuntamento", "Disdire un appuntamento" },
                new Runnable[] { this::handleViewAppointments, this::handleCreateAppointment,
                        this::handleCancelAppointment },
                uniformHeight);

        addGroup(centerPanel, "Comunicazione", new String[] {
                "Visualizzare messaggi dai clienti" },
                new Runnable[] { this::handleViewMessages }, uniformHeight);

        addGroup(centerPanel, "Gestione Personale", new String[] {
                "Modificare servizi offerti" },
                new Runnable[] { this::handleModifyServices }, uniformHeight);

        addGroup(centerPanel, "Gestione Account", new String[] {
                "Modificare la password" },
                new Runnable[] { this::handleChangePassword }, uniformHeight);

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
     * Mostra la vista del gestore.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Gestisce la visualizzazione degli appuntamenti settimanali.
     */
    private void handleViewAppointments() {
        frame.dispose();
        WeeklyAppointmentsController controller = new WeeklyAppointmentsController(user);
        SwingUtilities.invokeLater(() -> new WeeklyAppointmentsView(controller, this::show).show());
    }

    /**
     * Gestisce la creazione di un appuntamento.
     */
    private void handleCreateAppointment() {
        frame.dispose();
        CreateAppointmentController controller = new CreateAppointmentController(user);
        SwingUtilities.invokeLater(() -> new CreateAppointmentView(controller, this::show).show());
    }

    /**
     * Gestisce la cancellazione di un appuntamento.
     */
    private void handleCancelAppointment() {
        frame.dispose();
        CancelAppointmentForHairdresserController controller = new CancelAppointmentForHairdresserController(user);
        SwingUtilities.invokeLater(() -> new CancelAppointmentForHairdresserView(controller, this::show).show());
    }

    /**
     * Gestisce la visualizzazione dei messaggi dai clienti.
     */
    private void handleViewMessages() {
        frame.dispose();
        ViewMessagesController controller = new ViewMessagesController(user.getId());
        SwingUtilities.invokeLater(() -> new ViewMessagesView(controller, this::show).show());
    }

    /**
     * Gestisce la modifica dei servizi offerti.
     */
    private void handleModifyServices() {
        frame.dispose();
        ModifyServicesController controller = new ModifyServicesController(user);
        SwingUtilities.invokeLater(() -> new ModifyServicesView(controller, this::show).show());
    }

    /**
     * Gestisce la modifica della password.
     */
    private void handleChangePassword() {
        frame.dispose();
        ChangePasswordController changePasswordController = new ChangePasswordController(user);
        SwingUtilities.invokeLater(() -> new ChangePasswordView(changePasswordController, this::show).show());
    }
}