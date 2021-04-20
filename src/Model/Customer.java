package Model;

/**
 * <h2>Customer</h2>
 * The customer class provides the structure and attributes in order to
 * create a customer.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-04
 */

public class Customer {
    private final int id;
    private String name;
    private String address;
    private String postcode;
    private String country;
    private String state;
    private String phone;
    private int appointments;

    public Customer(int id, String name, String address, String postcode, String country, String state, String phone, int appointments) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.country = country;
        this.state = state;
        this.phone = phone;
        this. appointments = appointments;
    }

    public Customer(int id, String name, String address, String country, int appointments) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
        this.appointments = appointments;

    }


    // Getters and setters for Customer class
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAppointments() {
        return appointments;
    }

    public void setAppointments(int appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
