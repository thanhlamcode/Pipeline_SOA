package core;

import java.util.List;

public class OrderInfo {
    private List<Order> orders;
    private Delivery delivery;
    private Payment payments;

    public OrderInfo() {}

    public OrderInfo(List<Order> orders, Delivery delivery, Payment payments) {
        this.orders = orders;
        this.delivery = delivery;
        this.payments = payments;
    }

    // Getters and setters
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public Delivery getDelivery() { return delivery; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }
    public Payment getPayments() { return payments; }
    public void setPayments(Payment payments) { this.payments = payments; }
}
