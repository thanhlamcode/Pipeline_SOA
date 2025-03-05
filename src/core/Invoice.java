package core;

import java.util.List;

public class Invoice {
    private int invoiceId;
    private int custId;
    private double amount;
    private boolean isPaid;
    private List<Order> orders;

    public Invoice() {}

    public Invoice(int invoiceId, int custId, double amount, boolean isPaid, List<Order> orders) {
        this.invoiceId = invoiceId;
        this.custId = custId;
        this.amount = amount;
        this.isPaid = isPaid;
        this.orders = orders;
    }

    // Getters and setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public int getCustId() { return custId; }
    public void setCustId(int custId) { this.custId = custId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
