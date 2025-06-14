/**
 * @Rafael
 * @15May
 */

package backend;

import java.util.ArrayList;

public class Supplier
{
    private ContactDetails contactDetails;
    private ArrayList<Invoice> orders;
  
    public Supplier(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
        this.orders = new ArrayList<Invoice>();
    }
    
    public ContactDetails getContactDetails() {
        ContactDetails cd = contactDetails;
        return contactDetails;
    }
    
    public ArrayList<Invoice> getOrders() {
        return orders;
    }
    
    public void addOrder(Invoice order) {
        if (order.getType() == InvoiceType.STOCK_DELIVERY) {
            orders.add(order);
            System.out.println("Order: " + order.getId() + " added to supplier" + contactDetails.getName());
        } else {
            System.out.println("Invoice must be of type " + InvoiceType.STOCK_DELIVERY);
        }
    }
    
    public void updateSupplier(ContactDetails contactDetails) {
        this.contactDetails.update(contactDetails);
    }

}
