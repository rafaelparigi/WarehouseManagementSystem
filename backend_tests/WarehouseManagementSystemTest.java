/**
 * @Rafael
 * @31May
 */

package backend_tests;
import backend.*;
import UI.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseManagementSystemTest {
    
    private WarehouseManagementSystem wms;
    private Supplier supplier;
    private Product product;
    private Invoice stockInvoice;
    private Invoice customerInvoice;

    @BeforeEach
    public void setUp() {
        wms = new WarehouseManagementSystem();
        product = new Product("P001", 0, 10);
        
        supplier = new Supplier(new ContactDetails("Supplier A", "123123123123"));
        stockInvoice = new Invoice(InvoiceType.STOCK_DELIVERY);
        stockInvoice.addInvoiceLine("P001", new QuantityPricePair(50, 5.0));
        supplier.addOrder(stockInvoice);

        customerInvoice = new Invoice(InvoiceType.CUSTOMER_ORDER);
        customerInvoice.addInvoiceLine("P001", new QuantityPricePair(10, 15.0));
    }
    
    @Test
    public void testAddSupplier() {
        wms.addSupplier(supplier);
        assertTrue(wms.getSuppliers().contains(supplier));
    }
    @Test
    public void testDeleteSupplier() {
        wms.addSupplier(supplier);
        wms.deleteSupplier(supplier);
        assertFalse(wms.getSuppliers().contains(supplier));
    }

    @Test
    public void testAddProduct() {
        wms.addProduct(product);
        assertTrue(wms.getInventory().containsKey("P001"));
    }

    @Test
    public void testReceiveStockDelivery() {
        wms.addProduct(product);
        wms.receiveStockDelivery(stockInvoice);
        assertEquals(50, wms.getInventory().get("P001").getStock());
    }

    @Test
    public void testProcessCustomerOrder() {
        wms.addProduct(product);
        wms.receiveStockDelivery(stockInvoice); // Ensure enough stock
        wms.processCustomerOrder(customerInvoice);
        assertEquals(40, wms.getInventory().get("P001").getStock());
    }

    @Test
    public void testCalculateSalesRevenue() {
        wms.addProduct(product);
        wms.receiveStockDelivery(stockInvoice);
        wms.processCustomerOrder(customerInvoice);
        assertEquals(150.0, wms.calculateSalesRevenue(), 0.01);
    }

    @Test
    public void testCalculateNetIncome() {
        wms.addSupplier(supplier);
        List<Supplier> suppliers = wms.getSuppliers();
        
        wms.addProduct(product);
        List<Invoice> customerOrders = new ArrayList<>();
        customerOrders.add(customerInvoice);
        
        assertEquals(150.0 - 250.0, wms.calculateNetIncome(suppliers, customerOrders), 0.01);
    }

}
