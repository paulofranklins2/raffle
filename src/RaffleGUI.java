import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RaffleGUI implements ActionListener {
    private final JFrame frame;
    private final JPanel panel;
    private final JTextField numRunsField;
    private final JButton runButton;
    private final JTable namesTable;
    private final DefaultTableModel winnersModel;
    private final List<String> winners = new ArrayList<>();
    private final GridBagConstraints gridBagConstraints;


    public RaffleGUI() {
        frame = new JFrame("Raffle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridBagConstraints = new GridBagConstraints();

        JLabel label = new JLabel("Enter the number of times to run the raffle:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 0, 10, 10);
        panel.add(label, gridBagConstraints);

        numRunsField = new JTextField(10);
        numRunsField.setPreferredSize(new Dimension(100, 30));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 0, 10, 10);
        panel.add(numRunsField, gridBagConstraints);

        runButton = new JButton("Run Raffle");
        runButton.setFont(new Font("Arial", Font.PLAIN, 14));
        runButton.addActionListener(this);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        panel.add(runButton, gridBagConstraints);

        winnersModel = new DefaultTableModel();
        winnersModel.addColumn("Raffle #");
        winnersModel.addColumn("Winner");

        JTable winnersTable = new JTable(winnersModel);
        JScrollPane winnersScrollPane = new JScrollPane(winnersTable);
        winnersScrollPane.setPreferredSize(new Dimension(400, 300));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        panel.add(winnersScrollPane, gridBagConstraints);

        DefaultTableModel namesModel = new DefaultTableModel();
        namesModel.addColumn("Name");
        namesModel.addColumn("Number of Tickets");

        namesTable = new JTable(namesModel);
        // Import the list of names from the "names.txt" file
        List<String> names = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/names.txt"))) {
            String name;
            while ((name = br.readLine()) != null) {
                names.add(name);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage());
            return;
        }

        // Create a map to keep track of the number of tickets each person has
        Map<String, Integer> tickets = new HashMap<>();
        for (String name : names) {
            tickets.put(name, tickets.getOrDefault(name, 0) + 1);
        }

        // Sort the entries of the map by key (i.e., by name)
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(tickets.entrySet());
        Collections.sort(entries, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));

        // Create a panel to show the list of names and their tickets
        JPanel namesPanel = new JPanel(new BorderLayout());

        JTable namesTable = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Number of Tickets");

        for (Map.Entry<String, Integer> entry : entries) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        namesTable.setModel(model);

        JScrollPane namesScrollPane = new JScrollPane(namesTable);
        namesScrollPane.setPreferredSize(new Dimension(400, 300));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(namesScrollPane, gridBagConstraints);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        // Set custom colors and alignment for tables
        winnersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        winnersTable.getTableHeader().setBackground(new Color(204, 204, 204));
        winnersTable.getTableHeader().setForeground(Color.BLACK);
        winnersTable.setRowHeight(25);
        winnersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        winnersTable.getColumnModel().getColumn(1).setPreferredWidth(350);

        namesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        namesTable.getTableHeader().setBackground(new Color(204, 204, 204));
        namesTable.getTableHeader().setForeground(Color.BLACK);
        namesTable.setRowHeight(25);
        namesTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        namesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        namesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Center the JFrame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(centerX, centerY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButton) {
            // Clear previous results
            winners.clear();
            winnersModel.setRowCount(0);
            String numRunsString = numRunsField.getText();
            int numRuns = 0;
            try {
                numRuns = Integer.parseInt(numRunsString);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid integer.");
                return;
            }

            // Import the list of names from the "names.txt" file
            List<String> names = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("src/names.txt"))) {
                String name;
                while ((name = br.readLine()) != null) {
                    names.add(name);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage());
                return;
            }

            // Create a map to keep track of the number of tickets each person has
            Map<String, Integer> tickets = new HashMap<>();
            for (String name : names) {
                tickets.put(name, tickets.getOrDefault(name, 0) + 1);
            }

            // Create a panel to show the list of names and their tickets
            JPanel namesPanel = new JPanel(new BorderLayout());

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Number of Tickets");

            for (Map.Entry<String, Integer> entry : tickets.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }

            namesTable.setModel(model);

            JScrollPane namesScrollPane = new JScrollPane(namesTable);
            namesPanel.add(namesScrollPane, BorderLayout.CENTER);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            panel.add(namesPanel, gridBagConstraints);

            namesScrollPane.setPreferredSize(new Dimension(400, 300));
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            panel.add(namesScrollPane, gridBagConstraints);

            // Run the raffle for the specified number of times
            Random random = new Random();
            for (int i = 1; i <= numRuns; i++) {
                int winningIndex = random.nextInt(names.size());
                String winner = names.get(winningIndex);
                winners.add(winner);
                winnersModel.addRow(new Object[]{i, winner});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RaffleGUI();
            }
        });
    }
}