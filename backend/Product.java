package backend;

/**
 * @Rafael
 * @27May
 */

import java.util.HashMap;

public class Product
{
    private String id;
    private int stock;
    private int minimumStock;

    public Product(String id, int stock, int minimumStock)
    {
        this.id = id;
        this.stock = stock;
        this.minimumStock = minimumStock;
    }
    
    public String getId() {
        return id;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void printProduct() {
        System.out.println(id + " : " + stock);
    }
    
    public void updateStock (int quantity, InvoiceType type) {
        if (type == InvoiceType.CUSTOMER_ORDER) {
            stock -= quantity;
        } else {
            stock += quantity;
        }
    }
    
    public boolean isLowStock() {
        return stock < minimumStock;
    }
    
}
