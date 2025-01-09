package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.CancelAppointmentForHairdresserController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
/**
 * Classe che rappresenta la vista per la cancellazione degli appuntamenti per il parrucchiere.
 */
public class CancelAppointmentForHairdresserView {
    private final CancelAppointmentForHairdresserController controller;
    private final Runnable onCloseAction;
    private JFrame frame;
    private JTable appointmentTable;

    /**
     * Costruttore della vista.
     *
     * @param controller    il controller per la gestione degli appuntamenti
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public CancelAppointmentForHairdresserView(CancelAppointmentForHairdresserController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Mostra la finestra della vista.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Gestisci Appuntamenti - Parrucchiere");
        frame.setBounds(100, 100, 700, 400);
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

        JLabel titleLabel = new JLabel("Seleziona un appuntamento da disdire");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID Appuntamento", "Data", "Stato", "Cliente"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.getColumnModel().getColumn(0).setMinWidth(0);
        appointmentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        appointmentTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        try {
            List<String[]> appointments = controller.getValidAppointmentsForHairdresser();
            Map<Integer, String> clientNames = controller.getClientNames();
            updateAppointmentTable(appointments, clientNames);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento degli appuntamenti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton cancelAppointmentButton = new JButton("Disdici");
        cancelAppointmentButton.setPreferredSize(new Dimension(0, 40));
        cancelAppointmentButton.addActionListener(this::onCancelAppointmentClicked);
        buttonPanel.add(cancelAppointmentButton);
    }

    /**
     * Gestisce il click sul pulsante di cancellazione dell'appuntamento.
     *
     * @param e l'evento di azione
     */
    private void onCancelAppointmentClicked(ActionEvent e) {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un appuntamento da disdire.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int appointmentId = Integer.parseInt(appointmentTable.getValueAt(selectedRow, 0).toString());
            boolean success = controller.cancelAppointment(appointmentId);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Appuntamento disdetto con successo!");
                List<String[]> updatedAppointments = controller.getValidAppointmentsForHairdresser();
                Map<Integer, String> clientNames = controller.getClientNames();
                updateAppointmentTable(updatedAppointments, clientNames);
            } else {
                JOptionPane.showMessageDialog(frame, "Errore durante l'annullamento dell'appuntamento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Errore durante l'annullamento dell'appuntamento.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiorna la tabella degli appuntamenti.
     *
     * @param appointments la lista degli appuntamenti
     * @param clientNames  la mappa dei nomi dei clienti
     */
    private void updateAppointmentTable(List<String[]> appointments, Map<Integer, String> clientNames) {
        DefaultTableModel tableModel = (DefaultTableModel) appointmentTable.getModel();
        tableModel.setRowCount(0);
        for (String[] appointment : appointments) {
            int clientId = Integer.parseInt(appointment[3]);
            String clientName = clientNames.getOrDefault(clientId, "Sconosciuto");
            tableModel.addRow(new Object[]{appointment[0], appointment[1], appointment[2], clientName});
        }
    }
}