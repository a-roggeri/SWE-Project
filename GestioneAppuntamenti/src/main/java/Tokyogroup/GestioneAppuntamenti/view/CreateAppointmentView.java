package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.CreateAppointmentController;
import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la visualizzazione della creazione di un appuntamento.
 */
public class CreateAppointmentView {
    private final CreateAppointmentController createAppointmentController;
    private final Runnable onCloseAction;
    private JFrame frame;
    private JComboBox<User> clientComboBox;
    private JSpinner dateSpinner;
    private JComboBox<String> hourComboBox;
    private JTable serviceTable;
    private JLabel messageLabel;

    /**
     * Costruttore della classe CreateAppointmentView.
     *
     * @param createAppointmentController il controller per la creazione degli
     *                                    appuntamenti
     * @param onCloseAction               azione da eseguire alla chiusura della
     *                                    finestra
     */
    public CreateAppointmentView(CreateAppointmentController createAppointmentController, Runnable onCloseAction) {
        this.createAppointmentController = createAppointmentController;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Mostra la finestra di creazione dell'appuntamento.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Crea Appuntamento");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Crea un Appuntamento");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);

        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel clientLabel = new JLabel("Seleziona Cliente:");
        clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(clientLabel);

        clientComboBox = new JComboBox<>();
        clientComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        clientComboBox.addActionListener(e -> loadServicesForClient());
        clientComboBox.addActionListener(this::onHairdresserSelected);
        topPanel.add(clientComboBox);

        JLabel dateLabel = new JLabel("Seleziona Data e Ora:");
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(dateLabel);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(datePanel);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        dateSpinner.addChangeListener(e -> updateAvailableHours());
        datePanel.add(dateSpinner);

        hourComboBox = new JComboBox<>();
        hourComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        datePanel.add(hourComboBox);

        JLabel serviceLabel = new JLabel("Seleziona Servizi:");
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(serviceLabel);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Servizio", "Costo", "Seleziona" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };

        serviceTable = new JTable(tableModel);
        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        serviceScrollPane.setPreferredSize(new Dimension(300, 150));
        topPanel.add(serviceScrollPane);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton createButton = new JButton("Crea");
        createButton.setPreferredSize(new Dimension(0, 40));
        createButton.addActionListener(this::onCreateAppointment);
        buttonPanel.add(createButton);

        try {
            loadClients();
        } catch (Exception e) {
            showErrorMessage("Errore durante il caricamento dei clienti.");
        }
    }

    /**
     * Metodo chiamato quando viene selezionato un parrucchiere.
     *
     * @param e evento di selezione
     */
    private void onHairdresserSelected(ActionEvent e) {
        try {
            User selectedHairdresser = (User) clientComboBox.getSelectedItem();
            if (selectedHairdresser != null) {
                updateAvailableHours();
            }
        } catch (Exception ex) {
            showErrorMessage("Errore durante l'aggiornamento degli orari disponibili.");
        }
    }

    /**
     * Carica i servizi disponibili per il cliente selezionato.
     */
    private void loadServicesForClient() {
        try {
            DefaultTableModel model = (DefaultTableModel) serviceTable.getModel();
            model.setRowCount(0);

            User selectedClient = (User) clientComboBox.getSelectedItem();
            if (selectedClient == null) {
                return;
            }

            List<Service> services = createAppointmentController.getServicesForCurrentHairdresser();
            for (Service service : services) {
                model.addRow(new Object[] { service.getName(), String.format("€%.2f", service.getPrice()), false });
            }
        } catch (Exception e) {
            showErrorMessage("Errore durante il caricamento dei servizi per il cliente.");
        }
    }

    /**
     * Carica i clienti disponibili.
     */
    private void loadClients() {
        try {
            List<User> clients = createAppointmentController.getClients();
            clientComboBox.removeAllItems();
            for (User client : clients) {
                clientComboBox.addItem(client);
            }
        } catch (Exception e) {
            showErrorMessage("Errore durante il caricamento dei clienti.");
        }
    }

    /**
     * Aggiorna gli orari disponibili per la data selezionata.
     */
    private void updateAvailableHours() {
        try {
            String selectedDate = ((JSpinner.DateEditor) dateSpinner.getEditor()).getFormat()
                    .format(dateSpinner.getValue());
            List<String> availableHours = createAppointmentController.getAvailableHoursForDate(selectedDate);
            hourComboBox.removeAllItems();
            for (String hour : availableHours) {
                hourComboBox.addItem(hour);
            }
        } catch (Exception e) {
            showErrorMessage("Errore durante il caricamento degli orari disponibili.");
        }
    }

    /**
     * Metodo chiamato quando viene creato un appuntamento.
     *
     * @param e evento di creazione
     */
    private void onCreateAppointment(ActionEvent e) {
        try {
            User selectedClient = (User) clientComboBox.getSelectedItem();
            String selectedDate = ((JSpinner.DateEditor) dateSpinner.getEditor()).getFormat()
                    .format(dateSpinner.getValue());
            String selectedHour = (String) hourComboBox.getSelectedItem();
            DefaultTableModel model = (DefaultTableModel) serviceTable.getModel();

            List<String> selectedServices = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((boolean) model.getValueAt(i, 2)) {
                    selectedServices.add((String) model.getValueAt(i, 0));
                }
            }

            if (selectedClient == null || selectedHour == null || selectedServices.isEmpty()) {
                showErrorMessage("Seleziona tutti i campi obbligatori.");
                return;
            }

            boolean success = createAppointmentController.createAppointment(selectedClient.getId(), selectedDate,
                    selectedHour, selectedServices);

            if (success) {
                showSuccessMessage("Appuntamento creato con successo!");
                updateAvailableHours();
            } else {
                showErrorMessage("Errore nella creazione dell'appuntamento.");
            }
        } catch (Exception ex) {
            showErrorMessage("Errore durante la creazione dell'appuntamento.");
        }
    }

    /**
     * Mostra un messaggio di errore.
     *
     * @param message il messaggio di errore
     */
    private void showErrorMessage(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
    }

    /**
     * Mostra un messaggio di successo.
     *
     * @param message il messaggio di successo
     */
    private void showSuccessMessage(String message) {
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setText(message);
    }
}