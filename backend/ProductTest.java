
/**
 * @Rafael
 * @29May
 */

package backend;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTest
{
    @BeforeEach
    public void setUp()
    {
    }

    @Test
    public void testProductCreation() {
        Product product = new Product("P001", 50, 10);

        assertEquals("P001", product.getId());
        assertEquals(50, product.getStock());
        assertFalse(product.isLowStock());
    }
    
    @Test
    public void testStockReductionForCustomerOrder() {
        Product product = new Product("P002", 20, 5);
        product.updateStock(5, InvoiceType.CUSTOMER_ORDER);

        assertEquals(15, product.getStock());
    }
    
    @Test
    public void testStockIncreaseForStockDelivery() {
        Product product = new Product("P003", 10, 5);
        product.updateStock(10, InvoiceType.STOCK_DELIVERY);

        assertEquals(20, product.getStock());
    }
    
    @Test
    public void testLowStockDetection() {
        Product product = new Product("P004", 5, 10);
        
        assertTrue(product.isLowStock());
    }
}
