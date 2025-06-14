/**
 * @Rafael
 * @29May
 */

package backend_tests;
import backend.*;
import UI.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvoiceTest
{
    private Invoice invoice;
    private QuantityPricePair quantityPrice1;
    private QuantityPricePair quantityPrice2;
    static final double DELTA = 0.01;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice(InvoiceType.CUSTOMER_ORDER);
        quantityPrice1 = new QuantityPricePair(5, 20.00);
        quantityPrice2 = new QuantityPricePair(3, 15.00);
    }

    @Test
    public void testInvoiceCreation() {
        assertNotNull(invoice.getId());
        assertNotNull(invoice.getBillOfItems());
        assertEquals(InvoiceType.CUSTOMER_ORDER, invoice.getType());
    }
    
    @Test
    public void testAddInvoiceLine() {
        invoice.addInvoiceLine("P001", quantityPrice1);

        assertTrue(invoice.getBillOfItems().containsKey("P001"));
        assertEquals(5, invoice.getBillOfItems().get("P001").getQuantity());
        assertEquals(20.0, invoice.getBillOfItems().get("P001").getUnitPrice(), DELTA);
    }
    
    @Test
    public void getTotalPrice() {
        invoice.addInvoiceLine("P001", quantityPrice1);
        invoice.addInvoiceLine("P002", quantityPrice2);

        assertEquals(5 * 20.0 + 3 * 15.0, invoice.getTotalPrice(), DELTA);
    }
}
