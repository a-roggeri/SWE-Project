package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.ModifyServicesController;
import Tokyogroup.GestioneAppuntamenti.model.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe che rappresenta la vista per la modifica dei servizi
 * offerti.
 */
public class ModifyServicesView {

    private JFrame frame;
    private JList<Service> servicesList;
    private DefaultListModel<Service> listModel;
    private final ModifyServicesController controller;
    private final Runnable onCloseAction;
    private boolean isChildViewOpened = false;

    /**
     * Costruttore della classe ModifyServicesView.
     *
     * @param controller    il controller per la modifica dei servizi
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public ModifyServicesView(ModifyServicesController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Inizializza la finestra e i suoi componenti.
     */
    private void initialize() {
        frame = new JFrame("Modifica Servizi Offerti");
        frame.setBounds(100, 100, 400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (!isChildViewOpened && onCloseAction != null) {
                    onCloseAction.run();
                }
            }
        });

        listModel = new DefaultListModel<>();
        servicesList = new JList<>(listModel);
        servicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(servicesList);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setPreferredSize(new Dimension(0, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        JButton removeButton = new JButton("Rimuovi");
        removeButton.setPreferredSize(new Dimension(0, 40));
        removeButton.addActionListener(this::handleRemoveService);
        buttonPanel.add(removeButton);

        JButton addButton = new JButton("Aggiungi");
        addButton.setPreferredSize(new Dimension(0, 40));
        addButton.addActionListener(this::handleAddService);
        buttonPanel.add(addButton);

        loadServices();
    }

    /**
     * Carica i servizi dal controller e li aggiunge alla lista.
     */
    private void loadServices() {
        try {
            List<Service> services = controller.getServicesForHairdresser();
            listModel.clear();
            for (Service service : services) {
                listModel.addElement(service);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento dei servizi.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestisce la rimozione di un servizio selezionato.
     *
     * @param e l'evento di azione
     */
    private void handleRemoveService(ActionEvent e) {
        Service selectedService = servicesList.getSelectedValue();
        if (selectedService == null) {
            JOptionPane.showMessageDialog(frame, "Seleziona un servizio da rimuovere.", "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Rimuovere questo servizio annullerà gli appuntamenti futuri associati. Continuare?",
                "Conferma Rimozione Servizio",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = controller.removeServiceAndCancelAppointments(selectedService.getId());
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Servizio rimosso con successo. Appuntamenti aggiornati.",
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                    loadServices();
                } else {
                    JOptionPane.showMessageDialog(frame, "Errore durante la rimozione del servizio.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Errore durante la rimozione del servizio e l'aggiornamento degli appuntamenti.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Gestisce l'aggiunta di un nuovo servizio.
     *
     * @param e l'evento di azione
     */
    private void handleAddService(ActionEvent e) {
        isChildViewOpened = true;
        frame.dispose();
        SwingUtilities.invokeLater(() -> new AddExistingServiceView(controller, () -> {
            isChildViewOpened = false;
            this.show();
            this.loadServices();
        }).show());
    }

    /**
     * Mostra la finestra.
     */
    public void show() {
        frame.setVisible(true);
    }
}