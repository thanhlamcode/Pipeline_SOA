package filters;

import core.Order;
import core.OrderRequest;
import core.entities.IFilter;
import core.entities.IMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryCheckFilter implements IFilter {
    // Simulated inventory database with thread-safe map
    private static final Map<Integer, Integer> inventory = new ConcurrentHashMap<>();

    static {
        // Initialize with product data (product ID -> quantity in stock)
        for (int i = 1; i <= 50; i++) {
            // Products with odd IDs have 1000 units, even IDs have 500 units
            inventory.put(i, i % 2 == 0 ? 500 : 1000);
        }
    }

    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof OrderRequest;
    }

    @Override
    public IMessage execute(IMessage message) {
        OrderRequest orderRequest = (OrderRequest) message.getPayload();

        // Check if all products are available in the requested quantities
        for (Order order : orderRequest.getOrder_info().getOrders()) {
            Integer stock = inventory.get(order.getProductId());

            if (stock == null || stock < order.getQuantity()) {
                message.setSuccess(false);
                message.setMessage("Product ID " + order.getProductId() +
                        " is out of stock or insufficient quantity. Available: " +
                        (stock == null ? 0 : stock) + ", Requested: " + order.getQuantity());
                return message;
            }

            // Update inventory (simulating a transactional update)
            inventory.put(order.getProductId(), stock - order.getQuantity());
            System.out.println("Reserved " + order.getQuantity() + " units of product " +
                    order.getProductId() + ". Remaining: " + (stock - order.getQuantity()));
        }

        return message;
    }

    // Method to get current inventory level (for reporting)
    public static int getInventoryLevel(int productId) {
        return inventory.getOrDefault(productId, 0);
    }
}
