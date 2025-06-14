/**
 * @Rafael
 * @09Jun
 */

package ui;
import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class InventoryPanel extends JPanel {
    private WarehouseManagementSystem system;
    private DefaultListModel<String> inventoryListModel;
    private JList<String> inventoryList;

    public InventoryPanel(WarehouseManagementSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        inventoryListModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryListModel);

        // Custom renderer to highlight low-stock items in red
        inventoryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String itemText = value.toString();
                String productId = itemText.split(" ")[0];
                Product product = system.getInventory().get(productId);
                if (product != null && product.isLowStock()) {
                    label.setForeground(Color.RED);
                } else {
                    label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                }
                return label;
            }
        });

        add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JTextField idField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField minStockField = new JTextField();
        JButton addButton = new JButton("Add Product");

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Initial Stock:"));
        inputPanel.add(stockField);
        inputPanel.add(new JLabel("Minimum Stock:"));
        inputPanel.add(minStockField);
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                int stock = Integer.parseInt(stockField.getText().trim());
                int minStock = Integer.parseInt(minStockField.getText().trim());

                if (!id.isEmpty()) {
                    Product product = new Product(id, stock, minStock);
                    system.addProduct(product);
                    refreshInventoryList();
                    idField.setText("");
                    stockField.setText("");
                    minStockField.setText("");
                } else {
                    JOptionPane.showMessageDialog(InventoryPanel.this, "Product ID cannot be empty.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(InventoryPanel.this, "Please enter valid numbers for stock.");
            }
        });

        refreshInventoryList(); // Initial load
    }

    public void refreshInventoryList() {
        inventoryListModel.clear();
        for (Product product : system.getInventory().values()) {
            inventoryListModel.addElement(product.getId() + " - Stock: " + product.getStock());
        }
    }
}




