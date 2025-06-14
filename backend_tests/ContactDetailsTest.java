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

public class ContactDetailsTest
{
    private ContactDetails contactD1;

    @BeforeEach
    public void setUp()
    {
        contactD1 = new ContactDetails("BNU", "123123123");  
    }

    public void testContactDetailsCreation() {
        assertEquals("C001", contactD1.getId());
        assertEquals("John Doe", contactD1.getName());
        assertEquals("123456789", contactD1.getPhoneNumber());
    }
    
    @Test
    public void testUpdateContactDetails() {
        ContactDetails contactD2 = new ContactDetails("newBNU", "456456456");
        contactD1.update(contactD2);

        assertEquals("newBNU", contactD1.getName());
        assertEquals("456456456", contactD1.getPhoneNumber());
    }
}




