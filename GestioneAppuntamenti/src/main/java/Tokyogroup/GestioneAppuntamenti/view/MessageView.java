package Tokyogroup.GestioneAppuntamentiBarbiere.view;

import Tokyogroup.GestioneAppuntamenti.controller.MessageController;
import Tokyogroup.GestioneAppuntamenti.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe per la gestione della vista di invio messaggi.
 * Consente all'utente di inviare un messaggio a un gestore selezionato.
 */
public class MessageView {

    private JFrame frame;
    private JComboBox<User> managerComboBox;
    private JTextArea messageArea;
    private JLabel messageLabel;
    private final MessageController controller;
    private final Runnable onCloseAction;

    /**
     * Costruttore della classe MessageView.
     *
     * @param controller    Controller responsabile della gestione dei messaggi.
     * @param onCloseAction Azione da eseguire alla chiusura della finestra.
     */
    public MessageView(MessageController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Inizializza i componenti grafici della finestra.
     */
    private void initialize() {
        frame = new JFrame("Lascia un Messaggio");
        frame.setBounds(100, 100, 400, 400);
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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        JLabel managerLabel = new JLabel("Seleziona un Gestore:");
        managerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(managerLabel);

        managerComboBox = new JComboBox<>();
        managerComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(managerComboBox);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel messageTextLabel = new JLabel("Messaggio:");
        messageTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageTextLabel);

        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        mainPanel.add(scrollPane);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton sendButton = new JButton("Invia");
        sendButton.setPreferredSize(new Dimension(0, 40));
        sendButton.addActionListener(this::handleSendMessage);
        buttonPanel.add(sendButton);

        loadManagers();
    }

    /**
     * Carica la lista dei gestori disponibili nella ComboBox.
     */
    private void loadManagers() {
        try {
            List<User> managers = controller.getAllManagers();
            managerComboBox.removeAllItems();

            for (User manager : managers) {
                managerComboBox.addItem(manager);
            }
        } catch (RuntimeException ex) {
            showMessage("Errore durante il caricamento dei gestori.", Color.RED);
        }
    }

    /**
     * Gestisce l'invio del messaggio.
     *
     * @param e Evento generato dalla pressione del pulsante "Invia".
     */
    private void handleSendMessage(ActionEvent e) {
        User selectedManager = (User) managerComboBox.getSelectedItem();
        String message = messageArea.getText().trim();

        if (selectedManager == null) {
            showMessage("Seleziona un gestore.", Color.RED);
            return;
        }

        if (message.isEmpty()) {
            showMessage("Il messaggio non può essere vuoto.", Color.RED);
            return;
        }

        try {
            boolean success = controller.sendMessage(selectedManager.getId(), message);
            if (success) {
                showMessage("Messaggio inviato con successo!", Color.GREEN);
                messageArea.setText("");
            } else {
                showMessage("Errore nell'invio del messaggio. Riprova.", Color.RED);
            }
        } catch (RuntimeException ex) {
            showMessage("Errore durante l'invio del messaggio.", Color.RED);
        }
    }

    /**
     * Mostra un messaggio all'utente.
     *
     * @param message Testo del messaggio da mostrare.
     * @param color   Colore del messaggio (rosso per errore, verde per successo).
     */
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    /**
     * Mostra la finestra all'utente.
     */
    public void show() {
        frame.setVisible(true);
    }
}