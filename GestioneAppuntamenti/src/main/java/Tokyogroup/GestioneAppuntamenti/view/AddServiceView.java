package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.ModifyServicesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Classe per la gestione della finestra di aggiunta di un nuovo servizio.
 * Permette all'utente di inserire un nuovo servizio specificando nome e prezzo.
 */
public class AddServiceView {

    private JFrame frame;
    private JTextField serviceNameField;
    private JTextField servicePriceField;
    private JLabel messageLabel;
    private final ModifyServicesController controller;
    private final Runnable onCloseAction;
    private final Runnable onServiceAdded;

    /**
     * Costruttore della classe AddServiceView.
     *
     * @param controller     Il controller responsabile della gestione dei servizi.
     * @param onCloseAction  Azione da eseguire alla chiusura della finestra.
     * @param onServiceAdded Callback per aggiornare la lista dei servizi dopo
     *                       l'inserimento.
     */
    public AddServiceView(ModifyServicesController controller, Runnable onCloseAction, Runnable onServiceAdded) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        this.onServiceAdded = onServiceAdded;
        initialize();
    }

    /**
     * Inizializza i componenti grafici e la finestra principale.
     */
    private void initialize() {
        frame = new JFrame("Aggiungi Servizio");
        frame.setBounds(100, 100, 400, 300);
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

        JLabel nameLabel = new JLabel("Nome Servizio:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameLabel);

        serviceNameField = new JTextField();
        serviceNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(serviceNameField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel priceLabel = new JLabel("Prezzo Servizio (€):");
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(priceLabel);

        servicePriceField = new JTextField();
        servicePriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(servicePriceField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton insertButton = new JButton("Inserisci");
        insertButton.setPreferredSize(new Dimension(0, 40));
        insertButton.addActionListener(this::handleInsertService);
        buttonPanel.add(insertButton);
    }

    /**
     * Gestisce l'azione di inserimento del servizio.
     * 
     * @param e Evento generato dall'interazione con l'utente.
     */
    private void handleInsertService(ActionEvent e) {
        String serviceName = serviceNameField.getText().trim();
        String servicePriceText = servicePriceField.getText().trim();

        if (serviceName.isEmpty()) {
            showMessage("Il nome del servizio non può essere vuoto.", Color.RED);
            return;
        }

        if (servicePriceText.isEmpty()) {
            showMessage("Il prezzo del servizio non può essere vuoto.", Color.RED);
            return;
        }

        double servicePrice;
        try {
            servicePrice = Double.parseDouble(servicePriceText);
            if (servicePrice <= 0) {
                showMessage("Il prezzo deve essere maggiore di 0.", Color.RED);
                return;
            }
        } catch (NumberFormatException ex) {
            showMessage("Inserisci un prezzo valido.", Color.RED);
            return;
        }

        try {
            int success = controller.addNewService(serviceName, servicePrice);
            if (success >= 0) {
                showMessage("Servizio aggiunto con successo!", Color.GREEN);
                serviceNameField.setText("");
                servicePriceField.setText("");
                if (onServiceAdded != null) {
                    onServiceAdded.run();
                }
            } else {
                showMessage("Esiste già un servizio con questo nome.", Color.RED);
            }
        } catch (RuntimeException ex) {
            showMessage("Errore durante l'aggiunta del servizio.", Color.RED);
        }
    }

    /**
     * Mostra un messaggio all'utente.
     * 
     * @param message Testo del messaggio.
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
