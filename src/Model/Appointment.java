package Model;

import java.time.LocalDateTime;

/**
 * <h2>Appointment</h2>
 * The appointment class provides the structure and attributes in order to
 * create an appointment. Methods are provided in order to return attributes
 * including appointment types.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-04
 */

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String contact;
    private int contactId;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private String customer;
    private int customerId;
    private int days;

    // Full constructor
    public Appointment(int id, String title, String description, String location, String contact, int contactId, String type, LocalDateTime start, LocalDateTime end, String customer, int customerId, int days) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.contactId = contactId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.customerId = customerId;
        this.days = days;
    }

    // Reduced constructor to compare customer appointment times
    public Appointment(int id, LocalDateTime start, LocalDateTime end, int customerId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
    }

    /**
     * Method returns an array of appointment types for use in appointment creation
     * @return String Array
     */
    public static String[] appointmentTypes() {

        String[] types = {
                "General",
                "Introduction",
                "Consultation"};
        return types;
    }

    /**
     * Method returns an array of appointment types for use in appointment creation
     * @return String Array
     */
    public static String[] locations() {

        String[] types = {
                "Phoenix",
                "White Plains",
                "Montreal",
                "London"};
        return types;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) { this.contact = contact; }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getDays() { return days; }

    public void setDays(int days) { this.days = days; }

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getContactId() { return contactId; }

    public void setContactId(int contactId) { this.contactId = contactId; }
}
