package core;

public class OrderRequest {
    private OrderInfo order_info;

    public OrderRequest() {}

    public OrderRequest(OrderInfo order_info) {
        this.order_info = order_info;
    }

    // Getters and setters
    public OrderInfo getOrder_info() { return order_info; }
    public void setOrder_info(OrderInfo order_info) { this.order_info = order_info; }
}
