package entities;

public class Contact {
    private final int contactID;
    private final String firstName;
    private final String lastName;
    private final String role;
    private final String phoneNumber;

    public Contact(int contactID, String firstName, String lastName, String role, String phoneNumber) {
        this.contactID = contactID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public int getContactID() {
        return contactID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
