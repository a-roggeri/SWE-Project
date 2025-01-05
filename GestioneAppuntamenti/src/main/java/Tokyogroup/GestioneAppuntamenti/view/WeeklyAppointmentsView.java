package Tokyogroup.GestioneAppuntamenti.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Tokyogroup.GestioneAppuntamenti.controller.WeeklyAppointmentsController;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe per la visualizzazione degli appuntamenti settimanali.
 */
public class WeeklyAppointmentsView {

    private final WeeklyAppointmentsController controller;
    private final Runnable onCloseAction;
    private JFrame frame;
    private JTable table;
    private JLabel revenueLabel;

    /**
     * Costruttore della vista degli appuntamenti settimanali.
     *
     * @param controller    il controller degli appuntamenti settimanali
     * @param onCloseAction l'azione da eseguire alla chiusura della finestra
     */
    public WeeklyAppointmentsView(WeeklyAppointmentsController controller, Runnable onCloseAction) {
        this.controller = controller;
        this.onCloseAction = onCloseAction;
        initialize();
    }

    /**
     * Mostra la finestra degli appuntamenti settimanali.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Inizializza i componenti della finestra.
     */
    private void initialize() {
        frame = new JFrame("Appuntamenti Settimanali");
        frame.setBounds(100, 100, 1500, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            }
        });

        frame.getContentPane().setBackground(Color.GRAY);

        DefaultTableModel tableModel = new DefaultTableModel(24, 7) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                String value = (String) getValueAt(row, column);
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                panel.setBackground(Color.LIGHT_GRAY);
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                if (value != null && !value.isEmpty()) {
                    JLabel label = new JLabel("<html>" + value.replace("\n", "<br>") + "</html>");
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    label.setForeground(Color.BLACK);
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                    label.setVerticalAlignment(SwingConstants.TOP);

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.BOTH;
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                    panel.add(label, gbc);
                }

                LocalDate today = LocalDate.now();
                int currentDayColumn = today.getDayOfWeek().getValue() - 1;
                if (column == currentDayColumn) {
                    panel.setBackground(new Color(220, 220, 220));
                }

                return panel;
            }
        };

        table.setRowHeight(120);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(Color.DARK_GRAY);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        for (int i = 0; i < 7; i++) {
            String dayName = startOfWeek.plusDays(i).getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.getDefault());
            table.getColumnModel().getColumn(i).setHeaderValue(dayName);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        revenueLabel = new JLabel("Fatturato settimanale: €0.00");
        revenueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(revenueLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(e -> frame.dispose());
        bottomPanel.add(closeButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadWeeklyAppointments();
    }

    /**
     * Carica gli appuntamenti settimanali e aggiorna la tabella e il fatturato.
     */
    private void loadWeeklyAppointments() {
        try {
            Map<Integer, List<String[]>> weeklyAppointments = controller.getWeeklyAppointments();
            double totalRevenue = controller.calculateWeeklyRevenue();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            for (int hour = 0; hour < 24; hour++) {
                for (int day = 0; day < 7; day++) {
                    tableModel.setValueAt("", hour, day);
                }
            }

            for (Map.Entry<Integer, List<String[]>> entry : weeklyAppointments.entrySet()) {
                int dayOfWeek = entry.getKey();
                for (String[] appointment : entry.getValue()) {
                    int hour = Integer.parseInt(appointment[0]);
                    String client = appointment[1];
                    String status = appointment[2];
                    String services = appointment[3];

                    String cellText = String.format(
                            "Cliente:%s, Stato:%s, Servizi:%s",
                            client, status, services);
                    tableModel.setValueAt(cellText, hour, dayOfWeek);
                }
            }

            revenueLabel.setText(String.format("Fatturato settimanale: €%.2f", totalRevenue));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento degli appuntamenti settimanali.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}