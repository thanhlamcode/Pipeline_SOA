package core;

public class InvoiceInfo {
    private Invoice invoice;
    private Delivery delivery;
    private Payment payment;

    public InvoiceInfo() {}

    public InvoiceInfo(Invoice invoice, Delivery delivery, Payment payment) {
        this.invoice = invoice;
        this.delivery = delivery;
        this.payment = payment;
    }

    // Getters and setters
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    public Delivery getDelivery() { return delivery; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
