package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.CancelAppointmentController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta la vista per la cancellazione degli appuntamenti.
 */
public class CancelAppointmentView {
    private final CancelAppointmentController cancelAppointmentController;
    private final Runnable onCloseAction;
    private JFrame frame;
    private JTable appointmentTable;

    /**
     * Costruttore della classe CancelAppointmentView.
     *
     * @param cancelAppointmentController il controller per la cancellazione degli
     *                                    appuntamenti
     * @param onCloseAction               l'azione da eseguire alla chiusura della
     *                                    finestra
     */
    public CancelAppointmentView(CancelAppointmentController cancelAppointmentController, Runnable onCloseAction) {
        this.cancelAppointmentController = cancelAppointmentController;
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
        frame = new JFrame("Disdici Appuntamenti");
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

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[] { "ID Appuntamento", "Data", "Stato", "Parrucchiere" }, 0) {
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
            List<String[]> appointments = cancelAppointmentController.getValidAppointmentsForClient();
            Map<Integer, String> hairdresserNames = cancelAppointmentController.getHairdresserNames();
            updateAppointmentTable(appointments, hairdresserNames);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento degli appuntamenti.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton closeButton = new JButton("Annulla");
        closeButton.setPreferredSize(new Dimension(0, 40));
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);

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
            JOptionPane.showMessageDialog(frame, "Seleziona un appuntamento da disdire.", "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int appointmentId = Integer.parseInt(appointmentTable.getValueAt(selectedRow, 0).toString());

            boolean success = cancelAppointmentController.cancelAppointment(appointmentId);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Appuntamento disdetto con successo!");
                List<String[]> updatedAppointments = cancelAppointmentController.getValidAppointmentsForClient();
                Map<Integer, String> hairdresserNames = cancelAppointmentController.getHairdresserNames();
                updateAppointmentTable(updatedAppointments, hairdresserNames);
            } else {
                JOptionPane.showMessageDialog(frame, "Errore durante l'annullamento dell'appuntamento.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Errore durante l'annullamento dell'appuntamento.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiorna la tabella degli appuntamenti.
     *
     * @param appointments     la lista degli appuntamenti
     * @param hairdresserNames la mappa degli ID dei parrucchieri con i loro nomi
     */
    private void updateAppointmentTable(List<String[]> appointments, Map<Integer, String> hairdresserNames) {
        DefaultTableModel tableModel = (DefaultTableModel) appointmentTable.getModel();
        tableModel.setRowCount(0);
        for (String[] appointment : appointments) {
            int hairdresserId = Integer.parseInt(appointment[3]);
            String hairdresserName = hairdresserNames.getOrDefault(hairdresserId, "Sconosciuto");
            tableModel.addRow(new Object[] { appointment[0], appointment[1], appointment[2], hairdresserName });
        }
    }
}