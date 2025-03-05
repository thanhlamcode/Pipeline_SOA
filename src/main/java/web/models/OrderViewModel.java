package web.models;

import java.util.List;

public class OrderViewModel {
    private List<CartItem> items;
    private String address;
    private String cardNumber;
    private String cvv;
    private int customerId;

    // Constructors, getters, and setters
    public OrderViewModel() {}

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
}

