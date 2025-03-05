package web.models;

public class ProductViewModel {
    private int productId;
    private String productName;
    private double price;
    private String description;
    private int availableQuantity;

    // Constructors, getters, and setters
    public ProductViewModel() {}

    public ProductViewModel(int productId, String productName, double price, String description, int availableQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.availableQuantity = availableQuantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
}
