package model;

public class CartItem {
    private int productId;
    private String name;
    private int quantity;
    private double price;
    private double total;

    public CartItem(int productId, String name, int quantity, double price, double total) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
