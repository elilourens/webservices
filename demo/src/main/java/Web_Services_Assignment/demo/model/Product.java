package Web_Services_Assignment.demo.model;

public class Product {

    private String id;
    private String description;
    private double retailPrice;
    private int wholesaleStockLevel;
    private double wholesalePrice;

    public Product() {
    }

    public Product(String id, String description, double retailPrice, int wholesaleStockLevel, double wholesalePrice) {
        this.id = id;
        this.description = description;
        this.retailPrice = retailPrice;
        this.wholesaleStockLevel = wholesaleStockLevel;
        this.wholesalePrice = wholesalePrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getWholesaleStockLevel() {
        return wholesaleStockLevel;
    }

    public void setWholesaleStockLevel(int wholesaleStockLevel) {
        this.wholesaleStockLevel = wholesaleStockLevel;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }
}