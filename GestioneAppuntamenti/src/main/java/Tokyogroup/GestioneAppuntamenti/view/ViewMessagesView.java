package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.ViewMessagesController;
import Tokyogroup.GestioneAppuntamenti.model.Message;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe che rappresenta la vista per visualizzare i messaggi non letti.
 */
public class ViewMessagesView {

    private JFrame frame;
    private JTable messagesTable;
    private DefaultTableModel tableModel;
    private final ViewMessagesController controller;
    private final Runnable onCloseAction;

    /**
     * Costruttore della classe ViewMessagesView.
     *
     * @param controller    il controller per la gestione dei messaggi
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public ViewMessagesView(ViewMessagesController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Messaggi dai Clienti");
        frame.setBounds(100, 100, 1000, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            }
        });

        JLabel titleLabel = new JLabel("Messaggi Non Letti");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "Messaggio", "ID Messaggio", "Utente", "Data" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        messagesTable = new JTable(tableModel);
        messagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(messagesTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton markAsReadButton = new JButton("Segna come Letto");
        markAsReadButton.setPreferredSize(new Dimension(0, 40));
        markAsReadButton.addActionListener(this::handleMarkAsRead);
        buttonPanel.add(markAsReadButton);

        loadMessages();
    }

    /**
     * Carica i messaggi non letti nella tabella.
     */
    private void loadMessages() {
        try {
            List<Message> messages = controller.getUnreadMessages();
            tableModel.setRowCount(0);
            for (Message message : messages) {
                tableModel.addRow(new Object[] {
                        message.getMessageText(),
                        message.getId(),
                        message.getSenderUsername(),
                        message.getSentDate(),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento dei messaggi.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestisce l'azione di segnare un messaggio come letto.
     *
     * @param e l'evento di azione
     */
    private void handleMarkAsRead(ActionEvent e) {
        int selectedRow = messagesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un messaggio da segnare come letto.", "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int messageId = (int) messagesTable.getValueAt(selectedRow, 1);
        try {
            boolean success = controller.markMessageAsRead(messageId);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Messaggio segnato come letto.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                loadMessages();
            } else {
                JOptionPane.showMessageDialog(frame, "Errore durante l'aggiornamento del messaggio.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Errore durante l'aggiornamento del messaggio.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Mostra la finestra.
     */
    public void show() {
        frame.setVisible(true);
    }
}