package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.ChangePasswordController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Classe che rappresenta la vista per il cambio della password.
 */
public class ChangePasswordView {

    private JFrame frame;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel messageLabel;
    private final ChangePasswordController controller;
    private final Runnable onCloseAction;

    /**
     * Costruttore della classe ChangePasswordView.
     *
     * @param controller    il controller per gestire il cambio della password
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public ChangePasswordView(ChangePasswordController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Cambia Password");
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

        JLabel oldPasswordLabel = new JLabel("Vecchia Password:");
        oldPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(oldPasswordLabel);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(oldPasswordField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel newPasswordLabel = new JLabel("Nuova Password:");
        newPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(newPasswordField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel confirmPasswordLabel = new JLabel("Conferma Nuova Password:");
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(confirmPasswordField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

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
        cancelButton.addActionListener(e -> {
            frame.dispose();
        });
        buttonPanel.add(cancelButton);

        JButton changeButton = new JButton("Cambia");
        changeButton.setPreferredSize(new Dimension(0, 40));
        changeButton.addActionListener(this::handleChangePassword);
        buttonPanel.add(changeButton);
    }

    /**
     * Gestisce il cambio della password.
     *
     * @param e l'evento di azione
     */
    private void handleChangePassword(ActionEvent e) {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()) {
            showMessage("Tutti i campi sono obbligatori.", Color.RED);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showMessage("La nuova password e la conferma non corrispondono.", Color.RED);
            return;
        }

        if (newPassword.equals(oldPassword)) {
            showMessage("La nuova password deve essere diversa dalla vecchia.", Color.RED);
            return;
        }

        try {
            boolean success = controller.changePassword(oldPassword, newPassword);
            if (success) {
                showMessage("Password cambiata con successo!", Color.GREEN);
            } else {
                showMessage("Vecchia password non corretta.", Color.RED);
            }
        } catch (RuntimeException ex) {
            showMessage("Errore durante il cambio della password. Riprova.", Color.RED);
        }
    }

    /**
     * Mostra un messaggio all'utente.
     *
     * @param message il messaggio da mostrare
     * @param color   il colore del messaggio
     */
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    /**
     * Mostra la finestra di cambio password.
     */
    public void show() {
        frame.setVisible(true);
    }
}