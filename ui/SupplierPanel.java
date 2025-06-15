
/**
 * @Rafael
 * @11Jun
 */
package ui;
import backend.*;
 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.ArrayList;

public class SupplierPanel extends JPanel {
    private WarehouseManagementSystem system;
    private DefaultListModel<String> supplierListModel;
    private JList<String> supplierList;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTextField nameField, phoneField;
    private JLabel phoneDisplayLabel, idDisplayLabel;
    private JTextArea invoiceArea;

    public SupplierPanel(WarehouseManagementSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        // Supplier list
        supplierListModel = new DefaultListModel<>();
        supplierList = new JList<>(supplierListModel);

        // Invoice display
        invoiceArea = new JTextArea(8, 30);
        invoiceArea.setEditable(false);
        invoiceArea.setBorder(BorderFactory.createTitledBorder("Invoices"));

        // Draggable split pane
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(supplierList),
            new JScrollPane(invoiceArea)
        );
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // Display labels
        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        phoneDisplayLabel = new JLabel("Phone: ");
        idDisplayLabel = new JLabel("ID: ");
        displayPanel.add(phoneDisplayLabel);
        displayPanel.add(idDisplayLabel);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(displayPanel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        nameField = new JTextField();
        phoneField = new JTextField();
        addButton = new JButton("Add Supplier");
        updateButton = new JButton("Update Supplier");
        deleteButton = new JButton("Delete Supplier");
        clearButton = new JButton("Clear Fields");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        DocumentListener fieldListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkFields(); }
            public void removeUpdate(DocumentEvent e) { checkFields(); }
            public void changedUpdate(DocumentEvent e) { checkFields(); }

            private void checkFields() {
                boolean allFilled = !nameField.getText().trim().isEmpty() &&
                                    !phoneField.getText().trim().isEmpty();
                addButton.setEnabled(allFilled);
            }
        };

        nameField.getDocument().addDocumentListener(fieldListener);
        phoneField.getDocument().addDocumentListener(fieldListener);

        supplierList.addListSelectionListener(e -> {
            boolean selected = supplierList.getSelectedIndex() != -1;
            updateButton.setEnabled(selected);
            deleteButton.setEnabled(selected);

            if (selected) {
                Supplier supplier = system.getSuppliers().get(supplierList.getSelectedIndex());
                nameField.setText(supplier.getContactDetails().getName());
                phoneField.setText(supplier.getContactDetails().getPhoneNumber());

                phoneDisplayLabel.setText("Phone: " + supplier.getContactDetails().getPhoneNumber());
                idDisplayLabel.setText("ID: " + supplier.getContactDetails().getId());

                updateInvoiceArea(supplier);
            }
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();

            ContactDetails cd = new ContactDetails(name, phone);
            Supplier supplier = new Supplier(cd);
            system.addSupplier(supplier);
            supplierListModel.addElement(name);
            clearFields();
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = supplierList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this supplier?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    Supplier supplier = system.getSuppliers().get(selectedIndex);
                    system.deleteSupplier(supplier);
                    supplierListModel.remove(selectedIndex);
                    clearFields();
                }
            }
        });

        updateButton.addActionListener(e -> {
            int selectedIndex = supplierList.getSelectedIndex();
            if (selectedIndex != -1) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                Supplier supplier = system.getSuppliers().get(selectedIndex);
                ContactDetails updatedDetails = new ContactDetails(name, phone);
                supplier.updateSupplier(updatedDetails);
                supplierListModel.set(selectedIndex, name);
                phoneDisplayLabel.setText("Phone: " + phone);
                updateInvoiceArea(supplier);
            }
        });

        clearButton.addActionListener(e -> clearFields());
    }

    private void updateInvoiceArea(Supplier supplier) {
        StringBuilder sb = new StringBuilder();
        for (Invoice invoice : supplier.getOrders()) {
            sb.append("ID: ").append(invoice.getId())
              .append(" | Date: ").append(invoice.getDate())
              .append(" | Total: £").append(String.format("%.2f", invoice.getTotalPrice()))
              .append("\n");
        }
        invoiceArea.setText(sb.toString());
    }

    public void refreshSelectedSupplierInvoices() {
        int selectedIndex = supplierList.getSelectedIndex();
        if (selectedIndex != -1) {
            Supplier supplier = system.getSuppliers().get(selectedIndex);
            updateInvoiceArea(supplier);
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        supplierList.clearSelection();
        phoneDisplayLabel.setText("Phone: ");
        idDisplayLabel.setText("ID: ");
        invoiceArea.setText("");
        addButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}

