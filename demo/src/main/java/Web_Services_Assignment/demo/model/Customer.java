package Web_Services_Assignment.demo.model;

public class Customer {

    private String id;
    private String name;
    private String email;
    private String postalAddress;
    private double revenue;

    public Customer() {
    }

    public Customer(String id, String name, String email, String postalAddress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.postalAddress = postalAddress;
        this.revenue = 0.0;
    }

    public Customer(String id, String name, String email, String postalAddress, double revenue) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.postalAddress = postalAddress;
        this.revenue = revenue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}