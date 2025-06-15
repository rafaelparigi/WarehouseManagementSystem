/**
 * @Rafael
 * @07Jun
 */

package ui;
import backend.WarehouseManagementSystem;
 
import javax.swing.*;

public class WarehouseManagementUI extends JFrame {
    private WarehouseManagementSystem system;

    public WarehouseManagementUI() {
        system = new WarehouseManagementSystem();
        setTitle("Warehouse Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        SupplierPanel supplierPanel = new SupplierPanel(system);
        InventoryPanel inventoryPanel = new InventoryPanel(system);
        OrderPanel orderPanel = new OrderPanel(system, supplierPanel, inventoryPanel);
        ReportPanel reportPanel = new ReportPanel(system);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Suppliers", supplierPanel);
        tabbedPane.addTab("Inventory", inventoryPanel);
        tabbedPane.addTab("Orders", orderPanel);
        tabbedPane.addTab("Reports", reportPanel);

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WarehouseManagementUI::new);
    }
}




