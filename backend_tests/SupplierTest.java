/**
 * @Rafael
 * @28May
 */

package backend_tests;
import backend.*;
import UI.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupplierTest
{
    private ContactDetails contactD1;
    private Supplier supplier;
    private Invoice order1;
    private Invoice order2;
    private java.util.ArrayList<Invoice> orders;

    @BeforeEach
    public void setUp() {
        contactD1 = new ContactDetails("BNU", "123123123");
        supplier = new Supplier(contactD1);
        
        order1 = new Invoice(InvoiceType.STOCK_DELIVERY);
        order2 = new Invoice(InvoiceType.CUSTOMER_ORDER);
        orders = new java.util.ArrayList<Invoice>();
    }
    
    @Test
    public void testSupplierCreation() {
        Supplier supplier = new Supplier(contactD1);

        assertNotNull(supplier.getContactDetails());
        assertNotNull(supplier.getOrders());
        assertTrue(supplier.getOrders().isEmpty());
    }
    
    @Test
    public void testAddOrderWithStockDeliveryType() {
        supplier.addOrder(order1);
        orders = supplier.getOrders();
        
        assertEquals(1, orders.size());
        assertEquals(order1, orders.get(0));
    }
    
    @Test
    public void testAddOrderWithNonStockDeliveryType() {
        supplier.addOrder(order2);
        orders = supplier.getOrders();
        
        assertEquals(0, orders.size());
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void getContactDetails() {
        assertEquals(contactD1, supplier.getContactDetails());
    }
    
    @Test
    public void testUpdateSupplier() {
        ContactDetails contactD2 = new ContactDetails("newBNU", "456456456");
        supplier.updateSupplier(contactD2);
        
        assertEquals("newBNU", supplier.getContactDetails().getName());
        assertEquals("456456456", supplier.getContactDetails().getPhoneNumber());
    }

}
