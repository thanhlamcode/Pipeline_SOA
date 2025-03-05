package core;

import java.util.List;

public class Delivery {
    private int nodeId;
    private String note;
    private String deliveryAdd;
    private boolean isDelivery;
    private int estimatedDeliveryDays;
    private double deliveryFee;

    public Delivery() {}

    public Delivery(int nodeId, String note, String deliveryAdd, boolean isDelivery) {
        this.nodeId = nodeId;
        this.note = note;
        this.deliveryAdd = deliveryAdd;
        this.isDelivery = isDelivery;
    }

    // Getters and setters
    public int getNodeId() { return nodeId; }
    public void setNodeId(int nodeId) { this.nodeId = nodeId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getDeliveryAdd() { return deliveryAdd; }
    public void setDeliveryAdd(String deliveryAdd) { this.deliveryAdd = deliveryAdd; }
    public boolean isDelivery() { return isDelivery; }
    public void setDelivery(boolean delivery) { isDelivery = delivery; }

    public int getEstimatedDeliveryDays() {
        return estimatedDeliveryDays;
    }

    public void setEstimatedDeliveryDays(int estimatedDeliveryDays) {
        this.estimatedDeliveryDays = estimatedDeliveryDays;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
