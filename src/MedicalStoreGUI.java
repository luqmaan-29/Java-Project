import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class MedicalStoreGUI extends JFrame {
    private DatabaseManager dbManager;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, manufacturerField, priceField, stockField, idField;
    private JPanel mainPanel, tablePanel;

    public MedicalStoreGUI() {
        dbManager = new DatabaseManager();
        setTitle("ALL NEEDS MEDICAL STORE");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set Background Image and Scale it to fit the screen
        JLabel backgroundLabel = new JLabel(new ImageIcon(
                new ImageIcon(
                        "C:\\Users\\mohammed luqmaan\\OneDrive\\Desktop\\MedicalStoreManagement\\resources\\bg2.jpg")
                        .getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
        setContentPane(backgroundLabel);
        setLayout(new CardLayout());

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        initMainPanel();

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        initTablePanel();

        add(mainPanel, "Main");
        add(tablePanel, "Table");

        switchToPanel("Main");
    }

    private void initMainPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("ALL NEEDS MEDICAL STORE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(59, 155, 111)); // Soft green color for the title
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(titleLabel, gbc);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        gbc.insets = new Insets(10, 10, 10, 10);

        addLabeledField("Name:", nameField = new JTextField(), centerPanel, gbc, 0);
        addLabeledField("Manufacturer:", manufacturerField = new JTextField(), centerPanel, gbc, 1);
        addLabeledField("Price:", priceField = new JTextField(), centerPanel, gbc, 2);
        addLabeledField("Stock:", stockField = new JTextField(), centerPanel, gbc, 3);
        addLabeledField("Medicine ID:", idField = new JTextField(), centerPanel, gbc, 4);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false);

        buttonPanel.add(createStyledButton("Add Medicine", e -> addMedicine()));
        buttonPanel.add(createStyledButton("Update Stock", e -> showUpdateStockDialog()));
        buttonPanel.add(createStyledButton("Delete Medicine", e -> showDeleteMedicineDialog()));
        buttonPanel.add(createStyledButton("View Medicines", e -> viewMedicines()));

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void initTablePanel() {
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Manufacturer", "Price", "Stock" }, 0);
        medicineTable = new JTable(tableModel);
        medicineTable.setFont(new Font("Roboto", Font.PLAIN, 14));
        medicineTable.setForeground(new Color(51, 51, 51)); // Dark gray text color for table
        medicineTable.setRowHeight(24);

        JScrollPane tableScrollPane = new JScrollPane(medicineTable);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);

        JButton backButton = createStyledButton("Back", e -> switchToPanel("Main"));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(String labelText, JTextField textField, JPanel panel, GridBagConstraints gbc,
            int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(85, 85, 85)); // Soft gray for labels
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        textField.setPreferredSize(new Dimension(200, 24));
        panel.add(textField, gbc);
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(76, 201, 162)); // Fresh teal background for buttons
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), panelName);
    }

    private void addMedicine() {
        try {
            String name = nameField.getText();
            String manufacturer = manufacturerField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            dbManager.addMedicine(name, manufacturer, price, stock);
            JOptionPane.showMessageDialog(this, "Medicine Added Successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showDeleteMedicineDialog() {
        JDialog deleteDialog = new JDialog(this, "Delete Medicine", true);
        deleteDialog.setSize(400, 150);
        deleteDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Medicine Name:");
        JTextField nameField = new JTextField(20);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                dbManager.deleteMedicineByName(name);
                JOptionPane.showMessageDialog(this, "Medicine Deleted Successfully!");
                deleteDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteDialog.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        deleteDialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        deleteDialog.add(deleteButton, gbc);

        deleteDialog.setVisible(true);
    }

    private void showUpdateStockDialog() {
        JDialog updateDialog = new JDialog(this, "Update Stock", true);
        updateDialog.setSize(400, 200);
        updateDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel idLabel = new JLabel("Medicine ID:");
        JTextField idField = new JTextField(20);
        JLabel stockLabel = new JLabel("New Stock Value:");
        JTextField stockField = new JTextField(20);

        JButton updateButton = new JButton("Update Stock");
        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int newStock = Integer.parseInt(stockField.getText());
                dbManager.updateMedicineStock(id, newStock);
                JOptionPane.showMessageDialog(this, "Stock Updated Successfully!");
                updateDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        updateDialog.add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        updateDialog.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        updateDialog.add(stockLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        updateDialog.add(stockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        updateDialog.add(updateButton, gbc);

        updateDialog.setVisible(true);
    }

    private void viewMedicines() {
        try {
            List<Map<String, Object>> medicines = dbManager.getAllMedicines();
            tableModel.setRowCount(0);
            for (Map<String, Object> med : medicines) {
                tableModel.addRow(new Object[] {
                        med.get("id"),
                        med.get("name"),
                        med.get("manufacturer"),
                        med.get("price"),
                        med.get("stock")
                });
            }
            switchToPanel("Table");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        manufacturerField.setText("");
        priceField.setText("");
        stockField.setText("");
        idField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedicalStoreGUI().setVisible(true));
    }
}
