package core;

public class Payment {
    private int paymentId;
    private int custId;
    private String cardNumber;
    private String cvv;
    private String transactionId;

    public Payment() {}

    public Payment(int paymentId, int custId, String cardNumber, String cvv) {
        this.paymentId = paymentId;
        this.custId = custId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    // Getters and setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getCustId() { return custId; }
    public void setCustId(int custId) { this.custId = custId; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
