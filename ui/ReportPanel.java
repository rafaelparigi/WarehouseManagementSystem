/**
 * @Rafael
 * @09Jun
 */
package ui;
import backend.WarehouseManagementSystem;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    private WarehouseManagementSystem system;
    private JTextArea reportArea;

    public ReportPanel(WarehouseManagementSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton salesButton = new JButton("Show Sales Revenue");
        JButton incomeButton = new JButton("Show Net Income");
        JButton stockReportButton = new JButton("Show Stock Purchase Report");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salesButton);
        buttonPanel.add(incomeButton);
        buttonPanel.add(stockReportButton);

        add(buttonPanel, BorderLayout.NORTH);

        salesButton.addActionListener(e -> {
            double sales = system.calculateSalesRevenue();
            reportArea.setText("Sales Revenue: £" + sales);
        });

        incomeButton.addActionListener(e -> {
            double income = system.calculateNetIncome(system.getSuppliers(), system.getCustomerOrders());
                reportArea.setText("Net Income: £" + income);
        });

        stockReportButton.addActionListener(e -> {
            String report = system.getStockPurchaseReport();
            reportArea.setText("Stock Purchase Report:\n" + report);
        });

    }
}


