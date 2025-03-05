package core;

public class Order {
    private int orderId;
    private int custId;
    private int productId;
    private int quantity;
    private String description;

    public Order() {}

    public Order(int orderId, int custId, int productId, int quantity, String description) {
        this.orderId = orderId;
        this.custId = custId;
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getCustId() { return custId; }
    public void setCustId(int custId) { this.custId = custId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}