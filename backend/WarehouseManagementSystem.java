/**
 * @Rafael
 * @15May
 */

package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarehouseManagementSystem
{
    private ArrayList<Supplier> suppliers;
    private HashMap<String, Product> inventory;
    private ArrayList<Invoice> customerOrders;

    public WarehouseManagementSystem()
    {
        suppliers = new ArrayList<Supplier>();
        inventory = new HashMap<String, Product>();
        customerOrders = new ArrayList<Invoice>();
    }
    
    public HashMap<String, Product> getInventory()
    {
        return inventory;
    }
    
    public ArrayList<Supplier> getSuppliers()
    {
        return suppliers;
    }
    
    
    public ArrayList<Invoice> getCustomerOrders() {
        return customerOrders;
    }


    public void addSupplier(Supplier supplier)
    {
        suppliers.add(supplier);
        System.out.println(suppliers);
    }
    
    public void deleteSupplier(Supplier supplier)
    {
        int supplierIndex = suppliers.indexOf(supplier);
        suppliers.remove(supplierIndex);
        System.out.println(suppliers);
    }
    
    public void addProduct(Product product) {
        inventory.putIfAbsent(product.getId(), product);
    }

    public void printInventory() {
        for (Product product : inventory.values()) {
            product.printProduct();
        }
    }
    
    public void receiveStockDelivery(Invoice stockDelivery) {
        if (areAllProductsInInventory(stockDelivery)) {
            processInvoice(stockDelivery);
        }
    }
    
    public void processCustomerOrder(Invoice customerOrder) {
        if (areAllProductsInInventory(customerOrder) & isThereEnoughStockOfAllOrderedItems(customerOrder)) {
            customerOrders.add(customerOrder);
            processInvoice(customerOrder);
        }
    }
    
    public String getStockPurchaseReport() {
        StringBuilder report = new StringBuilder();
        for (Supplier supplier : suppliers) {
            for (Invoice order : supplier.getOrders()) {
                report.append(String.format("%-5s %-15s %-30s £%-10.2f%n",
                    supplier.getContactDetails().getId(),
                    supplier.getContactDetails().getName(),
                    order.getDate(),
                    order.getTotalPrice()));
            }
        }
        return report.toString();
    }


    public double calculateSalesRevenue() {
        double sales = calculateTotal(customerOrders);
        System.out.println("Sales: " + sales);
        return sales;
    }
    
    public double calculateNetIncome(List<Supplier> suppliers, List<Invoice> customerOrders) {
        double expenses = 0;
        for (Supplier supplier : suppliers) {
            expenses += calculateTotal(supplier.getOrders());
        }

        double revenue = calculateTotal(customerOrders);
        double netIncome = revenue - expenses;

        System.out.println("Net income: " + netIncome);
        return netIncome;
    }
    
    private boolean isProductInInventory(String productId) {
        return inventory.containsKey(productId);
    }
    
    private boolean areAllProductsInInventory(Invoice invoice) {
        for (String productId : invoice.getBillOfItems().keySet()) {
            if (!isProductInInventory(productId)){
                System.out.println("Product " + productId + " has not yet been added to the Warehouse Management System.");
                return false;
            }
        };
        return true;
    }
    
    private boolean isThereEnoughStockOfAllOrderedItems(Invoice customerOrder) {
        for (String productId : customerOrder.getBillOfItems().keySet()) {
            if (inventory.get(productId).getStock() < customerOrder.getBillOfItems().get(productId).getQuantity()){
                System.out.println("Product: " + productId + " - not enough in stock");
                return false;
            }
        };
        return true;
    }

    private void processInvoice(Invoice invoice) {
        invoice.getBillOfItems().forEach((productId, quantityPricePair) -> {
            inventory.get(productId).updateStock(quantityPricePair.getQuantity(), invoice.getType());
        });
    }
    
    private double calculateTotal(List<Invoice> invoices) {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTotalPrice();
        }
        return total;
    }

}
