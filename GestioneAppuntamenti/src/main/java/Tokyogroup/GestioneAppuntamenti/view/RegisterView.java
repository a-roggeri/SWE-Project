package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.RegisterController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Classe che rappresenta la vista per la registrazione degli utenti.
 */
public class RegisterView {
	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JComboBox<String> accountTypeCombo;
	private JLabel messageLabel;
	private final RegisterController registerController;

	/**
	 * Costruttore della classe RegisterView.
	 * Inizializza il controller e la GUI.
	 */
	public RegisterView() {
		this.registerController = new RegisterController();
		initialize();
	}

	/**
	 * Inizializza i componenti della GUI.
	 */
	private void initialize() {
		frame = new JFrame("Registrati");
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		JLabel logoLabel = new JLabel();
		ImageIcon logoIcon = new ImageIcon("./resources/images/logos.png");
		Image scaledLogo = logoIcon.getImage().getScaledInstance(frame.getWidth(), 150, Image.SCALE_SMOOTH);
		logoLabel.setIcon(new ImageIcon(scaledLogo));
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(logoLabel, BorderLayout.CENTER);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

		JLabel lblUsername = new JLabel("Nome utente:");
		lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(lblUsername);

		usernameField = new JTextField();
		usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		centerPanel.add(usernameField);

		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		centerPanel.add(passwordField);

		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblAccountType = new JLabel("Tipo di Account:");
		lblAccountType.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(lblAccountType);

		accountTypeCombo = new JComboBox<>(new String[] { "CLIENTE", "GESTORE" });
		accountTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		centerPanel.add(accountTypeCombo);

		centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		messageLabel = new JLabel("");
		messageLabel.setForeground(Color.RED);
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(messageLabel);

		JPanel registerButtonPanel = new JPanel();
		registerButtonPanel.setLayout(new GridLayout(1, 1));
		registerButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.add(registerButtonPanel, BorderLayout.SOUTH);

		JButton registerButton = new JButton("Registrati");
		registerButton.setPreferredSize(new Dimension(0, 40));
		registerButton.addActionListener(this::handleRegister);
		registerButtonPanel.add(registerButton);
	}

	/**
	 * Mostra la finestra di registrazione.
	 */
	public void show() {
		frame.setVisible(true);
	}

	/**
	 * Gestisce l'evento di registrazione.
	 * 
	 * @param e l'evento di azione
	 */
	private void handleRegister(ActionEvent e) {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		String accountType = (String) accountTypeCombo.getSelectedItem();

		try {
			boolean success = registerController.registerUser(username, password, accountType);
			if (success) {
				showMessage("Registrazione completata!", Color.GREEN);
			} else {
				showMessage("Errore durante la registrazione.", Color.RED);
			}
		} catch (IllegalArgumentException ex) {
			showMessage(ex.getMessage(), Color.RED);
		} catch (RuntimeException ex) {
			showMessage(ex.getMessage(), Color.RED);
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
}