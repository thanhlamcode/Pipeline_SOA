package filters;

import core.Order;
import core.OrderRequest;
import core.entities.IFilter;
import core.entities.IMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class InventoryCheckFilter implements IFilter {
    private static final Logger logger = Logger.getLogger(InventoryCheckFilter.class.getName());
    private static final Map<Integer, Integer> inventory = new ConcurrentHashMap<>();

    static {
        // Khởi tạo kho hàng (ID sản phẩm -> Số lượng tồn kho)
        for (int i = 1; i <= 50; i++) {
            inventory.put(i, i % 2 == 0 ? 500 : 1000);
        }
    }

    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof OrderRequest;
    }

    @Override
    public IMessage execute(IMessage message) {
        if (message == null || message.getPayload() == null) {
            throw new IllegalArgumentException("IMessage hoặc payload bị null");
        }

        OrderRequest orderRequest = (OrderRequest) message.getPayload();

        if (orderRequest.getOrder_info() == null) {
            throw new IllegalArgumentException("OrderRequest hoặc OrderInfo bị null");
        }

        // Dùng bản sao để rollback nếu đơn hàng không hợp lệ
        Map<Integer, Integer> tempInventory = new ConcurrentHashMap<>(inventory);

        for (Order order : orderRequest.getOrder_info().getOrders()) {
            int productId = order.getProductId();
            int requested = order.getQuantity();
            int available = inventory.getOrDefault(productId, 0);

            if (available < requested) {
                message.setSuccess(false);
                message.setMessage("Sản phẩm " + productId + " không đủ số lượng. Còn lại: " + available + ", Yêu cầu: " + requested);

                // Khôi phục lại kho hàng trước đó (rollback)
                inventory.putAll(tempInventory);
                return message;
            }

            // Cập nhật tồn kho an toàn
            inventory.compute(productId, (key, currentStock) -> currentStock - requested);
            logger.info("Đã đặt trước " + requested + " sản phẩm " + productId + ". Còn lại: " + (available - requested));
        }

        message.setSuccess(true);
        message.setMessage("Tất cả sản phẩm đều đủ số lượng, đơn hàng hợp lệ.");
        return message;
    }

    // Lấy số lượng hàng tồn kho
    public static int getInventoryLevel(int productId) {
        return inventory.getOrDefault(productId, 0);
    }
}
