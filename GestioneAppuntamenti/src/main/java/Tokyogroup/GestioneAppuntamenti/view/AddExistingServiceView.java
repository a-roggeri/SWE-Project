package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.ModifyServicesController;
import Tokyogroup.GestioneAppuntamenti.model.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Classe per aggiungere un servizio esistente.
 */
public class AddExistingServiceView {

    private JFrame frame;
    private JList<Service> servicesList;
    private DefaultListModel<Service> listModel;
    private final ModifyServicesController controller;
    private final Runnable onCloseAction;
    private boolean isChildViewOpened = false;

    /**
     * Costruttore della vista per aggiungere un servizio esistente.
     *
     * @param controller    il controller per la modifica dei servizi
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public AddExistingServiceView(ModifyServicesController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Aggiungi Servizio");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
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
        cancelButton.addActionListener(e -> {
            frame.dispose();
        });
        buttonPanel.add(cancelButton);

        JButton selectButton = new JButton("Aggiungi Servizio");
        selectButton.setPreferredSize(new Dimension(0, 40));
        selectButton.addActionListener(this::handleSelectService);
        buttonPanel.add(selectButton);

        JButton addNewServiceButton = new JButton("Nuovo Servizio");
        addNewServiceButton.setPreferredSize(new Dimension(0, 40));
        addNewServiceButton.addActionListener(this::handleAddNewService);
        buttonPanel.add(addNewServiceButton);

        loadServices();
    }

    /**
     * Gestisce l'azione di aggiungere un nuovo servizio.
     *
     * @param e l'evento di azione
     */
    private void handleAddNewService(ActionEvent e) {
        isChildViewOpened = true;
        frame.dispose();
        SwingUtilities.invokeLater(() -> new AddServiceView(controller, () -> {
            isChildViewOpened = false;
            this.show();
        }, this::loadServices).show());
    }

    /**
     * Carica i servizi disponibili.
     */
    private void loadServices() {
        try {
            List<Service> availableServices = controller.getAvailableServicesForHairdresser();
            listModel.clear();
            for (Service service : availableServices) {
                listModel.addElement(service);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento dei servizi disponibili.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestisce l'azione di selezionare un servizio.
     *
     * @param e l'evento di azione
     */
    private void handleSelectService(ActionEvent e) {
        Service selectedService = servicesList.getSelectedValue();
        if (selectedService == null) {
            JOptionPane.showMessageDialog(frame, "Seleziona un servizio da aggiungere.", "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean success = controller.addServiceToHairdresser(selectedService.getId());
            if (success) {
                JOptionPane.showMessageDialog(frame, "Servizio aggiunto con successo.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                loadServices();
            } else {
                JOptionPane.showMessageDialog(frame, "Errore durante l'aggiunta del servizio.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Errore durante l'aggiunta del servizio.", "Errore",
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