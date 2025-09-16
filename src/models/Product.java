package models;

public class Product {
    private int productID;
    private String name;
    private String category;
    private double price;
    private int stockQuantity;
    private int reorderLevel;
    private int supplierID;

    public Product() {}

    public Product(int productID, String name, String category, double price, int stockQuantity, int reorderLevel, int supplierID) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.supplierID = supplierID;
    }

    // getters and setters
    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
    public int getSupplierID() { return supplierID; }
    public void setSupplierID(int supplierID) { this.supplierID = supplierID; }
}
