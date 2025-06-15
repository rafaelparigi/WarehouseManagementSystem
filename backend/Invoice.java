/**
 * @Rafael
 * @27May
 */

package backend;

import java.util.HashMap;
import java.util.UUID;
import java.time.LocalDateTime;

public class Invoice
{
    private String id;
    private LocalDateTime date;
    private InvoiceType type;
    private HashMap<String, QuantityPricePair> billOfItems;
    
    public Invoice(InvoiceType type) {
        this.id = UUID.randomUUID().toString();
        this.date = LocalDateTime.now();
        this.type = type;
        this.billOfItems = new HashMap<>();
    }
    
    public void addInvoiceLine(String productId, QuantityPricePair quantityPrice) {
        billOfItems.put(productId, quantityPrice);
    }

    public String getId() {
        return id;
    }
    
    public String getDate() {
        return date.toString();
    }
    
    public InvoiceType getType() {
        return type;
    }
    
    public HashMap<String, QuantityPricePair> getBillOfItems() {
        return billOfItems;
    }
    
    public double getTotalPrice() {
        double total = 0;
        
        for (QuantityPricePair pair : billOfItems.values()) {
            total += pair.calculateCost();
        }
        return total;
    }
}
