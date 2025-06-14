/**
 * @Rafael
 * @01Jun
 */

package backend;

public class QuantityPricePair
{
    private int quantity;
    private double unitPrice;

    public QuantityPricePair(int quantity, double unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public double calculateCost() {
        return quantity * unitPrice;
    }

}
