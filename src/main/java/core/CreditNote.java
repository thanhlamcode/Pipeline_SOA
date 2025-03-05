package core;

public class CreditNote {
    private int creditId;
    private int custId;
    private double creditLimit;
    private boolean isApproved;

    public CreditNote() {}

    public CreditNote(int creditId, int custId, double creditLimit, boolean isApproved) {
        this.creditId = creditId;
        this.custId = custId;
        this.creditLimit = creditLimit;
        this.isApproved = isApproved;
    }

    // Getters and setters
    public int getCreditId() { return creditId; }
    public void setCreditId(int creditId) { this.creditId = creditId; }
    public int getCustId() { return custId; }
    public void setCustId(int custId) { this.custId = custId; }
    public double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }
}
