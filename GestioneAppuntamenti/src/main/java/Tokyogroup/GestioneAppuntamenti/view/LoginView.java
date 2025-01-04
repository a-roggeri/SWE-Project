package Tokyogroup.GestioneAppuntamenti.view;

import Tokyogroup.GestioneAppuntamenti.controller.LoginController;
import Tokyogroup.GestioneAppuntamenti.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Classe che rappresenta la vista per il login.
 */
public class LoginView {

	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel messageLabel;
	private final LoginController loginController;

	/**
	 * Costruttore della classe LoginView.
	 *
	 * @param loginController il controller per la gestione del login
	 */
	public LoginView(LoginController loginController) {
		this.loginController = loginController;
		initialize();
	}

	/**
	 * Inizializza i componenti della finestra di login.
	 */
	private void initialize() {
		frame = new JFrame("Login");
		frame.setBounds(100, 100, 500, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		JLabel registerLabel = new JLabel("<html><a href=''>Registrati</a></html>");
		registerLabel.setForeground(Color.BLUE);
		registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(registerLabel);

		centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		messageLabel = new JLabel("");
		messageLabel.setForeground(Color.RED);
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(messageLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 1));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.add(buttonPanel, BorderLayout.SOUTH);

		JButton loginButton = new JButton("Accedi");
		loginButton.setPreferredSize(new Dimension(0, 40));
		loginButton.addActionListener(this::handleLogin);
		buttonPanel.add(loginButton);

		registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				handleRegistrationClick();
			}
		});
	}

	/**
	 * Gestisce l'evento di login.
	 *
	 * @param e l'evento di azione
	 */
	private void handleLogin(ActionEvent e) {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		try {
			User user = loginController.authenticate(username, password);
			if (user != null) {
				if (user.isActive()) {
					frame.dispose();
					loginController.handleSuccessfulLogin(user);
				} else {
					int confirm = JOptionPane.showConfirmDialog(frame,
							"Account eliminato. Desideri ripristinarlo?",
							"Conferma Ripristino", JOptionPane.YES_NO_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						boolean restored = loginController.restoreAccount(user);
						if (restored) {
							JOptionPane.showMessageDialog(frame,
									"Account ripristinato con successo. Ora puoi accedere.");
						} else {
							showMessage("Errore durante il ripristino dell'account. Riprova.", Color.RED);
						}
					}
				}
			} else {
				showMessage("Credenziali non valide.", Color.RED);
			}
		} catch (IllegalArgumentException ex) {
			showMessage("Username o password non validi.", Color.RED);
		} catch (RuntimeException ex) {
			showMessage(ex.getMessage(), Color.RED);
		} catch (Exception ex) {
			showMessage("Errore durante il login. Riprova.", Color.RED);
		}
	}

	/**
	 * Gestisce il click sul link di registrazione.
	 */
	private void handleRegistrationClick() {
		loginController.openRegistration();
	}

	/**
	 * Mostra un messaggio nella finestra di login.
	 *
	 * @param message il messaggio da mostrare
	 * @param color   il colore del messaggio
	 */
	private void showMessage(String message, Color color) {
		messageLabel.setText(message);
		messageLabel.setForeground(color);
	}

	/**
	 * Mostra la finestra di login.
	 */
	public void show() {
		frame.setVisible(true);
	}
}