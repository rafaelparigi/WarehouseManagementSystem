/**
 * @Rafael
 * @21May
 */

package backend;

import java.util.UUID;

public class ContactDetails {
    private String id;
    private String name;
    private String phoneNumber;

    public ContactDetails(String name, String phoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void update(ContactDetails contactDetails) {
        name = contactDetails.name;
        phoneNumber = contactDetails.phoneNumber;
    }
}

