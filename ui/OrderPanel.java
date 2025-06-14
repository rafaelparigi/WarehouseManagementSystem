/**
 * @Rafael
 * @10Jun
 */

package ui;
import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OrderPanel extends JPanel {
    private WarehouseManagementSystem system;
    private SupplierPanel supplierPanel;
    private InventoryPanel inventoryPanel;
    private JTextField productIdField, quantityField, priceField;
    private JComboBox<String> orderTypeBox;
    private JComboBox<String> supplierBox;
    private JLabel supplierLabel;
    private JTextArea orderSummary;
    private JButton processButton, newOrderButton, addItemButton, deleteItemButton;
    private Invoice currentInvoice;
    private DefaultListModel<String> itemListModel;
    private JList<String> itemList;

    public OrderPanel(WarehouseManagementSystem system, SupplierPanel supplierPanel, InventoryPanel inventoryPanel) {
        this.system = system;
        this.supplierPanel = supplierPanel;
        this.inventoryPanel = inventoryPanel;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        productIdField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        orderTypeBox = new JComboBox<>(new String[]{"Customer Order", "Stock Delivery"});
        supplierLabel = new JLabel("Supplier (for Stock Delivery):");
        supplierBox = new JComboBox<>();
        newOrderButton = new JButton("New Order");
        addItemButton = new JButton("Add Item");
        processButton = new JButton("Process Order");
        deleteItemButton = new JButton("Delete Selected Item");

        supplierLabel.setVisible(false);
        supplierBox.setVisible(false);

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Unit Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Order Type:"));
        inputPanel.add(orderTypeBox);
        inputPanel.add(supplierLabel);
        inputPanel.add(supplierBox);
        inputPanel.add(newOrderButton);
        inputPanel.add(addItemButton);

        add(inputPanel, BorderLayout.NORTH);

        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        add(new JScrollPane(itemList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        orderSummary = new JTextArea(5, 30);
        orderSummary.setEditable(false);
        bottomPanel.add(new JScrollPane(orderSummary), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(processButton);
        buttonPanel.add(deleteItemButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        processButton.setEnabled(false);
        newOrderButton.setEnabled(true);

        orderTypeBox.addActionListener(e -> {
            boolean isStockDelivery = orderTypeBox.getSelectedIndex() == 1;
            supplierLabel.setVisible(isStockDelivery);
            supplierBox.setVisible(isStockDelivery);
            if (isStockDelivery) refreshSupplierBox();
        });

        newOrderButton.addActionListener(e -> {
            InvoiceType type = orderTypeBox.getSelectedIndex() == 0 ? InvoiceType.CUSTOMER_ORDER : InvoiceType.STOCK_DELIVERY;
            currentInvoice = new Invoice(type);
            itemListModel.clear();
            orderSummary.setText("New " + type + " created.\n");
            processButton.setEnabled(true);
            newOrderButton.setEnabled(false);
            if (type == InvoiceType.STOCK_DELIVERY) refreshSupplierBox();
        });

        addItemButton.addActionListener(e -> {
            if (currentInvoice == null) {
                JOptionPane.showMessageDialog(this, "Please create a new order first.");
                return;
            }
            try {
                String productId = productIdField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                if (currentInvoice.getBillOfItems().containsKey(productId)) {
                    JOptionPane.showMessageDialog(this, "This product is already in the order.");
                    return;
                }

                currentInvoice.addInvoiceLine(productId, new QuantityPricePair(quantity, price));
                itemListModel.addElement(productId + " x" + quantity + " @ £" + price);
                orderSummary.append("Added: " + productId + " x" + quantity + " @ £" + price + "\n");

                productIdField.setText("");
                quantityField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity or price.");
            }
        });

        deleteItemButton.addActionListener(e -> {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex != -1 && currentInvoice != null) {
                String selected = itemListModel.getElementAt(selectedIndex);
                String productId = selected.split(" ")[0];

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete item: " + productId + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    currentInvoice.getBillOfItems().remove(productId);
                    itemListModel.remove(selectedIndex);
                    orderSummary.append("Removed: " + productId + "\n");
                }
            }
        });

        processButton.addActionListener(e -> {
            if (currentInvoice == null) {
                JOptionPane.showMessageDialog(this, "No order to process.");
                return;
            }

            // Check for missing products
            StringBuilder missingProducts = new StringBuilder();
            for (String productId : currentInvoice.getBillOfItems().keySet()) {
                if (!system.getInventory().containsKey(productId)) {
                    missingProducts.append(productId).append("\n");
                }
            }

            if (missingProducts.length() > 0) {
                JOptionPane.showMessageDialog(this,
                    "The following products are not in the inventory:\n\n" +
                    missingProducts.toString() +
                    "\nPlease add them to the inventory before processing the order.",
                    "Missing Products",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Check for stock availability
            if (currentInvoice.getType() == InvoiceType.CUSTOMER_ORDER) {
                StringBuilder insufficientStock = new StringBuilder();
                for (String productId : currentInvoice.getBillOfItems().keySet()) {
                    Product product = system.getInventory().get(productId);
                    int requestedQty = currentInvoice.getBillOfItems().get(productId).getQuantity();
                    if (product.getStock() < requestedQty) {
                        insufficientStock.append(productId)
                                         .append(" - Requested: ").append(requestedQty)
                                         .append(", Available: ").append(product.getStock())
                                         .append("\n");
                    }
                }

                if (insufficientStock.length() > 0) {
                    JOptionPane.showMessageDialog(this,
                        "The following products do not have enough stock:\n\n" + insufficientStock.toString(),
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                system.processCustomerOrder(currentInvoice);
                orderSummary.append("Customer order processed.\n");
            } else {
                int selectedIndex = supplierBox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a supplier for stock delivery.");
                    return;
                }
                Supplier supplier = system.getSuppliers().get(selectedIndex);
                supplier.addOrder(currentInvoice);
                system.receiveStockDelivery(currentInvoice);
                orderSummary.append("Stock delivery processed for supplier: " + supplier.getContactDetails().getName() + "\n");
                supplierPanel.refreshSelectedSupplierInvoices();
            }

            inventoryPanel.refreshInventoryList();

            // Low stock alert
            StringBuilder lowStockAlert = new StringBuilder();
            for (Product product : system.getInventory().values()) {
                if (product.isLowStock()) {
                    lowStockAlert.append(product.getId())
                                 .append(" is low on stock (")
                                 .append(product.getStock())
                                 .append(" units left)\n");
                }
            }

            if (lowStockAlert.length() > 0) {
                JOptionPane.showMessageDialog(this,
                    "Low Stock Alert:\n" + lowStockAlert.toString(),
                    "Inventory Warning",
                    JOptionPane.WARNING_MESSAGE
                );
            }

            orderSummary.append("Total: £" + currentInvoice.getTotalPrice() + "\n\n");
            currentInvoice = null;
            processButton.setEnabled(false);
            newOrderButton.setEnabled(true);
            itemListModel.clear();
        });

        orderTypeBox.setSelectedIndex(0);
    }

    private void refreshSupplierBox() {
        supplierBox.removeAllItems();
        for (Supplier supplier : system.getSuppliers()) {
            supplierBox.addItem(supplier.getContactDetails().getName());
        }
    }
}








