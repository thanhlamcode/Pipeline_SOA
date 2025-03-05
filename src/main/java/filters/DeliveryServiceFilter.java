package filters;

import core.Delivery;
import core.Order;
import core.OrderRequest;
import core.entities.IFilter;
import core.entities.IMessage;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class DeliveryServiceFilter implements IFilter {
    // Simulated delivery service database
    private static final Map<Integer, DeliveryRegionInfo> deliveryRegions = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    static {
        // Initialize delivery information for 100 regions
        for (int i = 1; i <= 100; i++) {
            // Base delivery fee between $5-$20 depending on region
            double baseFee = 5.0 + (i % 10) * 1.5;

            // Base delivery time between 1-5 days depending on region
            int baseDeliveryTimeInDays = 1 + (i % 5);

            deliveryRegions.put(i, new DeliveryRegionInfo(baseFee, baseDeliveryTimeInDays));
        }
    }

    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof OrderRequest;
    }

    @Override
    public IMessage execute(IMessage message) {
        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        Delivery delivery = orderRequest.getOrder_info().getDelivery();

        int nodeId = delivery.getNodeId();
        DeliveryRegionInfo regionInfo = deliveryRegions.get(nodeId);

        if (regionInfo == null) {
            message.setSuccess(false);
            message.setMessage("Invalid delivery region ID: " + nodeId);
            return message;
        }

        // Calculate delivery fee based on items and distance
        double totalFee = calculateDeliveryFee(orderRequest, regionInfo);

        // Calculate estimated delivery time
        int estimatedDeliveryTime = calculateDeliveryTime(orderRequest, regionInfo);

        // Add delivery information to the delivery object
        delivery.setDeliveryFee(totalFee);
        delivery.setEstimatedDeliveryDays(estimatedDeliveryTime);

        System.out.println("Delivery service: Estimated fee: $" + totalFee +
                ", Estimated delivery time: " + estimatedDeliveryTime + " days");

        return message;
    }

    private double calculateDeliveryFee(OrderRequest orderRequest, DeliveryRegionInfo regionInfo) {
        // Base fee for the region
        double fee = regionInfo.getBaseFee();

        // Add fee based on number of items and total quantity
        int totalQuantity = 0;
        for (Order order : orderRequest.getOrder_info().getOrders()) {
            totalQuantity += order.getQuantity();
        }

        // Add $0.5 per item
        fee += totalQuantity * 0.5;

        // Add random surcharge for "current conditions" (0-20%)
        double surcharge = fee * (random.nextDouble() * 0.2);
        fee += surcharge;

        return Math.round(fee * 100.0) / 100.0; // Round to 2 decimal places
    }

    private int calculateDeliveryTime(OrderRequest orderRequest, DeliveryRegionInfo regionInfo) {
        // Base time for the region
        int days = regionInfo.getBaseDeliveryTime();

        // Add time based on total quantity of items (1 day per 10 items)
        int totalQuantity = 0;
        for (Order order : orderRequest.getOrder_info().getOrders()) {
            totalQuantity += order.getQuantity();
        }

        days += totalQuantity / 10;

        // Random factor for weather, traffic, etc. (0-2 days)
        days += random.nextInt(3);

        return days;
    }

    // Helper class for delivery region information
    private static class DeliveryRegionInfo {
        private final double baseFee;
        private final int baseDeliveryTime;

        public DeliveryRegionInfo(double baseFee, int baseDeliveryTime) {
            this.baseFee = baseFee;
            this.baseDeliveryTime = baseDeliveryTime;
        }

        public double getBaseFee() {
            return baseFee;
        }

        public int getBaseDeliveryTime() {
            return baseDeliveryTime;
        }
    }

    // Method to get current delivery info for a region (for reporting)
    public static DeliveryRegionInfo getDeliveryInfo(int regionId) {
        return deliveryRegions.get(regionId);
    }
}
